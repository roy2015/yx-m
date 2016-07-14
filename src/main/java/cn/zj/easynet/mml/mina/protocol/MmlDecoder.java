package cn.zj.easynet.mml.mina.protocol;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import cn.zj.easynet.util.ConfigUtil;

import java.util.Arrays;

@SuppressWarnings("unused")
public class MmlDecoder extends CumulativeProtocolDecoder {
    private static final Logger logger = Logger.getLogger(MmlDecoder.class);

    @Override
    public boolean doDecode(IoSession session, IoBuffer buf, ProtocolDecoderOutput out) throws Exception {
        int readableBytes = buf.remaining();
        if (readableBytes < 8) return false;
        buf.mark();
        
        /*byte[]  temp1 = new byte[readableBytes];
        buf.get(temp1);
        logger.debug(new String(temp1));*/

        byte[] temp = new byte[4];
        buf.get(temp);
        if (!Arrays.equals(ConfigUtil.header, temp)) {    // 协议不匹配
            buf.reset();
            temp = new byte[readableBytes];
            buf.get(temp);
            throw new RuntimeException("协议不匹配:" + new String(temp));
        }

        buf.get(temp);
        int nextPackageLength = Integer.parseInt(new String(temp), 16);

        if (readableBytes < nextPackageLength + 16) {
            buf.reset();
            return false; // 数据不足
        }

        buf.skip(12);   // 忽略消息头（20byte），忽略会话控制

        /*
            * 会话头(18byte)
                会话是ECP营运中心接入系统与第三方系统之间的一个虚拟连接（逻辑连接），唯一标示的一次有效登录。会话头包含如下部分：
                会话ID：16进制字符（0-F）标示的32位整数，长度8个字节，从1开始，在客户端向服务端登录时由服务端统一分配。
         */
        byte[] dlgID = (byte[]) session.getAttribute("dlgID");
        if (dlgID == null) {
            dlgID = new byte[8];
            buf.get(dlgID);
            String x = new String(dlgID);
            logger.debug("doDecode(): 会话ID:" + x);
            session.setAttribute("dlgID", dlgID);
        } else {
        	logger.debug("doDecode(): \tdlgID is not null!");
            buf.skip(8);
        }
        
        buf.skip(8);

        // 读取事务 ID
        byte[] txIdBytes = new byte[8];
        buf.get(txIdBytes);
        buf.skip(10);
        buf.skip(10);    // 忽略事务控制字

        // 操作信息
        byte[] data = new byte[nextPackageLength - 56];
        buf.get(data);
        buf.skip(8);   // 忽略校验和

        Object o = onMessage(new String(txIdBytes), new String(data, "gbk").trim(), session);
        if (o != null && out != null)
        	out.write(o);
        return buf.remaining() > 0;
    }

    private Object onMessage(String txID, String content, IoSession session) {
        logger.info("onMessage() :收到[" + txID + "]：" + content);
        if (content.startsWith("ACK:")) {   // 回应
            int i = content.indexOf(':', 5);
            if (i < 0) throw new RuntimeException("协议格式错误。");
            String cmd = content.substring(4, i);
            if (content.length() < i + 1)
                throw new RuntimeException("协议格式错误。");
            String substring = content.substring(i + 1);

            Answer answer = new Answer(cmd, txID);
            if ( !substring.isEmpty() ) {
                if (substring.startsWith("RETN=")) {
                    i=substring.indexOf(',');
                    if( i<0 ) {
                        answer.setCode(substring.substring(5) );
                        return answer;
                    }else {
                        answer.setCode(substring.substring(5, i));
                        substring = substring.substring(i + 1);
                    }
                }

                if (substring.startsWith("DESC="))
                    answer.setDescription(substring.substring(5));
            }
            return answer;
        } else {
            return null;
        }
    }
}

