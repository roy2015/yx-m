/**
 * @(#)RequestResult.java, 2013年7月27日.
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.zj.easynet.device.http.request;

import cn.zj.easynet.device.dao.domain.YixinAccount.AccountRegType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * request service请求后返回响应（给应用层调用）
 * 
 * @author jiangzhouyun@corp.netease.com
 */
@JSONType(orders = { "code", "msg", "mainaccount", "sid", "cookie", "mail" })
public class ResponseResult {

    /**
     * 错误code
     */
    private int code;

    /**
     * 错误描述,如果189邮箱登录成功，则此为sid
     */
    private String msg;

    private String mainaccount;

    private String cookie;

    private String sid;

    private String domain;

    private UrsMailInfo mail;
    
    /**
     * 帐号类型
     */
    private AccountRegType regtype;

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, String msg, UrsMailInfo mail) {
        this.code = code;
        this.msg = msg;
        this.mail = mail;
    }

    public ResponseResult(int code, String msg, String mainaccount) {
        this.code = code;
        this.msg = msg;
        this.mainaccount = mainaccount;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the mainaccount
     */
    public String getMainaccount() {
        return mainaccount;
    }

    /**
     * @param mainaccount
     *            the mainaccount to set
     */
    public void setMainaccount(String mainaccount) {
        this.mainaccount = mainaccount;
    }

    public void setMobileAccount(String mobile) {
        if (mainaccount != null) {
            int len = mainaccount.indexOf("@");
            if (len > 0) {
                String prefix = mainaccount.substring(len);
                this.mainaccount = mobile + prefix;
            }
        }
    }

    /**
     * @return the cookie
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * @param cookie
     *            the cookie to set
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid
     *            the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return the mail
     */
    public UrsMailInfo getMail() {
        return mail;
    }

    /**
     * @param mail
     *            the mail to set
     */
    public void setMail(UrsMailInfo mail) {
        this.mail = mail;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * @return the regtype
     */
    public AccountRegType getRegtype() {
        return regtype;
    }

    /**
     * @param regtype the regtype to set
     */
    public void setRegtype(AccountRegType regtype) {
        this.regtype = regtype;
    }

    public static void main(String[] args) {
        String mainaccount = "abc@163.com";
        String mobile = "13211112222";
        int len = mainaccount.indexOf("@");
        if (len > 0) {
            String prefix = mainaccount.substring(len);
            mainaccount = mobile + prefix;
        }
        System.out.println(mainaccount);
    }
}
