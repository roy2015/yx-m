package cn.zj.easynet.activemq;

import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.activemq.ActiveMQConnection;


public class JMSConsumerTest {

	
	public static void main(String[] args) throws Exception {
		
		//**  JMSConsumer 可以设置成全局的静态变量，只需实例化一次即可使用,禁止循环重复实例化JMSConsumer(因为其内部存在一个线程池)

		JMSConsumer consumer = new JMSConsumer();
		consumer.setBrokerUrl("tcp://192.168.21.2:61616");
		consumer.setQueue("test");
		consumer.setUserName(ActiveMQConnection.DEFAULT_USER);
		consumer.setPassword(ActiveMQConnection.DEFAULT_PASSWORD);
		consumer.setQueuePrefetch(500);
		consumer.setMessageListener(new MultiThreadMessageListener(50,new MessageHandler() {
			public void handle(Message message) {
				try {
					System.out.println("name is " + ((MapMessage)message).getString("name"));
//					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
		consumer.start();
		
		Thread.sleep(1000 * 20);
		consumer.shutdown();
		
	}
	
	
}
