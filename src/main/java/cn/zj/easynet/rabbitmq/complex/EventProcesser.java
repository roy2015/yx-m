package cn.zj.easynet.rabbitmq.complex;

/**
 * User: poplar
 * Date: 14-7-4 下午5:59
 */
public interface EventProcesser {
	public void process(Object e);
}
