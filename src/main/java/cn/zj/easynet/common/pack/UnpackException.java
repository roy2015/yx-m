package cn.zj.easynet.common.pack;

public class UnpackException extends RuntimeException
{
    public static final long serialVersionUID = 12L;

    public UnpackException()
    {
        this( "Unpack error" );
    }

    public UnpackException( String message )
    {
        super( message );
    }

    public UnpackException( Throwable cause )
    {
        super( cause );
    }

    public UnpackException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
