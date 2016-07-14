/*
 * cn.zj.easynet.yx.it.remote.request.ZxAccountReq.java
 * Jul 14, 2014 
 */
package cn.zj.easynet.remote.request;

import cn.zj.easynet.remote.dto.ZxRequestEntryDto;


/**
 * Jul 14, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ZxAccountReq extends CmdRequestBase {
    private ZxRequestEntryDto entry;

    /**
     * @return the entry
     */
    public ZxRequestEntryDto getEntry() {
        return entry;
    }

    /**
     * @param entry
     *            the entry to set
     */
    public void setEntry(ZxRequestEntryDto entry) {
        this.entry = entry;
    }

}
