package cn.zj.easynet.device.dao.domain;

import org.apache.commons.lang3.StringUtils;

import cn.zj.easynet.common.marshal.DomainTag;
import cn.zj.easynet.common.marshal.LiteDomain;
import cn.zj.easynet.common.service.ILiteUserService2;
import cn.zj.easynet.common.util.StringUtil;
import cn.zj.easynet.device.cache.annotation.CacheKey;
import cn.zj.easynet.device.cache.annotation.Timetag;
import cn.zj.easynet.device.config.GameBindInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class YixinUinfo extends LiteDomain {

    // 注销账号YID
    public static final String CLOSED_ACCOUNT_YID = "";

    // 注销账号手机号
    public static final String CLOSED_ACCOUNT_MOBILE = "";

    // 重置账号YID
    public static final String RESET_ACCOUNT_YID = "";

    /**
     * 游戏总属性
     */
    private static final String GAMES = "games";

    /**
     * id
     */
    private static final String GAMEID = "id";

    public interface Sex {
        public static final byte UNKNOWN = 0;

        public static final byte MALE = 1;

        public static final byte FEMALE = 2;

        public static final byte YOU_GUESS = 3;
    }

    // [!] 各项开关的默认值是false，即bits的对应位是0
    public interface ConfigBitsMask {
        public static final Long CLOSE_READ_RECEIPT = 1L << 0; // 已读回执

        public static final Long CANNOT_BE_SEARCHED_BY_YID = 1L << 1; // 不允许通过ID搜索到我

        public static final Long CANNOT_BE_SEARCHED_BY_MOBILE = 1L << 2; // 不允许通过手机号搜索到我

        public static final Long CANNOT_RECOMMEND_ADDRESS_BOOK_FRIEND = 1L << 3; // 不向我推荐手机通讯录好友

        public static final Long CANNOT_RECOMMEND_WEIBO_FRIEND = 1L << 4; // 不向我推荐新浪微博好友

        public static final Long CANNOT_RECOMMEND_RENREN_FRIEND = 1L << 5; // 不向我推荐人人网好友

        public static final Long CANNOT_RECOMMEND_YILIAO_FRIEND = 1L << 6; // 不向我推荐翼聊好友

        public static final Long CHECK_BOUND_PHONE_MAIL = 1L << 7; // 是否进行了一次自动绑定手机邮箱的检测（默认0未检测，1检测了）

        public static final Long CLOSE_MAP_LOCATION = 1L << 8; // 不允许看位置
        // public static final Long CANNOT_AUTO_ACCEPT_FRIEND = 1L << 0; //
        // 是否不自动接受添加好友请求

        // 陌生人是否能看朋友圈，默认0，不能看见
        public static final Long CANNOT_SEE_FC_BY_STRANGER = 1L << 9; // 不允许看位置

        public static final Long CLIENT_KEEP = 1L << 10; // 客户端保留
    }

    @CacheKey
    @DomainTag(id = ILiteUserService2.UserInfoTag.ID)
    private Long id;

    @DomainTag(id = ILiteUserService2.UserInfoTag.YID)
    private String yid;

    @DomainTag(id = ILiteUserService2.UserInfoTag.ECP_ID)
    private String ecpId;

    @DomainTag(id = ILiteUserService2.UserInfoTag.MAIL_ID)
    private String mailId;

    @DomainTag(id = ILiteUserService2.UserInfoTag.MOBILE)
    private String mobile;

    @DomainTag(id = ILiteUserService2.UserInfoTag.NICK)
    private String nick;

    @DomainTag(id = ILiteUserService2.UserInfoTag.SIGNATURE)
    private String signature;

    @DomainTag(id = ILiteUserService2.UserInfoTag.ICON)
    private String icon;

    @DomainTag(id = ILiteUserService2.UserInfoTag.SEX)
    private Byte sex;

    @DomainTag(id = ILiteUserService2.UserInfoTag.BIRTHDAY)
    private String birthday;

    @DomainTag(id = ILiteUserService2.UserInfoTag.CITY)
    private String city;

    @DomainTag(id = ILiteUserService2.UserInfoTag.SOCIAL_ADDR)
    private String socialAddr;

    @DomainTag(id = ILiteUserService2.UserInfoTag.BITS)
    private Long bits; // 客户端的各种配置开关，每个占一位

    @Timetag
    @DomainTag(id = ILiteUserService2.UserInfoTag.TIMETAG)
    private Integer timetag;

    @DomainTag(id = ILiteUserService2.UserInfoTag.VALID_FLAG)
    private Byte validFlag;

    /**
     * 1.暂时存用户绑定的游戏信息，其中games为此字段，增加新游戏，只是games中的一个sub
     * ,games为JSONArray,增加其它字段时，跟games平级。
     * game所对应的字段有{"bind":1,"icon":"xxx","id":
     * "000001","name":"倩女幽魂","proicon":"xxx","bit":0,"rid":"xxx"}
     */
    @DomainTag(id = ILiteUserService2.UserInfoTag.CONFIG)
    private String config; // 客户端的各种非开关型配置，存为一个json字符串

    @DomainTag(id = ILiteUserService2.UserInfoTag.EMAIL)
    private String email; // 仅用于展示，不作登录等用途，mailid字段才是预留给以后登录用的

    @DomainTag(id = ILiteUserService2.UserInfoTag.BKIMAGE)
    private String bkImage;

    public YixinUinfo() {}

    public static YixinUinfo getNullInstance() {
        return new YixinUinfo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYid() {
        return yid;
    }

    public void setYid(String yid) {
        this.yid = yid;
    }

    public String getEcpId() {
        return ecpId;
    }

    public void setEcpId(String ecpId) {
        this.ecpId = ecpId;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSocialAddr() {
        return socialAddr;
    }

    public void setSocialAddr(String socialAddr) {
        this.socialAddr = socialAddr;
    }

    public Long getBits() {
        return bits;
    }

    public void setBits(Long bits) {
        this.bits = bits;
    }

    public Integer getTimetag() {
        return timetag;
    }

    public void setTimetag(Integer timetag) {
        this.timetag = timetag;
    }

    public Byte getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Byte validFlag) {
        this.validFlag = validFlag;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * config中增加一个新的key value字段
     * 
     * @param key
     * @param value
     */
    public void addConfigData(String key, String value) {
        JSONObject obj = null;
        if (StringUtils.isBlank(config)) {
            obj = new JSONObject();
        } else {
            obj = JSON.parseObject(config);
        }

        obj.put(key, value);

        this.setConfig(obj.toJSONString());
    }

    /**
     * config中增加一个新的boolean值key value字段
     * 
     * @param key
     * @param value
     */
    public void addBooleanConfig(String key, boolean value) {
        JSONObject obj = null;
        if (StringUtils.isBlank(config)) {
            obj = new JSONObject();
        } else {
            obj = JSON.parseObject(config);
        }

        obj.put(key, value);

        this.setConfig(obj.toJSONString());
    }

    /**
     * 增加一个新的游戏（即games字段中增加一个JSONArray）
     * 
     * @param id
     *            游戏id(预先分配)
     * @param name
     *            游戏name
     * @param mainicon
     * @param prourl
     * @param roleid
     */
    public void addGameData(Short id, String name, String mainicon,
        String prourl, String roleid) {
        if (id == null || StringUtil.isEmpty(name)) {
            throw new NullPointerException("id or name is null");
        }

        JSONObject obj = null;
        if (StringUtil.isEmpty(config)) {
            obj = new JSONObject();
        } else {
            obj = JSON.parseObject(config);
        }

        if (!obj.containsKey(GAMES)) {
            //第一次增加
            GameBindInfo info = new GameBindInfo(id, name, mainicon, prourl, 0,
                roleid);
            JSONObject sub = JSON.parseObject(info.toString());
            JSONArray array = new JSONArray();
            array.add(sub);

            obj.put(GAMES, array);
        } else {
            //增加其它游戏
            JSONArray array = obj.getJSONArray(GAMES);

            for (int i = 0; i < array.size(); i++) {
                JSONObject temp = (JSONObject) array.get(i);
                if (temp.getShort(GAMEID) == id) {
                    //增加时，如果已经存在了，只需要把绑定状态开启，状态开启即可

                    array.remove(i);
                    //解绑

                    //重新增加
                    GameBindInfo info = new GameBindInfo(id, name, mainicon,
                        prourl, 0, roleid);
                    temp = JSON.parseObject(info.toString());
                    array.add(temp);
                    obj.put(GAMES, array);

                    this.setConfig(obj.toJSONString());
                    //直接返回成功
                    return;
                }
            }

            //原来没有，则需要重新增加
            GameBindInfo info = new GameBindInfo(id, name, mainicon, prourl, 0,
                roleid);
            JSONObject sub = JSON.parseObject(info.toString());
            array.add(sub);

            obj.put(GAMES, array);
        }

        this.setConfig(obj.toJSONString());
    }

    /**
     * 删除某个用户绑定的一个游戏
     * 
     * @param id
     *            游戏id(预先分配)
     */
    public void removeGameData(Short id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }

        JSONObject obj = null;
        if (StringUtil.isEmpty(config)) {
            return;
        } else {
            obj = JSON.parseObject(config);
        }

        if (obj.containsKey(GAMES)) {
            //删除其它游戏
            JSONArray array = obj.getJSONArray(GAMES);
            for (int i = 0; i < array.size(); i++) {
                JSONObject temp = (JSONObject) array.get(i);
                if (temp.getShort(GAMEID) == id) {
                    array.remove(i);
                    //解绑

                    GameBindInfo info = JSON.toJavaObject(temp,
                        GameBindInfo.class);
                    info.setBindGame(false);
                    temp = JSON.parseObject(info.toString());
                    array.add(temp);
                    break;
                }
            }

            obj.put(GAMES, array);
        }

        this.setConfig(obj.toJSONString());
    }

    /**
     * 修改game true状态 status开启消息，false关闭
     * 
     * @param id
     * @param status
     */
    public void alterGameStatus(Short id, boolean status) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }

        JSONObject obj = null;
        if (StringUtil.isEmpty(config)) {
            return;
        } else {
            obj = JSON.parseObject(config);
        }

        if (obj.containsKey(GAMES)) {
            JSONArray array = obj.getJSONArray(GAMES);
            for (int i = 0; i < array.size(); i++) {
                JSONObject temp = (JSONObject) array.get(i);
                if (temp.getShort(GAMEID) == id) {
                    array.remove(i);
                    //解绑
                    GameBindInfo info = JSON.toJavaObject(temp,
                        GameBindInfo.class);
                    info.setGameMsgNotify(status);
                    temp = JSON.parseObject(info.toString());
                    array.add(temp);
                    break;
                }
            }

            obj.put(GAMES, array);
        }

        this.setConfig(obj.toJSONString());
    }

    /**
     * 跟据游戏gameid，获取对应的GameBindInfo信息
     * 
     * @param gameid
     * @return
     */
    public GameBindInfo getGameBindInfo(Short gameid) {
        if (gameid == null) {
            throw new NullPointerException("id is null");
        }

        JSONObject obj = null;
        if (StringUtil.isEmpty(config)) {
            return null;
        } else {
            obj = JSON.parseObject(config);
        }

        if (obj.containsKey(GAMES)) {
            JSONArray array = obj.getJSONArray(GAMES);
            for (int i = 0; i < array.size(); i++) {
                JSONObject temp = (JSONObject) array.get(i);
                if (temp.getShort(GAMEID) == gameid) {
                    array.remove(i);
                    //解绑
                    GameBindInfo info = JSON.toJavaObject(temp,
                        GameBindInfo.class);
                    return info;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "YixinUinfo [id=" + id + ", yid=" + yid + ", ecpId=" + ecpId
            + ", mailId=" + mailId + ", mobile=" + mobile + ", nick=" + nick
            + ", signature=" + signature + ", icon=" + icon + ", sex=" + sex
            + ", birthday=" + birthday + ", city=" + city + ", socialAddr="
            + socialAddr + ", bits=" + bits + ", timetag=" + timetag
            + ", validFlag=" + validFlag + ", config=" + config + ", email="
            + email + ", bkImage=" + bkImage + "]";
    }

    @Override
    public String toJSONString() {
        JSONObject bitsJo = new JSONObject();
        bitsJo.put("closeReadReceipt",
            isConfigValid(ConfigBitsMask.CLOSE_READ_RECEIPT));
        bitsJo.put("canBeSearchedByYid",
            !isConfigValid(ConfigBitsMask.CANNOT_BE_SEARCHED_BY_YID));
        bitsJo.put("canBeSearchedByMobile",
            !isConfigValid(ConfigBitsMask.CANNOT_BE_SEARCHED_BY_MOBILE));
        bitsJo.put("checkBoundPhoneMail",
            isConfigValid(ConfigBitsMask.CHECK_BOUND_PHONE_MAIL));
        bitsJo.put("closeMapLocation",
            isConfigValid(ConfigBitsMask.CLOSE_MAP_LOCATION));

        JSONObject jo = toJSONObject();
        jo.put("bits_detail", bitsJo);
        return JSON.toJSONString(jo, true);
    }

    public boolean isConfigValid(Long mask) {
        return (bits & mask) == mask;
    }

    public static boolean isConfigValid(Long bits, Long mask) {
        return (bits & mask) == mask;
    }

    public String getBkImage() {
        return bkImage;
    }

    public void setBkImage(String bkImage) {
        this.bkImage = bkImage;
    }

    /***
     * 设置bits对应位的值(正常是客户端值设置，此方法只为纯服务端修改调用，如标记用户是否做过邮箱自动绑定配制：
     * CHECK_BOUND_PHONE_MAIL)
     * 
     * @param flagValue
     *            true设置对应的位为0，false设置对应的位为1
     * @param bitsId
     *            对应的ID
     */
    public void setBitsValue(boolean flagValue, long bitsId) {
        if (!flagValue) {
            bits |= bitsId;
        } else {
            bits &= ~bitsId;
        }
    }

    public static void main(String[] args) {

        YixinUinfo u = YixinUinfo.getNullInstance();
        u.setBits(0L);
        System.out.println("default 0:"
            + u.isConfigValid(ConfigBitsMask.CHECK_BOUND_PHONE_MAIL));

        u.setBitsValue(false, ConfigBitsMask.CHECK_BOUND_PHONE_MAIL);
        System.out.println("after set 1:"
            + u.isConfigValid(ConfigBitsMask.CHECK_BOUND_PHONE_MAIL) + " v:"
            + u.getBits());

        u.setBitsValue(true, ConfigBitsMask.CHECK_BOUND_PHONE_MAIL);
        System.out.println("after set 0:"
            + u.isConfigValid(ConfigBitsMask.CHECK_BOUND_PHONE_MAIL));
        u.setBits(128L);
        System.out.println(u.toString());

        String key = "testconfig";
        String value = "123";
        u.addConfigData(key, value);
        System.out.println(u.toString());

        u = YixinUinfo.getNullInstance();
        u.setConfig("");

        Short id = 50;
        String name = "倩女幽魂";

        String mainicon = "http://nos.netease.com/yixinpublic/qn2logo.png";
        String proiconurl = "http://nos.netease.com/yixinpublic/";
        u.addGameData(id, name, mainicon, proiconurl, "xxxx");
        System.out.println(u.toString());

        id = 51;
        name = "大话西游";
        u.addGameData(id, name, mainicon, proiconurl, "xxxx");
        System.out.println(u.toString());

        u.removeGameData(id);
        u.alterGameStatus(id, false);
        System.out.println(u.toString());
    }

}
