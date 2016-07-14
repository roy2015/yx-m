package cn.zj.easynet.common.pack;

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.log4j.Logger;

import cn.zj.easynet.common.marshal.Marshallable;
import cn.zj.easynet.common.util.NioBufferManager;

public class Pack {
    private Logger m_logger = Logger.getLogger(Pack.class);

    private ByteBuffer m_buffer;

    private int m_maxCapacity = 15 * 1024 * 1024;

    public Pack() {
        m_buffer = NioBufferManager.allocate(1024);
        m_buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Pack(int initialSize) {
        m_buffer = NioBufferManager.allocate(initialSize);
        m_buffer.order(ByteOrder.LITTLE_ENDIAN);

    }

    public int size() {
        return m_buffer.position();
    }

    // 获取当前的偏移量
    public int getPosition() {
        return m_buffer.position();
    }

    /**
     * Get buffer of Pack with flip()
     */
    public ByteBuffer getBuffer() {
        ByteBuffer dup = m_buffer.duplicate();
        dup.flip();
        return dup;
    }

    public Pack putFetch(byte[] bytes) {
        try {
            ensureCapacity(bytes.length);
            m_buffer.put(bytes);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public Pack putByte(byte bt) {
        try {
            ensureCapacity(1);
            m_buffer.put(bt);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public Pack putVarstr(String str) {
        try {
            return putVarbin(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException codeEx) {
            throw new PackException();
        }
    }

    public Pack putVarstr32(String str) {
        try {
            return putVarbin32(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException codeEx) {
            throw new PackException();
        }
    }

    public Pack putInt(int val) {
        try {
            ensureCapacity(4);
            m_buffer.putInt(val);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public Pack putBoolean(boolean val) {
        try {
            ensureCapacity(1);
            m_buffer.put((byte) (val ? 1 : 0));
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public Pack putLong(long val) {
        try {
            ensureCapacity(8);
            m_buffer.putLong(val);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public Pack putShort(short val) {
        try {
            ensureCapacity(2);
            m_buffer.putShort(val);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    // 16位的
    public Pack putVarbin(byte[] bytes) {
        try {
            //warning 超过16位限制的 不存有效数据,避免获取失败. 为了减少线上异常报警数量设置 
            if (bytes.length > Short.MAX_VALUE) {
                bytes = new byte[0];
            }
            ensureCapacity(2 + bytes.length);
            m_buffer.putShort((short) bytes.length);
            m_buffer.put(bytes);
            return this;
        } catch (BufferOverflowException bex) {
            m_logger.error("bytes size:" + bytes.length, bex);
            throw new PackException();
        }
    }

    // 32位的
    public Pack putVarbin32(byte[] bytes) {
        try {
            ensureCapacity(4 + bytes.length);
            m_buffer.putInt((int) bytes.length);
            m_buffer.put(bytes);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public Pack putMarshallable(Marshallable mar) {
        mar.marshal(this);
        return this;
    }

    public Pack putBuffer(ByteBuffer buf) {
        try {
            ensureCapacity(buf.remaining());
            m_buffer.put(buf);
            return this;
        } catch (BufferOverflowException bex) {
            throw new PackException();
        }
    }

    public void replaceShort(int off, short val) {
        try {
            int pos = m_buffer.position();
            m_buffer.position(off);
            m_buffer.putShort(val).position(pos);
        } catch (BufferOverflowException bex) {
            throw new PackException();
        } catch (IllegalArgumentException iex) {
            throw new PackException();
        }
    }

    public void replaceInt(int off, int val) {
        try {
            int pos = m_buffer.position();
            m_buffer.position(off);
            m_buffer.putInt(val).position(pos);
        } catch (BufferOverflowException bex) {
            throw new PackException();
        } catch (IllegalArgumentException iex) {
            throw new PackException();
        }
    }

    /**
     * Ensures that a the buffer can hold up the new increment
     * 
     * @throws Exception
     */
    public void ensureCapacity(int increament) throws BufferOverflowException {
        if (m_buffer.remaining() >= increament) {
            return;
        }

        int requiredCapacity = m_buffer.capacity() + increament
            - m_buffer.remaining();

        if (requiredCapacity > m_maxCapacity) {
            m_logger.info("DynamicBuffer maximun size reached " + m_maxCapacity
                + ", required size " + requiredCapacity);
            throw new BufferOverflowException();
        }

        int tmp = Math.max(requiredCapacity, m_buffer.capacity() * 2);
        int newCapacity = Math.min(tmp, m_maxCapacity);

        ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
        newBuffer.order(m_buffer.order());
        m_buffer.flip();
        newBuffer.put(m_buffer);
        m_buffer = newBuffer;
    }

    public String toString() {
        return m_buffer.toString() + " Size " + size();
    }

    // http://svn.apache.org/repos/asf/mahout/trunk/core/src/main/java/org/apache/mahout/math/Varint.java
    public void putVarUint(int value) {
        while ((value & 0xFFFFFF80) != 0) {
            m_buffer.put((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        m_buffer.put((byte) (value & 0x7F));
    }

    public void putVarUlong(long value) {
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
            m_buffer.put((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        m_buffer.put((byte) (value & 0x7F));
    }

    public static void main(String... args) {
        Pack p = new Pack();
        p.putVarUint(123);
        System.out.println("varint len: " + p.getBuffer().limit());

        Unpack up = new Unpack(p.getBuffer());
        System.out.println(up.popVarUint());
    }
}
