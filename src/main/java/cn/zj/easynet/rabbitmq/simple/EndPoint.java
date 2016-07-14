package cn.zj.easynet.rabbitmq.simple;
 
import java.io.IOException;
 

import org.springframework.amqp.core.ExchangeTypes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
 
/**
 * Represents a connection with a queue
 * @author syntx
 *
 */
public abstract class EndPoint{
     
    protected Channel channel;
    protected Connection connection;
    protected String endPointName;
     
    public EndPoint(String endpointName) throws IOException{
         this.endPointName = endpointName;
         
         //Create a connection factory
         ConnectionFactory factory = new ConnectionFactory();
         
         //hostname of your rabbitmq server
         factory.setHost("192.168.21.3");
//         factory.setHost("172.23.4.65");
         factory.setPort(5672);
         
         //getting a connection
         connection = factory.newConnection();
         
         //creating a channel
         channel = connection.createChannel();
         
         //declaring a queue for this channel. If queue does not exist,
         //it will be created on the server.
         
//         channel.exchangeDeclare("EXCHANGE_DIRECT_TEST_TEMP", ExchangeTypes.DIRECT,true);
//         com.rabbitmq.client.AMQP.Queue.DeclareOk declareQueue = channel.queueDeclare(endpointName, true, false, false, null);
         com.rabbitmq.client.AMQP.Queue.DeclareOk declareQueue = channel.queueDeclare(endpointName, false, false, false, null);
         /*System.out.println( declareQueue.getMessageCount()  );
         if(declareQueue.getMessageCount() == 0){
        	 close();
         }*/
        
    }
     
     
    /**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。 
     * @throws IOException
     */
     public void close() throws IOException{
         this.channel.close();
         this.connection.close();
     }
}
