/**
 * @(#)GameBindsInfo.java, 2014-3-14.
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.zj.easynet.device.config;

import cn.zj.easynet.device.dao.domain.YixinUinfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 某个游戏游戏绑定在CONFIG中的info,
 * 
 * @author jiangzhouyun@corp.netease.com
 */
@JSONType(orders = { "id", "name", "icon", "proicon", "bind", "rid" })
public class GameBindInfo {

    /**
     * id
     */
    private Short id;

    /**
     * name
     */
    private String name;

    /**
     * 游戏main_icon
     */
    private String icon;

    /**
     * 个人profession下载地址（此url + GameUserInfo.proicon）
     */
    private String proicon;

    /**
     * bits位，0位表示是开绑定（1绑定），1位表示是否开启消息提醒，1表示开启
     */
    private Integer bind;

    /**
     * 对应绑定的游戏ID
     */
    private String rid;

    /**
     * bind对应位的定义
     */
    private GameInfoBindFlag bindFlag;

    public GameBindInfo() {}

    public GameBindInfo(Short id, String name, String icon, String proicon,
        int bind, String rid) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.proicon = proicon;
        this.bind = bind;
        this.rid = rid;
        this.bindFlag = new GameInfoBindFlag(bind);
    }

    /**
     * @return the id
     */
    public Short getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Short id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the proicon
     */
    public String getProicon() {
        return proicon;
    }

    /**
     * @param proicon
     *            the proicon to set
     */
    public void setProicon(String proicon) {
        this.proicon = proicon;
    }

    /**
     * @return the bind
     */
    public Integer getBind() {
        return bind;
    }

    /**
     * @param bind
     *            the bind to set
     */
    public void setBind(Integer bind) {
        this.bind = bind;
        if (this.bindFlag == null) {
            this.bindFlag = new GameInfoBindFlag(bind);
        } else {
            this.bindFlag.setFlagsValue(bind);
        }
    }

    /**
     * bindFlag标识
     * 
     * @return
     */
    public GameInfoBindFlag bindFlagValue() {
        if (this.bindFlag == null) {
            this.bindFlag = new GameInfoBindFlag(bind);
        }

        return this.bindFlag;
    }

    /**
     * @return the rid
     */
    public String getRid() {
        return rid;
    }

    /**
     * @param rid
     *            the rid to set
     */
    public void setRid(String rid) {
        this.rid = rid;
    }

    /**
     * 设置bind, true设置开启, false设置关闭
     * 
     * @param isBindGame
     */
    public void setBindGame(boolean isBindGame) {
        bindFlagValue().setBindGame(isBindGame);
        this.bind = bindFlagValue().getFlagsValue();
    }
    
    /**
     * 设置status, true消息提醒，false,不提醒
     * 
     * @param isMsgNotify
     */
    public void setGameMsgNotify(boolean isMsgNotify) {
        bindFlagValue().setGameMsgNotify(isMsgNotify);
        this.bind = bindFlagValue().getFlagsValue();
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        Short id = 50;
        String name = "倩女幽魂";

        String mainicon = "http://nos.netease.com/yixinpublic/qn2logo.png";
        String proiconurl = "http://nos.netease.com/yixinpublic/";
        YixinUinfo u = YixinUinfo.getNullInstance();
        u.setConfig("");
        //   u.addGameData(id, name, mainicon, proiconurl, "3821110002");

        GameBindInfo info = new GameBindInfo(id, name, mainicon, proiconurl, 0,
            "3821110002");
        System.out.println(info.toString());
        info.setBindGame(false);
        info.setGameMsgNotify(false);
        System.out.println(info.toString());
    }

}
