package cn.zj.easynet.mml.afterbusiness;

import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.zj.easynet.mml.mina.client.MinaClient;
import cn.zj.easynet.mml.mina.protocol.Answer;
import cn.zj.easynet.mml.mina.protocol.CreateUserBusiness;
import cn.zj.easynet.mml.mina.protocol.ZteBean;
import cn.zj.easynet.mml.pool.MMLZteTaskExecutor;
import cn.zj.easynet.mml.thread.CreateThread;
import cn.zj.easynet.mml.thread.FrontBaseThread;
import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.RedisWrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AfterBusinessResolver {
	private MinaClient zClient = null;
	private RedisWrapper redisWrapper = null;
		
	private static final Logger logger = Logger.getLogger(CreateThread.class);
	public static final Logger logger_A3 = Logger.getLogger("A3");
		
	public AfterBusinessResolver( MinaClient zClient){
		this.zClient = zClient;
	}
	
	public AfterBusinessResolver( MinaClient zClient, RedisWrapper redisWrapper){
		this.zClient = zClient;
		this.redisWrapper = redisWrapper;
	}
		
	public void resolveTask() {
			try {
				for(Entry<String, String> entry :FrontBaseThread.taskMap.entrySet()){
					String req = entry.getValue();
					String serverId = entry.getKey();
					
					HttpServletResponse resp = FrontBaseThread.responseMap.get(serverId);
					
//					Monitor.increase("REDIS_RECORD_SEQ");
					MMLZteTaskExecutor.submit(sendCommend(req,serverId, resp));
					FrontBaseThread.taskMap.remove(serverId);
				}	
			} catch (Exception e) {
				//e.printStackTrace();
				logger_A3.error("Server_Error:"+ e.toString());
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
