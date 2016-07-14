package cn.zj.easynet.mml.mina.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import cn.zj.easynet.mml.mina.protocol.Answer;
import cn.zj.easynet.mml.mina.protocol.MmlCodecFactory;
import cn.zj.easynet.mml.mina.protocol.ZteBean;
import cn.zj.easynet.mml.mina.protocol.ZteRequest;
import cn.zj.easynet.mml.thread.FrontBaseThread;
//import cn.zj.easynet.afterbusiness.DeleteUserBusiness;










import cn.zj.easynet.util.ConfigUtil;

//import cn.zj.easynet.util.RedisWrapper;
import org.apache.log4j.Logger;

public class MinaClient {
	
	private volatile static MinaClient CMPPCLIENT = null;
	private IoSession ioSession=null;
	private NioSocketConnector connector;
	private long timeoutMs = 1000 * 30;	
	private static final Logger logger = Logger.getLogger(MinaClient.class);
	
    ConnectFuture cf = null;
    boolean active = false;
    Hashtable<String, IoSession> notifyTable = new Hashtable<String, IoSession>();
    public Hashtable<Integer, IoSession> loginTable = new Hashtable<Integer, IoSession>();
    
//    RedisWrapper redisWrapper =  RedisWrapper.getInstance();
    
	/**
	 * 得到CmppClient连接实例, 单例模式
	 */
	public static MinaClient getInstance() {
		if (CMPPCLIENT == null) {
			synchronized (MinaClient.class) {
				if (CMPPCLIENT == null) {
					CMPPCLIENT = new MinaClient();
				}
			}
		}
		return CMPPCLIENT;
	}
	
	private Map<String, Object> _syncMap = new HashMap<String, Object>();
	
	public void waitNotify(String tid, Object sync) {
		try {
			logger.debug("waitNotify(): I ready to wait!");
			_syncMap.put(tid, sync);
			sync.wait(timeoutMs);
		} catch (InterruptedException e) {
			logger.error("waitNotify():\t" + e.getMessage());
		}
	}

	public void wakeup(String tid) {
		Object sync = _syncMap.get(tid);
		if (sync == null)
			return;

		synchronized (sync) {
			sync.notify();
		}
		_syncMap.remove(tid);
	}
	
	
	public void wakeup(Object obj) {
		Object sync = obj;
		if (sync == null)
			return;

		synchronized (sync) {
			sync.notify();
		}
		
	}
	
	//向socket中发送数据
	public Answer send(String serverId,ZteBean ZB,int cmdType) {
		if (!isConnected()) {
			connect();
		}
		Answer answer = null;
		String req = ZB.getZteOpinfo();
		Object sync = new Object();
		synchronized (sync) {
			logger.info("重新组装指令往MML送：" + req);
			//System.out.println(req);
			//修改单没有做任何修改的操作
			if(!req.contains("DONE")){
				String tid = serverId.substring(serverId.length()-8, serverId.length());
				logger.debug("send(): tid:\t" + tid);
				ZteRequest zteRequest = new ZteRequest(tid,req);
				if(null==ioSession){
//					Monitor.increase("CONNECT_TIMEOUT");
//					redisWrapper.hashSetString(ConfigUtil.INSIDE_ERROR_QUERY, serverId, ZB.getJsonOpinfo());
					logger.info("serverId="+serverId+"连接超时，需要重新处理");
					return answer;
				}
				ioSession.write(zteRequest);
				waitNotify(tid, sync);
				answer = (Answer) ioSession.getAttribute(tid);
			}else{
				answer = new Answer(req.split(":")[1].trim(),serverId);
				answer.setCode("0000");
				answer.setDescription("成功");
			}	
			//这里要处理中兴内部出错，0011，0014，0039，0044，这4个错误
			/*if(null==answer){
//				Monitor.increase("CONNECT_TIMEOUT");
				redisWrapper.hashSetString(ConfigUtil.INSIDE_ERROR_QUERY, serverId, ZB.getJsonOpinfo());
				logger.info("serverId="+serverId+"连接超时，需要重新处理");
				return answer;
			}*/
			if(!(answer.getCode().equals("0011")
                ||answer.getCode().equals("0014")
                ||answer.getCode().equals("0039")
                ||answer.getCode().equals("0044"))){
				/*if(cmdType==2){
					answer = DeleteUserBusiness.judgereturnMessage(answer, req);
				}*/
				//查询指令逻辑修改
				if(cmdType==4&&answer.getCode().equals("0000")){
					answer.setCode("0100");
					answer.setDescription("查询成功");
				}
			}else{
				//记录中兴内部错误数
//				Monitor.increase("COUNT_INSIDE_ERROR");
//				redisWrapper.hashSetString(ConfigUtil.INSIDE_ERROR_QUERY, serverId, ZB.getJsonOpinfo());
				logger.info("serverId="+serverId+" 发生内部错误，需要重新处理");
				answer = null;
			}
        }
		//修改单没有做任何修改的操作
		return answer;
    }
	
	//判断连接是否还存�?
	private boolean isConnected() {
		boolean isConnected = (ioSession != null) && (ioSession.isConnected());
		logger.info("ioSession.isConnected:\t" + isConnected);
		logger.debug("ConfigUtil.Zte_IP:\t" + ConfigUtil.Zte_IP);
		logger.debug("ConfigUtil.Zte_Port:\t" + ConfigUtil.Zte_Port);
		return isConnected;
	}
	
	//初始化连接
	public void initMinaClient() {
		//保存数据库操作句
		connect();
	}
	
	private void connect() {
		if (isConnected()) {
			return;
			}
		//重新连接
		_init();
		startup();
		
		logger.info("client Zte connector");
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			//e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	//初始化mina连接配置：数据过滤器
	private void _init() {
		if (connector == null) {
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(timeoutMs);
			connector.getFilterChain().addLast("codec",
					(IoFilter) new ProtocolCodecFilter(new MmlCodecFactory()));
			connector.getFilterChain().addLast("log", new LoggingFilter());
			connector.setHandler(new MinaClientSessionHandler(this));
		}
	}
	
	public void startup() {
		try {
			ioSession = null;
			ConnectFuture cf = connector.connect(new InetSocketAddress(ConfigUtil.Zte_IP, ConfigUtil.Zte_Port));
			
			cf.awaitUninterruptibly(timeoutMs);
			ioSession = cf.getSession();
			
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	public static void main(String[] args) {
		
		MinaClient.getInstance().initMinaClient();
	}

}