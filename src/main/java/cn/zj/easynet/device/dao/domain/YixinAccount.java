package cn.zj.easynet.device.dao.domain;

import cn.zj.easynet.common.marshal.DomainTag;
import cn.zj.easynet.common.marshal.LiteDomain;
import cn.zj.easynet.device.cache.annotation.CacheKey;
import cn.zj.easynet.device.flag.SyncFlag;

public class YixinAccount extends LiteDomain {

    @CacheKey
    @DomainTag(id = 1)
    private Long uid;

    @DomainTag(id = 2)
    private String pass;

    @DomainTag(id = 3)
    private Integer regtime;

    @DomainTag(id = 4)
    private Integer enableflag;

    @DomainTag(id = 5)
    private Integer authtype;

    @DomainTag(id = 6)
    private Integer timetag;

    // [!] 该字段仅用于统计，对客户端不可见，因此不设DomainTag
    private String property; // 用户注册时的属性，如客户端ip/版本/渠道等，存为一个json字符串

    /**
     * 个人帐号服务器端相关配制项,对客户端不可见
     */
    @DomainTag(id = 7)
    private Integer sync;

    /**
     * sync 对应的标记位映射
     */
    private SyncFlag flag;

    public YixinAccount() {
        regtime = 0;
        enableflag = 1;
        authtype = 0;
        timetag = 0;
        sync = 0;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getRegtime() {
        return regtime;
    }

    public void setRegtime(Integer regtime) {
        this.regtime = regtime;
    }

    public Integer getEnableflag() {
        return enableflag;
    }

    public void setEnableflag(Integer enableflag) {
        this.enableflag = enableflag;
    }

    public Integer getAuthtype() {
        return authtype;
    }

    public void setAuthtype(Integer authtype) {
        this.authtype = authtype;
    }

    public Integer getTimetag() {
        return timetag;
    }

    public void setTimetag(Integer timetag) {
        this.timetag = timetag;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the sync
     */
    public Integer getSync() {
        return sync;
    }

    /**
     * @param sync
     *            the sync to set
     */
    public void setSync(Integer sync) {
        this.sync = sync;
        if (this.flag == null) {
            this.flag = new SyncFlag();
        }
        this.flag.setFlagsValue(this.sync);
    }

    public SyncFlag getFlag() {
        if (flag == null) {
            if (this.sync == null) {
                this.sync = 0;
            }
            flag = new SyncFlag(this.sync);
        }
        return flag;
    }

    /**
     * 是否同步了
     * 
     * @return true为已经同步，false未同步
     */
    public boolean isChinatelecomSync() {
        return this.getFlag().isChinatelecomSync();
    }

    /**
     * 设置sync, true同步， false未同步
     * 
     * @param isSync
     */
    public void setChinatelecomSync(boolean isSync) {
        this.getFlag().setChinatelecomSync(isSync);
        this.sync = getFlag().getFlagsValue();
    }

    @Override
    public String toString() {
        return "YixinAccount [uid=" + uid + ", pass=" + pass + ", regtime="
            + getReadableTimetag(regtime) + ", enableflag=" + enableflag
            + ", authtype=" + authtype + ", sync=" + sync + ", timetag="
            + getReadableTimetag(timetag) + ", property=" + property + "]";
    }
    
    /**
     * 帐号注册类型
     * 
     * @author jiangzhouyun@corp.netease.com
     */
    public enum AccountRegType {

        // 默认注册
        DEFAULT_REG(0),

        // urs注册
        URS_REG(1),

        // 浙江翼校通注册
        GREENROOM_REG(2, "yixiaotong"),

        //倩女
        QIAN_NV(3, "qiannv"),

        //企业邮
        QIYE_EMAIL(4, "qiyeemail"),

        //coremail
        COREMAIL(5, "coremail"),

        //mail邮箱用户
        MAIL(6, "mail"),

        //全国翼校通
        ALL_YIXIAOTONG(7, "allyixiaotong"),

        //兰州翼校通
        YIXIAOTONG_LANZHOU(8, "allyixiaotonglz"),

        //天翼用户  -- 使用易信登录时,注册成易信用户
        CHIATELECOM(9),

        /**
         * 电信四川校园行
         */
        CHINATELECOM_CAMPUS(10, "chinatelecomcampus"),

        //泉州翼校通
        YIXIAOTONG_QUANZHOU(11, "allyixiaotongqz"),

        // 邮箱一键登录自动注册，无密码
        URS_WEB_REG_NO_PASS(12), URS_WEB_REG(13),

        //广西翼校通
        YIXIAOTONG_GUANGXI(14, "allyixiaotonggx"),

        MENGHUAN(15, "xyq"),

        LONGJIAN(16, "longjian"),

        //天下3
        TIANXIA3(17, "tianxia3"),

        //公开平台
        PUBLIC(18, "public"),

        //同城约会
        TONGCHENG(19, "tcdate"),

        //鬼魂
        WUHUN(20, "wuhun"),

        //中国电信综合办公
        DIANXIN_OFFICE(21, "chinatelecomoffice"),
        
        //大话2
        DAHUA2(22, "dh2"),
        
        // 189邮箱一键登录自动注册，无密码
        _189_WEB_REG_NO_PASS(23), _189_WEB_REG(24),
        
        // 公众号翼校通
        PUBLIC_ACCOUNT_YIXIAOTONG(25, "publicaccountyixiaotong"),
        
        //易信子公司的一个产品，农技宝(农技宝从120+开始)
        DIANXIN_NONGJIBAO_ZJ(121, "nongjibaozj"),

        //易信子公司的一个产品，广东农技宝
        DIANXIN_NONGJIBAO_GD(122, "nongjibaogd");

        private int value;

        private String product;

        private AccountRegType(int value) {
            this.value = value;
        }

        private AccountRegType(int value, String product) {
            this.value = value;
            this.product = product;
        }

        public int getValue() {
            return value;
        }

        public String getProduct() {
            return product;
        }

        /**
         * 帐号注册类型
         * 
         * @param value
         * @return
         */
        public static AccountRegType valueOf(int value) {
            switch (value) {
                case 0: {
                    return DEFAULT_REG;
                }
                case 1: {
                    return URS_REG;
                }
                case 2: {
                    return GREENROOM_REG;
                }
                case 3: {
                    return QIAN_NV;
                }
                case 4: {
                    return QIYE_EMAIL;
                }
                case 5: {
                    return COREMAIL;
                }
                case 6: {
                    return MAIL;
                }
                case 7: {
                    return ALL_YIXIAOTONG;
                }
                case 8: {
                    return YIXIAOTONG_LANZHOU;
                }
                case 9: {
                    return CHIATELECOM;
                }
                case 10: {
                    return CHINATELECOM_CAMPUS;
                }
                case 11: {
                    return YIXIAOTONG_QUANZHOU;
                }
                case 12: {
                    return URS_WEB_REG_NO_PASS;
                }
                case 13: {
                    return URS_WEB_REG;
                }
                case 14: {
                    return YIXIAOTONG_GUANGXI;
                }
                case 15: {
                    return MENGHUAN;
                }
                case 16: {
                    return LONGJIAN;
                }
                case 17: {
                    return TIANXIA3;
                }
                case 18: {
                    return PUBLIC;
                }
                case 19: {
                    return TONGCHENG;
                }
                case 20: {
                    return WUHUN;
                }
                case 21: {
                    return DIANXIN_OFFICE;
                }

                case 22: {
                    return DAHUA2;
                }

                case 25: {
                    return PUBLIC_ACCOUNT_YIXIAOTONG;
                }
                
                case 121: {
                    return DIANXIN_NONGJIBAO_ZJ;
                }

                case 122: {
                    return DIANXIN_NONGJIBAO_GD;
                }

                default: {
                    throw new IllegalArgumentException(
                        "illegal account reg type:" + value);
                }
            }
        }

        /**
         * 跟据产品名，得知其对应的注册类型
         * 
         * @param product
         * @return
         */
        public static AccountRegType valueOfProduct(String product) {
            if (product == null) {
                return AccountRegType.DEFAULT_REG;
            } else if (product.equals(AccountRegType.QIAN_NV.getProduct())) {
                return AccountRegType.QIAN_NV;
            } else if (product
                .equals(AccountRegType.GREENROOM_REG.getProduct())) {
                return AccountRegType.GREENROOM_REG;
            } else if (product.equals(AccountRegType.QIYE_EMAIL.getProduct())) {
                return AccountRegType.QIYE_EMAIL;
            } else if (product.equals(AccountRegType.COREMAIL.getProduct())) {
                return AccountRegType.COREMAIL;
            } else if (product.equals(AccountRegType.MAIL.getProduct())) {
                return AccountRegType.MAIL;
            } else if (product.equals(AccountRegType.ALL_YIXIAOTONG
                .getProduct())) {
                return AccountRegType.ALL_YIXIAOTONG;
            } else if (product.equals(AccountRegType.YIXIAOTONG_LANZHOU
                .getProduct())) {
                return AccountRegType.YIXIAOTONG_LANZHOU;
            } else if (product.equals(AccountRegType.YIXIAOTONG_QUANZHOU
                .getProduct())) {
                return AccountRegType.YIXIAOTONG_QUANZHOU;
            } else if (product.equals(AccountRegType.CHINATELECOM_CAMPUS
                .getProduct())) {
                return AccountRegType.CHINATELECOM_CAMPUS;
            } else if (product.equals(AccountRegType.YIXIAOTONG_GUANGXI
                .getProduct())) {
                return AccountRegType.YIXIAOTONG_GUANGXI;
            } else if (product.equals(AccountRegType.MENGHUAN.getProduct())) {
                return AccountRegType.MENGHUAN;
            } else if (product.equals(AccountRegType.LONGJIAN.getProduct())) {
                return AccountRegType.LONGJIAN;
            } else if (product.equals(AccountRegType.TIANXIA3.getProduct())) {
                return AccountRegType.TIANXIA3;
            } else if (product.equals(AccountRegType.PUBLIC.getProduct())) {
                return AccountRegType.PUBLIC;
            } else if (product.equals(AccountRegType.TONGCHENG.getProduct())) {
                return AccountRegType.TONGCHENG;
            } else if (product.equals(AccountRegType.WUHUN.getProduct())) {
                return AccountRegType.WUHUN;
            } else if (product.equals(AccountRegType.DIANXIN_OFFICE
                .getProduct())) {
                return AccountRegType.DIANXIN_OFFICE;
            } else if (product.equals(AccountRegType.DIANXIN_NONGJIBAO_ZJ
                .getProduct())) {
                return AccountRegType.DIANXIN_NONGJIBAO_ZJ;
            } else if (product.equals(AccountRegType.DIANXIN_NONGJIBAO_GD
                .getProduct())) {
                return AccountRegType.DIANXIN_NONGJIBAO_GD;
            } else if (product.equals(AccountRegType.DAHUA2.getProduct())) {
                return AccountRegType.DAHUA2;
            } else if (product
                .equals(AccountRegType.PUBLIC_ACCOUNT_YIXIAOTONG.getProduct())) {
                return AccountRegType.PUBLIC_ACCOUNT_YIXIAOTONG;
            } else {
                return AccountRegType.DEFAULT_REG;
            }
        }
    }
}
