package cn.zj.easynet.common.util;

import java.nio.ByteBuffer;

public class NioBufferManager
{
    static NioBufferManager m_instance = new NioBufferManager();

    public static ByteBuffer allocate()
    {
        ByteBuffer buffer = ByteBuffer.allocate( 8192 );
        return buffer;
    }
    
    public static ByteBuffer allocate(int size)
    {
        ByteBuffer buffer = ByteBuffer.allocate( size );
        return buffer;
    }

    public static void disponse( ByteBuffer buffer )
    {
    }
}
