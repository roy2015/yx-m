/**
 * @(#)UrsMailInfo.java, 2013-10-19.
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.zj.easynet.device.http.request;

import java.util.Map;

import cn.zj.easynet.common.util.StringUtil;
import cn.zj.easynet.device.dao.domain.YixinUinfo;

/**
 * 第三方帐号注册信息
 * 
 * @author jiangzhouyun@corp.netease.com
 */
public class UrsMailInfo {

    //username=m13555556660&firstname=&lastname=&gender=-1
    private static final String USERNAME = "username";

    private static final String FIRSTNAME = "firstname";
    
    private static final String LASTNAME = "lastname";

    private static final String GENDER = "gender";

    private String mobile;

    private String firstname;
    
    private String nick;
    
    private byte gender;

    public UrsMailInfo(){}
    
    public UrsMailInfo(String mobile, String nick, byte gender, String firstname) {
        this.mobile = mobile;
        this.nick = nick;
        this.gender = gender;
        this.firstname = firstname;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick
     *            the nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return the gender
     */
    public byte getGender() {
        return gender;
    }

    /**
     * @param gender
     *            the gender to set
     */
    public void setGender(byte gender) {
        this.gender = gender;
    }
    
    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * 跟据params参数构建对应的注册用户信息对象
     * 
     * @param params
     * @return
     */
    public static UrsMailInfo parseMailParams(Map<String, String> params) {

        if (params == null || params.isEmpty()) {
            return null;
        }

        String username = params.get(USERNAME);
        if (StringUtil.isEmpty(username)) {
            return null;
        }

        String firstname = params.get(FIRSTNAME);
        String nick = params.get(LASTNAME);
        String sex = params.get(GENDER);
        byte gender = YixinUinfo.Sex.UNKNOWN;
        if (sex.equals("0")) {
            gender = YixinUinfo.Sex.MALE;
        } else if (sex.equals("1")) {
            gender = YixinUinfo.Sex.FEMALE;
        }

        return new UrsMailInfo(username, nick, gender, firstname);
    }

}
