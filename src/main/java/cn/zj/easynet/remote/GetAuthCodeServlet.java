package cn.zj.easynet.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.zj.easynet.device.http.HttpClientPool;
import cn.zj.easynet.util.AuthCodeUtil;
import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.RedisUtils;
import cn.zj.easynet.util.ResponseCode;
import cn.zj.easynet.util.SendSMSUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.netease.om.Monitor;

/**
 * 发送短信验证码接口
 */
@WebServlet(name = "getAuthCodeServlet", loadOnStartup = 2,  urlPatterns = { "/nnl/getAuthCode" }, asyncSupported = false)
public class GetAuthCodeServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(GetAuthCodeServlet.class);
	private static final long serialVersionUID = 1L;
	public static final String type = "100";
	private static String redisServerUrl = "";
	private static int redisServerPort;
	private static int authCodeTimeOut;
	private static String authCodePrefix = "";
       
    public GetAuthCodeServlet() {
        super();
    }
    
    public void initMonitor(){
/*		Monitor m = Monitor.getInstance(ConfigUtil.MONITOR_PRODUCT_NAME);
		m.setXmppAddress(ConfigUtil.MONITOR_XMPP_ADDRESS);
		m.init();*/
	}
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	initMonitor();
    	redisServerUrl = config.getInitParameter("redisServerUrl");
    	redisServerPort = Integer.parseInt( config.getInitParameter("redisServerPort") );
    	authCodeTimeOut = Integer.parseInt( config.getInitParameter("authCodeTimeOut") );
    	authCodePrefix = config.getInitParameter("authCodePrefix");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();
        
        StringBuilder keyBuilder = new StringBuilder("");
        Map<String,Object> map = new HashMap<String, Object>();
        String mobile = request.getParameter("mobile");
        String ret ="",cnetPrefix="133,153,177,180,181,189,1700";
        JSONObject json = new JSONObject();
        JSONObject tempJson = new JSONObject();
        
        if(StringUtils.isBlank(mobile)){//check mobile
        	json.put("code", ResponseCode.ResponseCode_authCode_202.getCode());
        	json.put("message", ResponseCode.ResponseCode_authCode_202.getMessage());
        }else if( (cnetPrefix.indexOf(mobile.substring(0,3)) == -1) && (cnetPrefix.indexOf(mobile.substring(0,4)) == -1)){//check wether cNet mobile
        	json.put("code", ResponseCode.ResponseCode_authCode_203.getCode());
        	json.put("message", ResponseCode.ResponseCode_authCode_203.getMessage());
        }else{
        	if( !StringUtils.isBlank(request.getParameter("mobile")) ){
        		keyBuilder.append(authCodePrefix);
        		keyBuilder.append( request.getParameter("mobile") );
        		map.put("receiver", request.getParameter("mobile"));
        	}
        	
        	String authCode = AuthCodeUtil.generateAuthCode();
        	map.put("authCode", authCode);
        	
        	logger.debug("receiver: " + map.get("receiver"));
        	logger.debug("authCode: " + map.get("authCode"));
//        	Monitor.increase(ConfigUtil.GET_AUTH_CODE);
        	ret = SendSMSUtil.getInstance().sendSMS(map);
        	
        	tempJson = !StringUtils.isBlank(ret) ? JSONObject.parseObject(ret) : null;
        	if( !StringUtils.isBlank(ret) && tempJson.getString("code").trim().equals(ResponseCode.ResponseCode_sms_0.getCode()) ){//短信接口返回code=0表示短信发送指令送达到该接口
        		json.put("code", ResponseCode.ResponseCode_authCode_200.getCode());
            	json.put("message", ResponseCode.ResponseCode_authCode_200.getMessage());
            	
            	//存入验证码
            	if( RedisUtils.exisitKey(redisServerUrl, redisServerPort, keyBuilder.toString()) ){//如果存在key，则更新expire
            		RedisUtils.setAndExpireKey(redisServerUrl, redisServerPort, keyBuilder.toString(), authCode, authCodeTimeOut);
            	}else{//不存在则新增key并设置expire
            		RedisUtils.setAndExpireKey(redisServerUrl, redisServerPort, keyBuilder.toString(), authCode, authCodeTimeOut);
            	}
        	}else{//短信发送失败，reids里不存验证码
        		json.put("code", ResponseCode.ResponseCode_authCode_201.getCode());
            	json.put("message", ResponseCode.ResponseCode_authCode_201.getMessage());
        	}
        }
        
        out.write(JSON.toJSONString(json));
        out.flush();
        out.close();
	}

}
