package cn.zj.easynet.mml.mina.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import cn.zj.easynet.util.ConfigUtil;
import cn.zj.easynet.util.DateUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Jadedrip on 2014/5/27.
 */
@SuppressWarnings("unused")
public class MmlEncoder extends ProtocolEncoderAdapter {
    private static final Logger logger = Logger.getLogger(MmlEncoder.class);

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if (message instanceof ZteRequest) {
        	logger.debug("encode(): ZteRequest.getId():\t" + ((ZteRequest) message).getId());
        	ZteRequest cmd = (ZteRequest) message;
            //String content = Utilities.toMmlBody(cmd.getEntry());
        	String content = cmd.getReq();
            
            logger.info("发送："+ content);
            byte[] bytes = content.getBytes(ConfigUtil.charset);
            

            int i = bytes.length % 4;

            byte[] v;
            int length = bytes.length + 56 + (i==0?0:4-i);
            IoBuffer headBuffer = IoBuffer.allocate(length + 16);
            /*
             * 消息开始标志(4byte)
             消息开始标志用于确定消息的开始，长度为4字节。编码为`SC`。
         */
            assert ConfigUtil.header.length == 4;
            headBuffer.put(ConfigUtil.header);

            /*
             * 消息长度(4byte)
             消息长度用来指出消息头到操作信息的总长度，长度4个字节。长度值用16进制字符（0-F）表示的4位整数来表示。消息长度取值范围为0到65000（0000-FFFF）。
          */
            String s = Integer.toHexString(length);
            if (s.length() < 4) {
                s = "0000".substring(0, 4 - s.length()) + s;
            } else if (s.length() > 4) {
                throw new RuntimeException("内容过长.");
            }
            headBuffer.put(s.getBytes());

            CheckSummer checkSummer = new CheckSummer();
            /*
             * 消息头（20byte）
             消息头包含如下参数：
             版本号：用来识别接口协议的版本，长度为4个字节。目前编码为1.00
             服务名：用于标示处理的服务，长度为4个字节，编码为ECP。
             渠道标示：用于识别接入渠道编码，长度为12个字节。（不同的代理商渠道标识不同，新的代理商加入时，提供进行唯一标识的渠道标识）
          */
//            v = "1.0000001111PPS     ".getBytes();
            v = "1.00 ECPCY01".getBytes();
            headBuffer.put(v);
            checkSummer.update(v);
//            assert Config.channelId.length() == 12;
//
//            v = Config.channelId.getBytes();
//
//            headBuffer.put(v);
//            checkSummer.update(v);

            /*
             * 会话头(18byte)
             会话是ECP营运中心接入系统与第三方系统之间的一个虚拟连接（逻辑连接），唯一标示的一次有效登录。会话头包含如下部分：
             会话ID：16进制字符（0-F）标示的32位整数，长度8个字节，从1开始，在客户端向服务端登录时由服务端统一分配。
             会话控制字：10个字节，编码如下：
             DlgLgn：请求登录
             DlgCon：会话保持
             DlgEnd：会话结束
          */
            byte[] dlgID = (byte[]) session.getAttribute("dlgID");
            if (dlgID == null || dlgID.length != 8) {
                /*long l = System.currentTimeMillis() * 138;
                String id = Long.toHexString(l);
                if (id.length() > 8) id = id.substring(0, 8);

                dlgID = id.getBytes();
                headBuffer.put(dlgID);
                checkSummer.update(dlgID);*/
                v = "        DLGLGN  ".getBytes();
            } else {
            	logger.debug("encode():dlgID:\t*" + new String(dlgID) + "*");
                headBuffer.put(dlgID);
                checkSummer.update(dlgID);
                v = "DLGLGN  ".getBytes();
            }

            headBuffer.put(v);
            checkSummer.update(v);

            /*
             * 事务头(18byte)
             事务是接入ECP营运中心系统与第三方营运系统的一次交易，一次交易可以由一到多个操作信息组成（可以在一到多条消息中），
             交易的结果页可以通过一个到多个操作信息ACK返回。事务头包含如下部分：
             事务ID：16进制字符（0-F）表示的32位整数，长度8个字节，从1开始。
             事务控制字：10个字节长度，编码如下：
             TxBeg：开始事务
             TxCon：事务继续
             TxEnd：事物结束

             @ 注意：由于我们没用到事务处理，因此这里写死之
          */
            byte[] txKey = cmd.getId().getBytes();
            //if (txKey.length != 8) throw new RuntimeException("ID 长度错误");
            headBuffer.put(txKey);
            checkSummer.update(txKey);
            
            v = "TXBEG ".getBytes();
            headBuffer.put(v);
            checkSummer.update(v);
            
            //
            byte[] dateStr = DateUtil.getGeneralFormat().format(new Date()).getBytes();
            headBuffer.put(dateStr);           
            checkSummer.update(dateStr);
            
            /*
             * 操作信息
             操作信息涉及到具体的业务操作，其内容可以自由定义，目前为了方便定义采用命令码+操作参数块组成的形式，两者之间用冒号“：”分割开来。如果操作信息长度不是4的倍数，则在后面填充空格补齐。
          */
            headBuffer.put(bytes);           
            checkSummer.update(bytes);

            if (i > 0) { // 如果长度不是4的倍数，后面用空格填充
                String x = "    ".substring(i);
                byte[] b = x.getBytes();
                headBuffer.put(b);
                checkSummer.update(b);
            }

            /*
             * 校验和(4bytes)
             校验和的算法如下：
             对“消息头+会话头+事务头+操作信息”按32位异或，对异或结果取反后的值为校验和，8个16进制的明文。
          */
            headBuffer.put(checkSummer.flush());
            out.write(headBuffer.flip());
            /*headBuffer.rewind();
            byte[] kk = new byte[headBuffer.capacity()];
            headBuffer.get(kk);
            logger.debug(new String(kk));*/
        }
    }

    static class CheckSummer {
        private int i = 0;
        private byte[] res = {0, 0, 0, 0, 0, 0, 0, 0};

        public void update(byte[] buf) {
            for (byte b : buf) {
                res[i++] ^= b;
                if (i > 3) i = 0;
            }
        }

        public byte[] flush() {
            res[0] = (byte) ~res[0];
            res[1] = (byte) ~res[1];
            res[2] = (byte) ~res[2];
            res[3] = (byte) ~res[3];
            for (int i = 8 - 1; i >= 0; i--) {
                if (i % 2 > 0) { // LOW
                    res[i] = (byte) ((res[i / 2] & 0x0F) + '0');
                    if (res[i] > '9')
                        res[i] = (byte) (res[i] + 'A' - '0' - 10);
                } else { // HIGH
                    res[i] = (byte) (((res[i / 2] >> 4) & 0x0F) + '0');
                    if (res[i] > '9')
                        res[i] = (byte) (res[i] + 'A' - '0' - 10);
                }
            }
            return res;
        }
    }
}
