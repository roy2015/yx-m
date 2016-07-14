package cn.zj.easynet.activemq;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.ActiveMQConnection;


public class JMSProducerTest {

	
	public static void main(String[] args) {
		
		locationTest();
		System.out.println("over.");
	}
	
	private static void locationTest() {
		//**  JMSProducer 可以设置成全局的静态变量，只需实例化一次即可使用,禁止循环重复实例化JMSProducer(因为其内部存在一个线程池)
		
		//支持openwire协议的默认连接为 tcp://localhost:61616，支持 stomp协议的默认连接为tcp://localhost:61613。 
		//tcp和nio的区别
		//nio://localhost:61617 以及 tcp://localhost:61616均可在 activemq.xml配置文件中进行配置
		JMSProducer producer = new JMSProducer("nio://192.168.21.2:61616", ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "1");
		map.put("name", "sss1113333");
		map.put("password", "password");
		producer.send("test", map);
	}
	
}
