package cn.zj.easynet.rabbitmq.simple;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
 






import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.SerializationUtils;
 
import org.springframework.amqp.core.ExchangeTypes;

import cn.zj.easynet.rabbitmq.complex.impl.EventMessage;
import cn.zj.easynet.rabbitmq.complex.impl.HessionCodecFactory;
import cn.zj.easynet.rabbitmq.complex.test.People;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
 
 
/**
 * 读取队列的程序端，实现了Runnable接口。
 * @author syntx
 *
 */
public class QueueConsumer extends EndPoint implements Runnable, Consumer{
     
    public QueueConsumer(String endPointName) throws IOException{
        super(endPointName);        
    }
     
    public void run() {
        try {
            //start consuming messages. Auto acknowledge messages.
//            channel.basicConsume(endPointName, false,this);
        	
        	channel.basicConsume(endPointName, true,this);
            /*GetResponse res=channel.basicGet(endPointName, false);
            
            if(res!=null){

                System.out.println(new String(res.getBody()));

                channel.basicAck(res.getEnvelope().getDeliveryTag(), false);
                
                EventMessage str = (EventMessage) SerializationUtils.deserialize(res.getBody());
            	HessionCodecFactory codecFactory = new HessionCodecFactory();
            	People people = (People)codecFactory.deSerialize(str.getEventData());
                System.out.println(people);

            }else{

                System.out.println("No message！");

            }*/

//            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Called when consumer is registered.
     */
    public void handleConsumeOk(String consumerTag) {
        System.out.println("Consumer "+consumerTag +" registered");     
    }
 
    /**
     * Called when new message is available.
     */
    public void handleDelivery(String consumerTag, Envelope env,
            BasicProperties props, byte[] body) throws IOException {
    	Map map = (HashMap)SerializationUtils.deserialize(body);
        System.out.println("Message Number "+ map.get("message number") + " received.");

    	/*try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	EventMessage str = (EventMessage) SerializationUtils.deserialize(body);
    	HessionCodecFactory codecFactory = new HessionCodecFactory();
    	channel.basicAck(env.getDeliveryTag(), false);
    	People people = (People)codecFactory.deSerialize(str.getEventData());
        System.out.println(people);*/
//        close();
         
    }
 
    public void handleCancel(String consumerTag) {}
    public void handleCancelOk(String consumerTag) {}
    public void handleRecoverOk(String consumerTag) {}
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}
}
