package cn.zj.easynet.util.kafka.two.producer;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class MyKafkaPartitioner implements Partitioner {

  public MyKafkaPartitioner(VerifiableProperties props) {

  }

  public int partition(Object key, int numberOfPartitions) {
    int partition = 0;
    String stringKey = (String) key;
    int offset = stringKey.lastIndexOf('.');
    if (offset > 0) {
      partition = Integer.parseInt(stringKey.substring(offset + 1)) % numberOfPartitions;
    }
    return partition;
  }
}
