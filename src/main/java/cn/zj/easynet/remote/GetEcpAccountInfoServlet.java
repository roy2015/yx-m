package cn.zj.easynet.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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
import cn.zj.easynet.remote.dto.EcpInfoReq;
import cn.zj.easynet.remote.dto.EcpInfoResp;
import cn.zj.easynet.util.AuthCodeUtil;
import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.EcpInfoUtil;
import cn.zj.easynet.util.EncryptUtil;
import cn.zj.easynet.util.RedisUtils;
import cn.zj.easynet.util.ResponseCode;
import cn.zj.easynet.util.SendSMSUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.om.Monitor;

/**
 * 发送短信验证码接口
 */
@WebServlet(name = "getEcpAccountInfoServlet", loadOnStartup = 7,  urlPatterns = { "/nnl/getEcpAccountInfo" }, asyncSupported = false)
public class GetEcpAccountInfoServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(GetEcpAccountInfoServlet.class);
	private static final long serialVersionUID = 1L;
       
    public GetEcpAccountInfoServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();
        
        //读body
        InputStream is = request.getInputStream();
        int buffSize = 1024;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream(buffSize);  
        byte[] temp = new byte[buffSize];  
        int size = 0;  
        while ((size = is.read(temp)) != -1) {  
        	bos.write(temp, 0, size);  
        }  
        is.close();  
        bos.close();  
        byte[] content = bos.toByteArray();  
        bos.close();  

        
		String s = new String(content,Charset.forName("UTF-8"));
        logger.debug("进入getEcpAccountInfoServlet=============================================");
        logger.debug("接收到数据(密文)："+ s);
        logger.debug("接收到数据(明文)："+ EncryptUtil.decryptFull(s,"UTF-8"));
        
        EcpInfoResp resp = new EcpInfoResp();
        EcpInfoReq req = JSON.parseObject(EncryptUtil.decryptFull(s,"UTF-8"), EcpInfoReq.class);
        
        if(StringUtils.isBlank(req.getEcpAccount())){
        	resp.setCode("500");
        	resp.setMsg("账号为空!");
        }else{
        	resp.setEcpAccount(req.getEcpAccount());
        	EcpInfoUtil.getEcpAccountInfo(req, resp);
        }
        
        out.write(JSON.toJSONString(resp));
        out.flush();
        out.close();
	}

}
