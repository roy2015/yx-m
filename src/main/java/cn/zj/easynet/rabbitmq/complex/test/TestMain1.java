package cn.zj.easynet.rabbitmq.complex.test;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMain1 {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("rabbitmq-send.xml");  
		RabbitTemplate template = (RabbitTemplate) ctx.getBean("amqpTemplate");  
		template.convertAndSend("Hello, world1!"); 
		template.convertAndSend("Hello, world2!");
		template.convertAndSend("Hello, world3!");
		template.convertAndSend("Hello, world4!");
//		Thread.sleep(3000);  
//		ctx.destroy();  

	}

}
