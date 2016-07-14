package cn.zj.easynet.mml.mina.protocol;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by Jadedrip on 2014/5/27.
 */
public class MmlCodecFactory implements ProtocolCodecFactory {
    ProtocolEncoder encoder=new MmlEncoder();
    ProtocolDecoder decoder=new MmlDecoder();

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
