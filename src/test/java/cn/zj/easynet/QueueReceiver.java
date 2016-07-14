package cn.zj.easynet;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
 
/**
 * <b>function:</b> ��Ϣ�����ߣ� ����hawtbuf-1.9.jar
 * @author Josh, Wang(Sheng)
 * @file MessageSender.java
 * @package ccom.wsheng.mq.queue
 * @project ActiveMQ-5.8
 * @email josh_wang23@hotmail.com
 * @version 1.0
 */
public class QueueReceiver {
 
    // tcp ��ַ
    public static final String BROKER_URL = "tcp://192.168.21.2:61616";
    // Ŀ�꣬��ActiveMQ����Ա����̨���� http://localhost:8161/admin/queues.jsp
    public static final String TARGET = "test.queue";
    
    
    public static void run() throws Exception {
        
        QueueConnection connection = null;
        QueueSession session = null;
        try {
            // �������ӹ���
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
            // ͨ�����һ������
            connection = factory.createQueueConnection();
            // ��������
            connection.start();
            // ����һ��session�Ự
            session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // ����һ����Ϣ����
            Queue queue = session.createQueue(TARGET);
            // ������Ϣ������
            javax.jms.QueueReceiver receiver = session.createReceiver(queue);
            
            receiver.setMessageListener(new MessageListener() { 
                public void onMessage(Message msg) { 
                    if (msg != null) {
                        MapMessage map = (MapMessage) msg;
                        try {
                            System.out.println(map.getLong("time") + "#" + map.getString("text"));
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                } 
            }); 
            // ����100ms�ٹر�
            Thread.sleep(1000 * 15); 
            
            // �ύ�Ự
            session.commit();
            
        } catch (Exception e) {
            throw e;
        } finally {
            // �ر��ͷ���Դ
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        QueueReceiver.run();
    }
}
