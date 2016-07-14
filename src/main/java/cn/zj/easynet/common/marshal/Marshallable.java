package cn.zj.easynet.common.marshal;

import cn.zj.easynet.common.pack.Pack;
import cn.zj.easynet.common.pack.Unpack;

public interface Marshallable
{
    public abstract void marshal( Pack pack );

    public abstract void unmarshal( Unpack unpack );
}
