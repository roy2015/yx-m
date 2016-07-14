package cn.zj.easynet.util.kafka.two.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerThread implements Runnable {
  private KafkaStream<byte[], byte[]> messageStream;
  private int threadNumber;

  public ConsumerThread(KafkaStream<byte[], byte[]> messageStream, int threadNumber) {
    this.threadNumber = threadNumber;
    this.messageStream = messageStream;
  }

  public void run() { // NOSONAR
    ConsumerIterator<byte[], byte[]> it = messageStream.iterator();
    while (it.hasNext()) {
      System.out.println("Thread " + threadNumber + ": " + new String(it.next().message())); // NOSONAR
    }
    System.out.println("Shutting down Thread: " + threadNumber); // NOSONAR
  }
}
