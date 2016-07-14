package cn.zj.easynet.mml.mina.client;



import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import cn.zj.easynet.mml.mina.protocol.Answer;
import cn.zj.easynet.mml.mina.protocol.ZteRequest;
import cn.zj.easynet.util.ConfigUtil;

public class MinaClientSessionHandler extends IoHandlerAdapter{
	private static final Logger logger = Logger.getLogger(MinaClientSessionHandler.class);
	private MinaClient client;

	public MinaClientSessionHandler(MinaClient client) {
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionOpened(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("sessionOpened");
		//组成login命令
		String content = "LOGIN:USER="+ConfigUtil.Zte_User+",PSWD="+ConfigUtil.Zte_Pswd;
//        long l = System.currentTimeMillis();
        String id = "00000001";
        if (id.length() > 8) id = id.substring(0, 8);

        session.write(new ZteRequest(id, content));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.debug("sessionClosed");
		synchronized (this) {
			client.cf = null;
			client.active = false;
        }
		super.sessionClosed(session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionIdle(org.apache.
	 * mina.core.session.IoSession, org.apache.mina.core.session.IdleStatus)
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status){
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache
	 * .mina.core.session.IoSession, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// super.exceptionCaught(session, cause);
		// session.close(true);
		logger.error("exceptionCaught", cause);
	}
	
	public void releaseSession(IoSession session) throws Exception {  
		logger.debug("releaseSession");  
        if (session.isConnected()) {  
            session.close(true);  
        }  
    } 

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache
	 * .mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		super.messageReceived(session, message);
		
//		Gson gson = new Gson();
//        logger.info("MML 接收：" + gson.toJson(message));
        
		if (message instanceof Answer) {
			Answer answer = (Answer) message;
			String tid = answer.getId();
			logger.debug("messageReceived(): \tmessageReceived=" +answer.toString() + ", tid=" + tid);
			if( "GENERAL ERROR".equals(answer.getCommand()) ){
	            logger.error("连接发生通用错误!");
	            session.close(false);
	            if(!client.active){
	                synchronized (MinaClientSessionHandler.this) {
	                	client.active = true;
	                	MinaClientSessionHandler.this.notifyAll();
	                }
	            }
	            client.active=false;
	        }
			if (!client.active) {
                if ("LOGIN".equals(answer.getCommand())) {
                    if ("0000".equals(answer.getCode())) {
                        logger.info("MML 登录成功.");
                        synchronized (MinaClientSessionHandler.this) {
                        	client.active = true;
                        	MinaClientSessionHandler.this.notifyAll();
                        }
                        return;
                    }

                    logger.error("MML 登录失败：" + answer.getCode());
                } else {
                    logger.error("MML 返回异常.");
                }
                synchronized (MinaClientSessionHandler.this) {
                	client.cf = null;
                	MinaClientSessionHandler.this.notifyAll();
                }
                session.close(false);
            } else {
            	logger.debug("messageReceived(): tid:\t" + tid);
            	session.setAttribute(tid, answer);
    			client.wakeup(tid);
	        } 			
		} else {
			logger.warn("Unexpected message[" + message.getClass().getName() + "]");
		}
	}
}

