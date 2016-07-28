package cn.zj.easynet.rabbitmq.complex.test;

import java.io.IOException;

import cn.zj.easynet.rabbitmq.complex.EventControlConfig;
import cn.zj.easynet.rabbitmq.complex.EventProcesser;
import cn.zj.easynet.rabbitmq.complex.EventTemplate;
import cn.zj.easynet.rabbitmq.complex.exception.SendRefuseException;
import cn.zj.easynet.rabbitmq.complex.impl.DefaultEventController;
import cn.zj.easynet.rabbitmq.complex.test.RabbitMqTest.ApiProcessEventProcessor;

public class RbmqUtil {
//	private String defaultHost = "192.168.21.3";
//	private String defaultHost = "172.23.4.65";
	private String defaultHost = "rabbitmq-test-master-1.800best.com";
	
//	private String defaultExchange = "EXCHANGE_DIRECT_TEST";
//	private String defaultExchange = "EXCHANGE_DIRECT_TEST_TEMP";
	private String defaultExchange = "EXCHANGE_DIRECT_TEST";
	
	
//	private String defaultQueue = "QUEUE_TEST";
//	private String defaultQueue = "QUEUE_TEST_TEMP";
	private String defaultQueue = "QUEUE_TEST";
	
	private DefaultEventController controller;
	
	private EventTemplate eventTemplate;
	
	private static RbmqUtil rbmqUtil;
	
	public synchronized static RbmqUtil getInstance(){
		if(rbmqUtil==null){
			rbmqUtil = new RbmqUtil();
			rbmqUtil.init();
		}
		return rbmqUtil;
	}
	
	private RbmqUtil(){}
	
	public void init(){
		EventControlConfig config = new EventControlConfig(defaultHost);
		controller = DefaultEventController.getInstance(config);
		eventTemplate = controller.getEopEventTemplate();
	}

	public void getMsg(EventProcesser processor) throws IOException{
		controller.add(defaultQueue, defaultExchange, processor);
		controller.start();
	}
	
	public void sendMsg(String msg) throws SendRefuseException {
		eventTemplate.send(defaultQueue, defaultExchange, msg);
	}
	
	public void sendMsg(Object obj) throws SendRefuseException {
		eventTemplate.send(defaultQueue, defaultExchange, obj);
	}

	public DefaultEventController getController() {
		return controller;
	}
	
}
