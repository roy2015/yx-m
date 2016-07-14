package cn.zj.easynet.rabbitmq.complex.impl;

import cn.zj.easynet.rabbitmq.complex.CodecFactory;
import cn.zj.easynet.rabbitmq.complex.EventTemplate;
import cn.zj.easynet.rabbitmq.complex.exception.SendRefuseException;
import cn.zj.easynet.rabbitmq.complex.util.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.IOException;

/**
 * User: poplar
 * Date: 14-7-4 下午5:59
 */
public class DefaultEventTemplate implements EventTemplate {

	private static final Logger logger = Logger.getLogger(DefaultEventTemplate.class);

	private AmqpTemplate eventAmqpTemplate;

	private CodecFactory defaultCodecFactory;

	private DefaultEventController eec;

	public DefaultEventTemplate(AmqpTemplate eopAmqpTemplate,
			CodecFactory defaultCodecFactory, DefaultEventController eec) {
		this.eventAmqpTemplate = eopAmqpTemplate;
		this.defaultCodecFactory = defaultCodecFactory;
		this.eec = eec;
	}
	
	public DefaultEventTemplate(AmqpTemplate eopAmqpTemplate,CodecFactory defaultCodecFactory) {
		this.eventAmqpTemplate = eopAmqpTemplate;
		this.defaultCodecFactory = defaultCodecFactory;
	}

	@Override
	public void send(String queueName, String exchangeName, Object eventContent)
			throws SendRefuseException {
		this.send(queueName, exchangeName, eventContent, defaultCodecFactory);
	}  

	@Override
	public void send(String queueName, String exchangeName, Object eventContent,
			CodecFactory codecFactory) throws SendRefuseException {
		if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(exchangeName)) {
			throw new SendRefuseException("queueName exchangeName can not be empty.");
		}
		
		if (!eec.beBinded(exchangeName, queueName))
			eec.declareBinding(exchangeName, queueName);

		byte[] eventContentBytes = null;
		if (codecFactory == null) {
			if (eventContent == null) {
				logger.warn("Find eventContent is null,are you sure...");
			} else {
				throw new SendRefuseException(
						"codecFactory must not be null ,unless eventContent is null");
			}
		} else {
			try {
				eventContentBytes = codecFactory.serialize(eventContent);
			} catch (IOException e) {
				throw new SendRefuseException(e);
			}
		}

		// 构造成Message
		EventMessage msg = new EventMessage(queueName, exchangeName,
				eventContentBytes);
		try {
			eventAmqpTemplate.convertAndSend(exchangeName, queueName, msg);
		} catch (AmqpException e) {
			logger.error("send event fail. Event Message : [" + eventContent + "]", e);
			throw new SendRefuseException("send event fail", e);
		}
	}

	public AmqpTemplate getEventAmqpTemplate() {
		return eventAmqpTemplate;
	}

	public void setEventAmqpTemplate(AmqpTemplate eventAmqpTemplate) {
		this.eventAmqpTemplate = eventAmqpTemplate;
	}
}
