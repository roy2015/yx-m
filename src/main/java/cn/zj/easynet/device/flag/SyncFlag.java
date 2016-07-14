/**
 * @(#)SyncFlag.java, 2013-12-24.
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.zj.easynet.device.flag;

/**
 * 用于服务器端，个人帐号相关个人配制的标识位
 *
 * @author jiangzhouyun@corp.netease.com
 *
 */
public class SyncFlag extends Flags {
    
    /**
     * 标记位 0 = 是否同步给天翼帐号，默认0否，1同步了
     */
    public static final int ID_CHIANTELECOM_SYNC = 0;

    /**
     * 是否同步给天翼帐号，默认0否，1同步了
     */
    public static final Integer FLAG_CHIANTELECOM_SYNC = 1 << ID_CHIANTELECOM_SYNC;

    /**
     * 默认构造函数
     */
    public SyncFlag() {
        super();
    }

    /**
     * 构造函数
     * 
     * @param flags
     */
    public SyncFlag(Integer flags) {
        super(flags);
    }

    /**
     * 是否同步了
     * 
     * @return true为已经同步，false未同步
     */
    public boolean isChinatelecomSync() {
        return (flagsValue & FLAG_CHIANTELECOM_SYNC) == FLAG_CHIANTELECOM_SYNC;
    }
    
    /**
     * 设置sync, true同步， false未同步
     * 
     * @param isSync
     */
    public void setChinatelecomSync(boolean isSync) {
        if(isSync) {
            flagsValue |= FLAG_CHIANTELECOM_SYNC;
        }else {
            flagsValue &= ~FLAG_CHIANTELECOM_SYNC;
        }
    }
    
    public static void main(String[] args) {
        int config = 0;
        SyncFlag flag = new SyncFlag(config);
        System.out.println("isSync:"+flag.isChinatelecomSync());
        
        config = 1;
        flag.setFlagsValue(config);
        System.out.println("isSync:"+flag.isChinatelecomSync());
        
        flag.setChinatelecomSync(false);
        System.out.println("isSync:"+flag.isChinatelecomSync());
        
        flag.setChinatelecomSync(true);
        System.out.println("isSync:"+flag.isChinatelecomSync());
    }

}
