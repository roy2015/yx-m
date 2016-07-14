package cn.zj.easynet.util.kafka.one.producer;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import cn.zj.easynet.util.kafka.one.consumer.OrderDto;
import cn.zj.easynet.util.kafka.one.consumer.OrderEncoder;
  
  
  
  
public class kafkaProducer extends Thread{  
  
    private String topic;  
      
    public kafkaProducer(String topic){  
        super();  
        this.topic = topic;  
    }  
      
      
    @Override  
    public void run() {  
//        Producer producer = createProducer();  
        Producer<String,OrderDto> producer = createProducer();  
        OrderDto dto = null;
        for (int i = 0; i < 5; i++) {
            dto = new OrderDto(java.util.UUID.randomUUID().toString(), "order-" + i, 
                               30.0 + i);
            
            producer.send(new KeyedMessage<String,OrderDto>(topic, 
//                    dto.getSerialNo(),
                    dto));
//            producer.send(new KeyedMessage<Integer, String>(topic, "roy1: " + i++));  
            try {  
                TimeUnit.MILLISECONDS.sleep(100);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private Producer createProducer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", "127.0.0.1:2181");//声明zk  
//        properties.put("serializer.class", StringEncoder.class.getName()); 
        properties.put("serializer.class", OrderEncoder.class.getName());
        properties.put("metadata.broker.list", "127.0.0.1:9092");// 声明kafka broker  
//        return new Producer<Integer, String>(new ProducerConfig(properties));
        return new Producer<String, OrderDto>(new ProducerConfig(properties));
     }  
      
      
    public static void main(String[] args) {  
        new kafkaProducer("test2").start();// 使用kafka集群中创建好的主题 test   
          
    }  
       
}  