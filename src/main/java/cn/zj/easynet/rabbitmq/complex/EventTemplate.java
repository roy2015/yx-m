package cn.zj.easynet.rabbitmq.complex;

import cn.zj.easynet.rabbitmq.complex.exception.SendRefuseException;

/**
 * User: poplar
 * Date: 14-7-4 下午5:59
 */
public interface EventTemplate {

	void send(String queueName, String exchangeName, Object eventContent) throws SendRefuseException;
		
	void send(String queueName, String exchangeName, Object eventContent, CodecFactory codecFactory) throws SendRefuseException;
}
