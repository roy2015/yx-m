package cn.zj.easynet.util.kafka.two.producer;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class MyKafkaProducer {

  private MyKafkaProducer() {

  }

  public static void main(String[] args) {
    args = new String[]{"10","test2"};
    long events = Long.parseLong(args[0]);
    String topic = args[1];
    Random rnd = new Random();

    Properties props = new Properties();
    props.put("metadata.broker.list", "127.0.0.1:9092");
    props.put("producer.type", "sync");
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    props.put("partitioner.class", "cn.zj.easynet.util.kafka.producer.MyKafkaPartitioner");
    props.put("request.required.acks", "1");

    ProducerConfig config = new ProducerConfig(props);

    Producer<String, String> producer = new Producer(config);

    for (long nEvents = 0; nEvents < events; nEvents++) {
      System.out.println("creating event " + nEvents);
      long runtime = new Date().getTime();
      String ip = "192.168.2." + rnd.nextInt(255);
      String msg = runtime + ",www.example.com," + ip;
      KeyedMessage<String, String> data = new KeyedMessage(topic, ip, msg);
      producer.send(data);
    }
    producer.close();
  }
}
