/*
 * cn.zj.easynet.remote.UpgradeDto.java
 * Jun 27, 2014 
 */
package cn.zj.easynet.remote;

import java.util.Date;

import cn.zj.easynet.util.ConfigUtil;

/**
 * Jun 27, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Upgrade4AndroidDto {
    private long serverTime = new Date().getTime();
    private String lastVer = ConfigUtil.ANDROID_NEW_LASTVER;
    private String download = ConfigUtil.ANDROID__NEW_DOWNLOAD;
    private String force = ConfigUtil.ANDROID__NEW_FORCE;//根据账号0：不提示
    private String description = ConfigUtil.ANDROID__NEW_DESCRIPTION;

    private String lowVer = "2.1.0.0";

    /**
     * @return the serverTime
     */
    public long getServerTime() {
        return serverTime;
    }

    /**
     * @param serverTime
     *            the serverTime to set
     */
    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    /**
     * @return the lastVer
     */
    public String getLastVer() {
        return lastVer;
    }

    /**
     * @param lastVer
     *            the lastVer to set
     */
    public void setLastVer(String lastVer) {
        this.lastVer = lastVer;
    }

    /**
     * @return the download
     */
    public String getDownload() {
        return download;
    }

    /**
     * @param download
     *            the download to set
     */
    public void setDownload(String download) {
        this.download = download;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the lowVer
     */
    public String getLowVer() {
        return lowVer;
    }

    /**
     * @param lowVer
     *            the lowVer to set
     */
    public void setLowVer(String lowVer) {
        this.lowVer = lowVer;
    }

    /**
     * @return the force
     */
    public String getForce() {
        return force;
    }

    /**
     * @param force
     *            the force to set
     */
    public void setForce(String force) {
        this.force = force;
    }

}
