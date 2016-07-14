package cn.zj.easynet.remote;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.zj.easynet.mml.mina.client.MinaClient;
import cn.zj.easynet.mml.pool.MMLZteTaskExecutor;
import cn.zj.easynet.mml.pool.TaskExecutor;
import cn.zj.easynet.mml.thread.CreateThread;
import cn.zj.easynet.mml.thread.FrontBaseThread;
import cn.zj.easynet.util.AreaCodeUtil;
import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.RedisUtils;
import cn.zj.easynet.util.RedisWrapper;
import cn.zj.easynet.util.ResponseCode;
import cn.zj.easynet.util.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.netease.om.Monitor;

/**
 * Servlet implementation class CheckAuthCodeServlet
 */
@WebServlet(name = "checkAuthCodeServlet", loadOnStartup = 3,  urlPatterns = { "/nnl/checkAuthCodeAndAccount" }, asyncSupported = false)
public class CheckAuthCodeServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(CheckAuthCodeServlet.class);
	public static final Logger logger_A1 = Logger.getLogger("A1");
	public static final Logger logger_A3 = Logger.getLogger("A3");
	
	public static Long tid = new Long(0);
	
	private static final long serialVersionUID = 1L;
	public static String accountUrl ="";
	public static String type = "101";
	private static String zxAccountHttpUrl="";
	private static String areaCodehttpUrl="";
	private static String redisServerUrl = "";
	private static int redisServerPort;
	private static String authCodePrefix = "";
	
	private RedisWrapper redisWrapper = null;
	private MinaClient client = null;
	
    public static String getRedisServerUrl() {
		return redisServerUrl;
	}

	public static void setRedisServerUrl(String redisServerUrl) {
		CheckAuthCodeServlet.redisServerUrl = redisServerUrl;
	}

	public static int getRedisServerPort() {
		return redisServerPort;
	}

	public static void setRedisServerPort(int redisServerPort) {
		CheckAuthCodeServlet.redisServerPort = redisServerPort;
	}

	public CheckAuthCodeServlet() {
        super();
    }
	
	public void initMonitor(){
	/*	Monitor m = Monitor.getInstance(ConfigUtil.MONITOR_PRODUCT_NAME);
		m.setXmppAddress(ConfigUtil.MONITOR_XMPP_ADDRESS);
		m.init();*/
	}
	
	public void initThread(){
		// 初始化两个线程池
		logger.info("init create task thread pool, THREAD_POOL_MAX_NUMBER:"
				+ ConfigUtil.THREAD_POOL_MAX_NUMBER);
		logger.info("init create task thread pool, THREAD_POOL_MIN_NUMBER:"
				+ ConfigUtil.THREAD_POOL_MIN_NUMBER);
		TaskExecutor.init(ConfigUtil.THREAD_POOL_MIN_NUMBER,
				ConfigUtil.THREAD_POOL_MAX_NUMBER, 30, "QUERY_MML");
		
		MMLZteTaskExecutor.init(ConfigUtil.THREAD_POOL_MIN_NUMBER,ConfigUtil.THREAD_POOL_MAX_NUMBER, 30, "MML_MAIN");
		logger.info("init MML_ZTE task thread pool, THREAD_POOL_MAX_NUMBER:"
				+ ConfigUtil.THREAD_POOL_MAX_NUMBER);
		logger.info("init MML_ZTE task thread pool, THREAD_POOL_MIN_NUMBER:"
				+ ConfigUtil.THREAD_POOL_MIN_NUMBER);
	}

    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	zxAccountHttpUrl= config.getInitParameter("zxAccountHttpUrl");
    	areaCodehttpUrl= config.getInitParameter("areaCodehttpUrl");
    	redisServerUrl = config.getInitParameter("redisServerUrl");
//    	accountCallBackUrl= config.getInitParameter("accountCallBackUrl");
    	redisServerPort = Integer.parseInt( config.getInitParameter("redisServerPort") );
    	authCodePrefix = config.getInitParameter("authCodePrefix");
    	
    	redisWrapper = RedisWrapper.getInstance();
    	client = MinaClient.getInstance();
		client.initMinaClient();//初始化MML scoket连接
    	initThread();
//    	initMonitor();//初始化监控
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();
//        Long currentTime = System.currentTimeMillis();
        String receiver = "";
        JSONObject json = new JSONObject();
        JSONObject tempJson = new JSONObject();
        JSONObject tempJsonTwo = new JSONObject();
        String authCode ="";
        String password = "";
        String areaCode= "";
        
        if( StringUtils.isBlank(request.getParameter("mobile")) ){//手机号判空
        	json.put("code", ResponseCode.ResponseCode_account_E0001.getCode());
        	json.put("message", ResponseCode.ResponseCode_account_E0001.getMessage());
        	logger.debug(json.toJSONString());
        }else if( StringUtils.isBlank(request.getParameter("authCode")) ){//验证码判空
        	json.put("code", ResponseCode.ResponseCode_account_E0002.getCode());
        	json.put("message", ResponseCode.ResponseCode_account_E0002.getMessage());
        	logger.debug(json.toJSONString());
        }else{
        	receiver = request.getParameter("mobile");
        	authCode = request.getParameter("authCode");
        	password = request.getParameter("password");
        	logger.debug(receiver);
        	logger.debug(authCode);
        	logger.debug(password);
        	
        	if(!RedisUtils.exisitKey(redisServerUrl, redisServerPort,  authCodePrefix + receiver)){//验证码不存在或已超时被redis delete
        		json.put("code", ResponseCode.ResponseCode_account_E0004.getCode());
            	json.put("message", ResponseCode.ResponseCode_account_E0004.getMessage());
        	}else{
        		if(!RedisUtils.getKey(redisServerUrl, redisServerPort, authCodePrefix + receiver).equalsIgnoreCase( authCode )){//验证码不正确
        			json.put("code", ResponseCode.ResponseCode_account_E0003.getCode());
                	json.put("message", ResponseCode.ResponseCode_account_E0003.getMessage());
        		}else if( StringUtils.isBlank(request.getParameter("password")) ){//开户密码判空
                	json.put("code", ResponseCode.ResponseCode_account_E0005.getCode());
                	json.put("message", ResponseCode.ResponseCode_account_E0005.getMessage());
                	logger.debug(json.toJSONString());
                }else if( StringUtils.isBlank(request.getParameter("password")) ){//开户密码判空
                	json.put("code", ResponseCode.ResponseCode_account_E0005.getCode());
                	json.put("message", ResponseCode.ResponseCode_account_E0005.getMessage());
                	logger.debug(json.toJSONString());
                }else if( StringUtils.isBlank(request.getParameter("password")) ){//开户密码判空
                	json.put("code", ResponseCode.ResponseCode_account_E0005.getCode());
                	json.put("message", ResponseCode.ResponseCode_account_E0005.getMessage());
                	logger.debug(json.toJSONString());
                }else{//验证码正确必填字段都有
                	logger.debug("mobile:\t" + receiver );
                	logger.debug("password:\t" + request.getParameter("password") );
//                	Monitor.increase(ConfigUtil.CHECK_AUTH_CODE);
        	
            		//开户 调MML接口
                	try{
                		String serverId = ""; 
                		synchronized (tid) {
                			tid++;
                			if(tid>99999999){
                				tid = 1L;
                			}
                			serverId = Utils.QueryId(tid);
						}
                		
                		areaCode = AreaCodeUtil.getAreaCode4PhoneNumber(areaCodehttpUrl, receiver);
                		logger.debug("areaCode:\t" + areaCode);
                		tempJsonTwo = Utils.getCmpIdAndVersion4AreaCode(areaCode);
                		logger.debug("cmpyId:\t" + tempJsonTwo.getString("cmpyId").trim());
                		logger.debug("version:\t" + tempJsonTwo.getString("version").trim());
                		
                		//开始提交线程
            			TaskExecutor.submit(new FrontBaseThread(client, redisWrapper, serverId, response, receiver, password, 
            					tempJsonTwo.getString("cmpyId").trim(), tempJsonTwo.getString("version").trim()));
            			
            			synchronized (response) {
                    		response.wait(1000 * 60 * 3);
                        }
            			
            			logger.debug("doPost(): I'm awake completely!");
    					FrontBaseThread.responseMap.remove(serverId);
    					FrontBaseThread.taskMap.remove(serverId);
    					if( FrontBaseThread.timeoutMap.get(serverId) ){//超时
    						json.put("code", ResponseCode.ResponseCode_common_E505.getCode());
    						json.put("message", ResponseCode.ResponseCode_common_E505.getMessage());
    						logger.debug("doPost():\t" + JSON.toJSONString(json));
    					}else{//非超时，而是被唤醒的，remove掉相关的key-value
    						FrontBaseThread.responseMap.remove(serverId);
    						FrontBaseThread.taskMap.remove(serverId);
    						FrontBaseThread.timeoutMap.remove(serverId);
    						
    						String str = new String(redisWrapper.hGet(ConfigUtil.CREATE_QUERY.getBytes(), serverId.getBytes())) ;
    						redisWrapper.hDel(ConfigUtil.CREATE_QUERY.getBytes(), serverId.getBytes());
    						tempJson = JSONObject.parseObject(str);
    						
    						if(tempJson.getString("code").trim().equals(ResponseCode.ResponseCode_account_6000.getCode())){
    							json.put("code", ResponseCode.ResponseCode_account_6000.getCode());
        						json.put("message", ResponseCode.ResponseCode_account_6000.getMessage());
    						}else{
    							logger.debug("++++++");
    							json.put("code", tempJson.getString("code"));
        						json.put("message", tempJson.getString("description"));
    						}
    						logger.debug("doPost():\t" + str);
//    						Monitor.increase(ConfigUtil.CREATE_ECP_ACCOUNT);
    					}
            			
                	}catch(Exception e){
                		e.printStackTrace();
                		json.put("code", ResponseCode.ResponseCode_account_0008.getCode());
                    	json.put("message", ResponseCode.ResponseCode_account_0008.getMessage());
                	}
        		}
        	}
        }
        
        out.write(json.toJSONString());
        out.flush();
        out.close();
	}

	public String getZxAccountHttpUrl() {
		return zxAccountHttpUrl;
	}


	public String getAreaCodehttpUrl() {
		return areaCodehttpUrl;
	}

}
