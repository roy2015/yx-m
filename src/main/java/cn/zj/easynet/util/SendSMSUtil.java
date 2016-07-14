package cn.zj.easynet.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.zj.easynet.device.http.HttpClientPool;




public class SendSMSUtil {
	private static Logger logger = Logger.getLogger(SendSMSUtil.class);
//	account password receiver message businessID time remark
	private static SendSMSUtil sendSMSUtil = null;
	private String smsUrl;
	private String account;
	private String password;
	private String businessID;
	private String smsTemplate;

	private SendSMSUtil(String smsUrl, String account, String password, String businessID, String smsTemplate) {
		this.smsUrl = smsUrl;
		this.account = account;
		this.password = password;
		this.businessID = businessID;
		this.smsTemplate = smsTemplate;
	}
	
	private SendSMSUtil(){}
	
	public static synchronized SendSMSUtil getInstance(){
		if(sendSMSUtil == null){
			Properties appPro = new Properties();
			try {
				appPro.load(SendSMSUtil.class.getClassLoader().getResourceAsStream("system-parameter.properties"));
				String smsUrl= appPro.getProperty("sms.url");
				String account = appPro.getProperty("sms.account");
				String password = appPro.getProperty("sms.password");
				String businessID = appPro.getProperty("sms.businessID");
				String smsTemplate = appPro.getProperty("sms.message.template");
				return new SendSMSUtil(smsUrl, account, password, businessID, smsTemplate);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				return new SendSMSUtil();
			}
		}else{
			return sendSMSUtil;
		}
	}
	
	public String sendSMS(Map<String, Object> map){
		map.put("account", this.account);
		map.put("password", this.password);
		map.put("businessID", "1000");
		map.put("time", "");
		map.put("remark", "");
		map.put("message", MessageFormat.format(this.smsTemplate, new Object[]{map.get("authCode")}));
		
		String ret ="";
		try {
			ret = HttpClientPool.getInstance().postMethod(this.smsUrl, map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return ret;
	}

	protected String getAccount() {
		return account;
	}

	protected void setAccount(String account) {
		this.account = account;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected String getBusinessID() {
		return businessID;
	}

	protected void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

}
