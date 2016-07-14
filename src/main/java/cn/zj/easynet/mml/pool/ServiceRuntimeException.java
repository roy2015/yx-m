package cn.zj.easynet.mml.pool;

public class ServiceRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 5600381356270338141L;
    
    private static final String PREFIX = "ServiceRuntimeException, Error Code: ";
    
    private static final String SUFFIX = ", Message: ";
    
    private int m_code;
    
    public ServiceRuntimeException()
    {
        this(null, 500);
    }
    
    public ServiceRuntimeException(int code)
    {
        this(null, code);
    }
    
    public ServiceRuntimeException(String msg)
    {
        this(msg, 500);
    }
    
    public ServiceRuntimeException(String msg, int code)
    {
        super(PREFIX + code + ((msg == null || msg.trim().isEmpty()) ? "" : (SUFFIX + msg)));
        
        m_code = code;
    }
    
    public int getCode()
    {
        return m_code;
    }
}
