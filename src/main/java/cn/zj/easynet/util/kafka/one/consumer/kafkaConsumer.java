package cn.zj.easynet.util.kafka.one.consumer;
import java.io.IOException;
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Properties;  
  

import kafka.consumer.Consumer;  
import kafka.consumer.ConsumerConfig;  
import kafka.consumer.ConsumerIterator;  
import kafka.consumer.KafkaStream;  
import kafka.javaapi.consumer.ConsumerConnector;  
  
  
  
  
/** 
 * 接收数据 
 * 接收到: message: 10 
接收到: message: 11 
接收到: message: 12 
接收到: message: 13 
接收到: message: 14 
 * @author zm 
 * 
 */  
public class kafkaConsumer extends Thread{  
  
    private String topic;  
      
    public kafkaConsumer(String topic){  
        super();  
        this.topic = topic;  
    }  
      
      
    @Override  
    public void run() {  
        ConsumerConnector consumer = createConsumer();  
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();  
        topicCountMap.put(topic, 1); // 一次从主题中获取一个数据  
         Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);  
         KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据  
         ConsumerIterator<byte[], byte[]> iterator =  stream.iterator();  
         while(iterator.hasNext()){  
//             String message = new String(iterator.next().message());  
             String message;
            try {
                message = HessionCodecFactory.getInstance().deSerialize(iterator.next().message()).toString();
                System.out.println("接收到: " + message);  
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         }  
    }  
  
    private ConsumerConnector createConsumer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", "127.0.0.1:2181");//声明zk  
        properties.put("group.id", "test-consumer-group");// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据  
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));  
     }  
      
      
    public static void main(String[] args) {  
        new kafkaConsumer("test2").start();// 使用kafka集群中创建好的主题 test   
          
    }  
       
}  