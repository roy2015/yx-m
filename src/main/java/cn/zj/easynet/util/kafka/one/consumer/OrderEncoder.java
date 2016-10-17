package cn.zj.easynet.util.kafka.one.consumer;

import java.io.IOException;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;


public class OrderEncoder implements Encoder<OrderDto> {
    
    private String encoding;
    
    public String getEncoding() {
        return encoding;
    }
    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public OrderEncoder(VerifiableProperties props) {
        encoding = props.getString("serializer.encoding", "UTF8");
    }

    @Override
    public byte[] toBytes(OrderDto arg0) {
        try {
           return HessionCodecFactory.getInstance().serialize(arg0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
