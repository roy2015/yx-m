package cn.zj.easynet.rabbitmq.foo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class Foo implements  MessageListener{
	@Override
	public void onMessage(Message message) {
		System.out.println(" data :" + message.getBody());
	}
}
