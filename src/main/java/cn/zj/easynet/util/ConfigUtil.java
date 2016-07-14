package cn.zj.easynet.util;

import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.zj.easynet.mml.thread.CreateThread;

public class ConfigUtil {
	
	private static final Logger logger = Logger.getLogger(ConfigUtil.class);

	public static Integer THREAD_POOL_MAX_NUMBER = Integer.parseInt(Util
			.readString("mml", "thread_pool_max_number"));
	
	public static Integer THREAD_POOL_MIN_NUMBER = Integer.parseInt(Util
			.readString("mml", "thread_pool_min_number"));
	
	public static byte[] header = "`AL`".getBytes();
	public static Charset charset = Charset.forName("gbk");

	public static String Zte_IP = Util.readString("mml","Zte_IP");
	public static int Zte_Port = Integer.parseInt(Util.readString("mml","Zte_Port"));
	public static String Zte_User = Util.readString("mml","Zte_User");
	public static String Zte_Pswd = Util.readString("mml","Zte_Pswd");
	
	//redis
    public static Integer REDIS_TIMEOUT = 0;
	public static String REDIS_LIST = Util.readString("mml","redis_list");
	public static String REDIS_PORT = Util.readString("mml","redis_port");
	public static String CREATE_QUERY = Util.readString("mml","create_query");
	public static String PRIORITY_QUERY = "";
	public static String DEL_DISP_QUERY = "";
	public static String MOD_QUERY = "";
	public static String INSIDE_ERROR_QUERY =  "";
	public static String PUSH_FAIL_QUERY = "";
	
	//ddb of ecplive config info
	public static String M_HOST = Util.readString("mml","m_host");
	public static String M_USER = Util.readString("mml","m_user");
	public static String M_PASSWORD = Util.readString("mml","m_password");
	public static String M_KEYPATH = Util.readString("mml","m_keypath");
	public static String M_LOGDIR = Util.readString("mml","m_logdir");
	
	//Monitor监控
	public static String MONITOR_PRODUCT_NAME =  Util.readString("mml","monitor_product_name");
	public static String MONITOR_XMPP_ADDRESS = Util.readString("mml","monitor_xmpp_address");
	public static String GET_AUTH_CODE = Util.readString("mml","get_auth_code");
	public static String CHECK_AUTH_CODE = Util.readString("mml","check_auth_code");
	public static String CREATE_ECP_ACCOUNT = Util.readString("mml","create_ecp_account");
	
	//内部错误上线
	public static int INSIDE_ERROR_TIME = 0;
	
	//android/ios 升级信息
	public static String ANDROID_LASTVER=     Util.readString("mml","android_lastVer");
	public static String ANDROID_DOWNLOAD=    Util.readString("mml","android_download");
	public static String ANDROID_FORCE=       Util.readString("mml","android_force");
	public static String ANDROID_DESCRIPTION= Util.readString("mml","android_description");
	public static String ANDROID_LOWVER=      Util.readString("mml","android_lowVer");
	
	public static String ANDROID_NEW_LASTVER=     Util.readString("mml","android_new_lastVer");
	public static String ANDROID__NEW_DOWNLOAD=    Util.readString("mml","android_new_download");
	public static String ANDROID__NEW_FORCE=       Util.readString("mml","android_new_force");
	public static String ANDROID__NEW_DESCRIPTION= Util.readString("mml","android_new_description");
	public static String ANDROID__NEW_LOWVER=      Util.readString("mml","android_new_lowVer");

	public static String IOS_LASTVER=         Util.readString("mml","ios_lastVer");
	public static String IOS_LASTVER_ERROR=   Util.readString("mml","ios_lastVer_error");
	public static String IOS_DOWNLOAD=        Util.readString("mml","ios_download");
	public static String IOS_FORCE=           Util.readString("mml","ios_force");
	public static String IOS_DESCRIPTION=     Util.readString("mml","ios_description");
	public static String IOS_DESCRIPTION_ERROR=     Util.readString("mml","ios_description_error");
	public static String IOS_LOWVER=          Util.readString("mml","ios_lowVer");


	public static void reloadConfig() {
		Properties prop = new Properties();
		THREAD_POOL_MAX_NUMBER = Integer.parseInt(prop.get("thread_pool_max_number").toString());
		THREAD_POOL_MIN_NUMBER = Integer.parseInt(prop.get("thread_pool_min_number").toString());

		REDIS_TIMEOUT = Integer.parseInt(prop.get("redis_timeout").toString());
		REDIS_LIST = prop.get("redis_list").toString();

		logger.debug("加载配置文件：");
		logger.debug("最大线程总数：" + THREAD_POOL_MAX_NUMBER);
		logger.debug("最小线程总数：" + THREAD_POOL_MIN_NUMBER);
		logger.debug("redis超时时间EDIS_TIMEOUT：" + REDIS_TIMEOUT);
		logger.debug("redis服务器列表EDIS_LIST：" + REDIS_LIST);
	}

	public static void main(String[] args) {
		reloadConfig();
	}
}
