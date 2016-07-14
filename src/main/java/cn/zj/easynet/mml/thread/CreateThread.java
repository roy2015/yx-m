package cn.zj.easynet.mml.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.Set;

import javax.management.monitor.Monitor;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.zj.easynet.mml.mina.client.MinaClient;
import cn.zj.easynet.mml.mina.protocol.Answer;
import cn.zj.easynet.mml.mina.protocol.CreateUserBusiness;
import cn.zj.easynet.mml.mina.protocol.ZteBean;
import cn.zj.easynet.mml.pool.MMLZteTaskExecutor;
import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.RedisWrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/***
 * reso
 * @author Administrator
 * 
 * 此类必需改进，因为有死循环，这个影响主机CPU性能的
 *
 */
public class CreateThread implements Runnable{

	private MinaClient zClient = null;
	private RedisWrapper redisWrapper = null;
		
	private static final Logger logger = Logger.getLogger(CreateThread.class);
	public static final Logger logger_A3 = Logger.getLogger("A3");
		
	public CreateThread( MinaClient zClient){
		this.zClient = zClient;
	}
		
	private void _init() {
		MMLZteTaskExecutor.init(ConfigUtil.THREAD_POOL_MAX_NUMBER,ConfigUtil.THREAD_POOL_MIN_NUMBER, 3600, "MML_MAIN");
		logger.info("init MML_ZTE task thread pool, THREAD_POOL_MAX_NUMBER:"
				+ ConfigUtil.THREAD_POOL_MAX_NUMBER);
		logger.info("init MML_ZTE task thread pool, THREAD_POOL_MIN_NUMBER:"
				+ ConfigUtil.THREAD_POOL_MIN_NUMBER);
		redisWrapper =  RedisWrapper.getInstance();
	}
		
	public void run() {
		_init();
		while(true){
			try {
				for(Entry<String, String> entry :FrontBaseThread.taskMap.entrySet()){
					String req = entry.getValue();
					String serverId = entry.getKey();
					
					HttpServletResponse resp = FrontBaseThread.responseMap.get(serverId);
					
//					Monitor.increase("REDIS_RECORD_SEQ");
					MMLZteTaskExecutor.submit(sendCommend(req,serverId, resp));
					FrontBaseThread.taskMap.remove(serverId);
					
					TimeUnit.SECONDS.sleep(2);
				}	
			} catch (InterruptedException e) {
				//e.printStackTrace();
				logger_A3.error("Server_Error:"+ e.toString());
			}			
		}	
	}
	
	private Runnable sendCommend(final String req,final String serverId, final HttpServletResponse resp ){
		return new Runnable() {
			public void run() {
				try {
					JSONObject jsonReq = JSONObject.parseObject(req);
					ZteBean ZB = CreateUserBusiness.doZteCreate( jsonReq);
					ZteReturnToPre(ZB, req, serverId, resp, 1);
				} catch (Exception e) {
					//e.printStackTrace();
					logger_A3.error("服务错误：" + e.toString());
				}
			}

			
			
			private void ZteReturnToPre(ZteBean ZB, String req,String serverId, HttpServletResponse resp,int cmdtype) {
				ZB.setJsonOpinfo(req);
				Answer ack = zClient.send( serverId, ZB, cmdtype );
				if( null!=ack){
					ZB.setAnswer(ack);
					logger.debug("唤醒并清除相关Map开始:\t" + serverId);
					FrontBaseThread.timeoutMap.put(serverId, new Boolean(false));
					redisWrapper.hSet(ConfigUtil.CREATE_QUERY.getBytes(), serverId.getBytes(), JSON.toJSONString( ack ).getBytes());
					logger.debug("ACK:\t" + JSON.toJSONString( ack ));
					wakeup(resp);
					logger.debug("唤醒并清除相关Map结束:\t" +  serverId );
					return;
				}	
			}
			
			public void wakeup(Object obj) {
				Object sync = obj;
				if (sync == null)
					return;

				synchronized (sync) {
					sync.notify();
				}
				
			}
		};
	}

}

