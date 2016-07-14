package cn.zj.easynet.common.pack;

import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import cn.zj.easynet.common.marshal.Marshallable;

public class Unpack {
    protected ByteBuffer buffer;

    public int GetSize() {
        return buffer.limit() - buffer.position();
    }

    /* wrap */
    public Unpack(byte[] bytes, int offset, int length) {
        buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Unpack(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    /**
     * buf [ position : limit] -> Unpack
     */
    public Unpack(ByteBuffer buf) {
        this(buf.array(), buf.position(), buf.limit() - buf.position());
    }

    /**
     * 该函数没有buffer，只是便于Class.newInstance进行统一初始化, 只有popObject能继续使用之
     */
    public Unpack() {
        buffer = null;
    }

    public ByteBuffer getBuffer() {
        return buffer.duplicate();
    }

    public byte[] popFetch(int sz) {
        try {
            //warning , 去掉了一个excepiton, 把过大的内容都返回空byte.避免16位string存进去获取异常
            if (sz < 0)
                return null;
            byte[] fetch = new byte[sz];
            buffer.get(fetch);
            return fetch;
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException();
        }
    }

    public Object popObject(Object obj) throws UnpackException {
        if (obj instanceof Marshallable) {
            return popMarshallable((Marshallable) obj);
        } else if (obj instanceof String) {
            return popVarstr();
        } else if (obj instanceof Unpack) {
            ((Unpack) obj).buffer = ByteBuffer.allocate(buffer.remaining());
            ((Unpack) obj).buffer.order(ByteOrder.LITTLE_ENDIAN);
            ((Unpack) obj).buffer.put(buffer);
            ((Unpack) obj).buffer.rewind();
            return obj;
        } else {
            throw new UnpackException("unknow object type");
        }
    }

    public byte popByte() {
        try {
            return buffer.get();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException();
        }
    }

    // 16位的大小
    public byte[] popVarbin() {
        return popFetch(buffer.getShort());
    }

    // 32位的大小
    public byte[] popVarbin32() {
        return popFetch(buffer.getInt());
    }

    public String popVarbin(String encode) {
        try {
            byte[] bytes = popVarbin();
            return new String(bytes, encode);
        } catch (UnsupportedEncodingException codeEx) {
            throw new UnpackException();
        }
    }

    public String popVarbin32(String encode) {
        try {
            byte[] bytes = popVarbin32();
            return new String(bytes, encode);
        } catch (UnsupportedEncodingException codeEx) {
            throw new UnpackException();
        }
    }

    public String popVarstr() {
        return popVarbin("utf-8");
    }

    public String popVarstr(String encode) {
        return popVarbin(encode);
    }

    public String popVarstr32() {
        return popVarbin32("utf-8");
    }

    public String popVarstr32(String encode) {
        return popVarbin32(encode);
    }

    public int popInt() {
        try {
            return buffer.getInt();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException();
        }
    }

    public long popLong() {
        try {
            return buffer.getLong();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException();
        }
    }

    public short popShort() {
        try {
            return buffer.getShort();
        } catch (BufferUnderflowException bEx) {
            throw new UnpackException();
        }
    }

    public Marshallable popMarshallable(Marshallable mar) {
        mar.unmarshal(this);
        return mar;
    }

    public boolean popBoolean() {
        if (popByte() > 0)
            return true;
        else
            return false;
    }

    public String toString() {
        return buffer.toString();
    }

    public int popVarUint() {
        int value = 0;
        int i = 0;
        int b;

        while (((b = buffer.get()) & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;

            if (i > 35) {
                throw new UnpackException(
                    "Variable length quantity is too long");
            }
        }

        return value | (b << i);
    }

    public long popVarUlong() {
        long value = 0;
        int i = 0;
        long b;

        while (((b = buffer.get()) & 0x80L) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;

            if (i > 63) {
                throw new UnpackException(
                    "Variable length quantity is too long");
            }
        }

        return value | (b << i);
    }
}
