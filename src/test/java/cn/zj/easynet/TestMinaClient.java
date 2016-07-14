package cn.zj.easynet;

import org.apache.mina.core.RuntimeIoException;  
import org.apache.mina.core.future.ConnectFuture;  
import org.apache.mina.core.service.IoHandlerAdapter;  
import org.apache.mina.core.session.IdleStatus;  
import org.apache.mina.core.session.IoSession;  
import org.apache.mina.filter.codec.ProtocolCodecFilter;  
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;  
import org.apache.mina.transport.socket.SocketConnector;  
import org.apache.mina.transport.socket.nio.NioSocketConnector;  
  
import java.net.ConnectException;  
import java.net.InetSocketAddress;  
  
public class TestMinaClient {  
    public SocketConnector socketConnector;  
  
    public static final int DEFAULT_CONNECT_TIMEOUT = 5;  
  
    public static final String HOST = "127.0.0.1";  
  
    public static final int PORT = 1100;  
  
    public static IoSession ioSession;  
  
    public TestMinaClient() {  
        init();  
    }  
  
    public void init() {  
        socketConnector = new NioSocketConnector();  
  
        //   
        socketConnector.getSessionConfig().setKeepAlive(true);  
  
        socketConnector.setConnectTimeoutMillis(DEFAULT_CONNECT_TIMEOUT * 1000);  
  
        socketConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));  
  
        ClientIoHandler ioHandler = new ClientIoHandler();  
        socketConnector.setHandler(ioHandler);  
        InetSocketAddress addr = new InetSocketAddress(HOST, PORT);  
        ConnectFuture cf = socketConnector.connect(addr);  
        cf.awaitUninterruptibly();  
        ioSession = cf.getSession();  
    }  
  
    public void sendMessage(final String msg) {  
        try {  
        	
            if (ioSession != null) {  
            	System.out.println("ioSession.isConnected(): \t" + ioSession.isConnected());
                if (ioSession.isConnected()) {//  
                    ioSession.write(msg);//  
                    System.out.println("send message " + msg);  
                } else {  
                    reconnect();  
                }  
            }  
  
        } catch (RuntimeIoException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private void reconnect() {  
        InetSocketAddress addr = new InetSocketAddress(HOST, PORT);  
        ConnectFuture cf = socketConnector.connect(addr);  
        cf.awaitUninterruptibly();  
        ioSession = cf.getSession();  
    }  
  
    public static void main(String[] args) throws InterruptedException {  
        TestMinaClient clent = new TestMinaClient();  
        for (int i = 0; i < 99999; i++) {  
            Thread.sleep(2000);  
            System.err.println(i);  
            clent.sendMessage("Hello World " + i);  
        }  
        clent.getSocketConnector().dispose();  
  
    }  
  
    public SocketConnector getSocketConnector() {  
        return socketConnector;  
    }  
  
    public void setSocketConnector(SocketConnector socketConnector) {  
        this.socketConnector = socketConnector;  
    }  
  
}  
  
class ClientIoHandler extends IoHandlerAdapter {  
  
    private void releaseSession(IoSession session) throws Exception {  
        System.out.println("releaseSession");  
        if (session.isConnected()) {  
            session.close(true);  
        }  
    }  
  
    @Override  
    public void sessionOpened(IoSession session) throws Exception {  
        System.out.println("sessionOpened");  
    }  
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {  
        System.out.println("sessionClosed-c");  
    }  
  
    @Override  
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {  
        System.out.println("sessionIdle");  
        try {  
            releaseSession(session);  
        } catch (RuntimeIoException e) {  
        }  
    }  
  
    @Override  
    public void messageReceived(IoSession session, Object message) throws Exception {  
        System.out.println("Receive Server message " + message);  
  
    }  
  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {  
        System.out.println("exceptionCaught");  
        cause.printStackTrace();  
        releaseSession(session);  
    }  
  
    @Override  
    public void messageSent(IoSession session, Object message) throws Exception {  
        System.out.println("messageSent");  
        super.messageSent(session, message);  
    }  
  
}  

