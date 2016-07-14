package cn.zj.easynet.mml.thread;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.zj.easynet.mml.afterbusiness.AfterBusinessResolver;
import cn.zj.easynet.mml.mina.client.MinaClient;
import cn.zj.easynet.remote.dto.ZxRequestEntryDto;
import cn.zj.easynet.remote.request.ZxAccountReq;
import cn.zj.easynet.util.DateUtil;
import cn.zj.easynet.util.IConstants;
import cn.zj.easynet.util.RedisWrapper;
import cn.zj.easynet.util.Util;
import cn.zj.easynet.util.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FrontBaseThread implements Runnable{	
	public static final Logger logger = Logger.getLogger(FrontBaseThread.class);
	
	//repsone、timeout、task(response的map、超时标志的map，本身命令的map)
	public static ConcurrentHashMap<String, HttpServletResponse> responseMap = new ConcurrentHashMap<String, HttpServletResponse>();
	public static ConcurrentHashMap<String, Boolean> timeoutMap = new ConcurrentHashMap<String, Boolean>();
	public static ConcurrentHashMap<String, String> taskMap = new ConcurrentHashMap<String, String>();	
	
	private String serverId;
	private String phoneNbr;
	private String password;
	private String version;
	private String cmpyId;
	private HttpServletResponse resp;
	
	private RedisWrapper redisWrapper = null;
	private MinaClient client = null;
	
	public FrontBaseThread(String serverId, HttpServletResponse resp, String phoneNbr, String password){
		this.serverId = serverId;
		this.resp = resp;
		this.phoneNbr = phoneNbr;
		this.password = password;
	}
	
	public FrontBaseThread(MinaClient client, RedisWrapper redisWrapper, String serverId, HttpServletResponse resp, String phoneNbr, String password){
		this.client = client;
		this.redisWrapper = redisWrapper;
		this.serverId = serverId;
		this.resp = resp;
		this.phoneNbr = phoneNbr;
		this.password = password;
	}
	
	public FrontBaseThread(MinaClient client, RedisWrapper redisWrapper, String serverId, HttpServletResponse resp,
			String phoneNbr, String password, String cmpyId, String version){
		this.client = client;
		this.redisWrapper = redisWrapper;
		this.serverId = serverId;
		this.resp = resp;
		this.phoneNbr = phoneNbr;
		this.password = password;
		this.cmpyId = cmpyId;
		this.version = version;
	}

	public void run() {
		//给两个同步的静态的map赋值
		responseMap.put( serverId , resp);
		timeoutMap.put( serverId, new Boolean(true) );
		taskMap.put( serverId, Utils.assembleJsonStr4MMl(phoneNbr, password, cmpyId, version));
		
		new AfterBusinessResolver(client, redisWrapper).resolveTask();
	}

	public String getPhoneNbr() {
		return phoneNbr;
	}

	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCmpyId() {
		return cmpyId;
	}

	public void setCmpyId(String cmpyId) {
		this.cmpyId = cmpyId;
	}
	
	
	
}
