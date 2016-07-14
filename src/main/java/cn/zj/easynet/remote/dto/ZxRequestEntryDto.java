package cn.zj.easynet.remote.dto;



import cn.zj.easynet.util.IConstants;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Jul 14, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ZxRequestEntryDto {
	
	/*@JSONField(name = "URL")
    private String url;// String 20 必需 　业务号码
*/	
	@JSONField(name = "LOGIN")
    private String login;// String 32 必需 登录名
	
	@JSONField(name = "DOMAIN")
    private String domain;// String 32 可选 用户域名，如不填该字段，表示该用户为普通ECP用户，接口机根据默认的域名开户
	
    @JSONField(name = "BUSINESSPHONE")
    private String businessPhone;// String 20 必需 　业务号码

    @JSONField(name = "CMPYID")
    private String companyId;// String 32 必需 　企业ID，由营业厅系统维护，ECP业务平台需要保存为企业的一个属性。以前的长度定义为12，修改为20。只能是数字和大小写字母
    
    @JSONField(name = "REALNAME")
    private String realname;// String 64 必需 真实姓名，在开通时作为用户的初始昵称
    
    @JSONField(name = "PASSWORD")
    private String password;// String 32 必需 登录密码
    
    @JSONField(name = "PREFWAREID")
    private String preFwareId;// String 32 可选需 优惠，多个优惠用“|“隔开    具体优惠号电信提供

    @JSONField(name = "CONTACTURL")
    private String contactUrl;// String 512 必需  优惠，合同（审核，采用链接地址方式）
    
    @JSONField(name = "CHANNELID")
    private String channelId;// String 64 必需 渠道
    
    @JSONField(name = "VERSION")
    private String version;// String 32 必需 　用户类型 1：学生；2：网吧；3：企业；4：家庭普通；5：WEB呼叫中心；0：其它
    
    @JSONField(name = "NUMBER_TYPE")
    private String numberType;//String 5 必需 号码类型: 0=虚号码,1=实号码
    
    @JSONField(name = "IT_FLAG")
    private String itFlag;//String 1 必需 it标识位: 0=浙江,1=全国
    
    @JSONField(name = "BILL_NAME")
    private String billName;//String 32 必需 用户开票名称：空
    
    @JSONField(name = "SERIALNO")
    private String serialNo;// String 32 必需 流水号
    
    @JSONField(name = "CMPYNAME")
    private String cmpyName;// String 50 可选 企业名称
    
    @JSONField(name = "NICKNAME")
    private String nickName;// String 16 可选 用户别名
    
    @JSONField(name = "MOBILE1")
    private String mobile1;// String 16 可选 移动电话1
    
    @JSONField(name = "MOBILE2")
    private String mobile2;// String 16 可选 移动电话2
    
    @JSONField(name = "HOMEPHONE")
    private String homeTel;// String 20 可选 家庭电话
    
    @JSONField(name = "WORKPHONE")
    private String officeTel;// String 20 可选 办公室电话，空则填入业务号码
    
    @JSONField(name = "ADDRESSS")
    private String address;// String 200 可选 用户住址
    
    @JSONField(name = "POSTCODE")
    private String postCode;// String 8 可选 邮政编码
    
    @JSONField(name = "DESCRIBE")
    private String describe;// String 64 可选 描述
    
    @JSONField(name = "BUSINESS_TYPE")
    private String businessType; // String 1 可选 用户功能类型（默认全业务）1:IM用户；2:全业务用户；3:试用
    
    @JSONField(name = "IP")
    private String ip; // String 20 可选 IP地址
    
    @JSONField(name = "PORT")
    private String port; // String 5 可选 端口
    
    @JSONField(name = "MARKET_CHANNEL")
    private String marketChannel; // String 16 可选 市场渠道标识
    
    @JSONField(name = "USER_SUBTYPE")
    private String userSubType; // String 5 可选 用户类型：    1:体验用户    2:付费用户    3:体验用户升级为付费用户
    
    @JSONField(name = "CONTRACT")
    private String contract; // String 50 可选  合同文件名
    
    @JSONField(name = "MESSAGE_TYPE")
    private String messageType; // String 1 可选  是否要发通知短信:    0:发默认短信    1:发送指定编码短信    2:否
    
    @JSONField(name = "MESSAGE_CODE")
    private String messageCode; // String 10 可选 短信发送内容编码
    
    @JSONField(name = "BATCH_TYPE")
    private String batchType; // String 1 可选 被开渠道开户类型（默认非即时开通）  1:即时开通；2:非即时开通
    
    @JSONField(name = "EDITION")
    private String edition; // String 4 可选 客户端版本
    
    @JSONField(name = "FAX_MODE")
    private String faxMode; // String 1 网络传真模式：    0：无 1：同号 2：异号

    
    
    
    public String getBusinessType() {
		return businessType;
	}

	public String getPreFwareId() {
		return preFwareId;
	}

	public void setPreFwareId(String preFwareId) {
		this.preFwareId = preFwareId;
	}

	public String getContactUrl() {
		return contactUrl;
	}

	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	public String getItFlag() {
		return itFlag;
	}

	public void setItFlag(String itFlag) {
		this.itFlag = itFlag;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCmpyName() {
		return cmpyName;
	}

	public void setCmpyName(String cmpyName) {
		this.cmpyName = cmpyName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile1() {
		return mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMarketChannel() {
		return marketChannel;
	}

	public void setMarketChannel(String marketChannel) {
		this.marketChannel = marketChannel;
	}

	public String getUserSubType() {
		return userSubType;
	}

	public void setUserSubType(String userSubType) {
		this.userSubType = userSubType;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getFaxMode() {
		return faxMode;
	}

	public void setFaxMode(String faxMode) {
		this.faxMode = faxMode;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	@JSONField(name = "CARDNUMBER")
    private String cardNumber;// String 50 可选  优惠，多个优惠用“|“隔开 具体优惠号电信提供

    @JSONField(name = "USERTYPE")
    private int userType;// numeric(3,0) 必需 　用户类型 1：学生；2：网吧；3：企业；4：家庭普通；5：WEB呼叫中心；0：其它
    @JSONField(name = "BUSINESSPACKAGE")
    private String businessPackage;// String 必需 套餐包类型
    
    
    @JSONField(name = "ECPCOMPANYID")
    private String ecpcompanyId;// String 20 必需 　ECP用户ID，由营业厅系统维护，ECP业务平台需要保存为企业的一个属性。以前的长度定义为12，修改为20。只能是数字和大小写字母
    
    @JSONField(name = "USERNAME")
    private String username;// String 64 必需 用户名，作昵称
    
   

    
    
    
    @JSONField(name = "VERSIONTYPE")
    private int versionType;// Int 必需 　版本类型0浙江版1网络版，2自助版、3定制版，4 EMA版
    @JSONField(name = "LANGUAGE")
    private int language;// numeric(1,0) 必需 语言1: 中文 2: 英文
    @JSONField(name = "STATE")
    private int state;// numeric(1,0) 必需 SoftDA用户状态1：正常使用 2：禁止使用4：用户暂停 5：停机报号6：未使用7：待验证
    @JSONField(name = "BIND_AUTHORIZATION")
    private int bindAuthorization;// numeric(1,0) 必需 绑定号码权限1: 网内, 2:市话权限3: 国内长途权限, 4: 国际长途权限 , 默认权限: 1
    @JSONField(name = "CALL_AUTHORIZATION")
    private int callAuthorization;// numeric(1,0) 必需 呼叫限制权限 1: 网内, 2: 市话权限, 3: 国内长途权限, 4: 国际长途权限 , 默认权限: 1
    @JSONField(name = "SOFTPHONETYPE")
    private int softphoneType;// numeric(1,0) 必需 终端类型0: 无权使用软终端电话, 1: 有权使用软号码池 2：有权使用固定软终端号码，默认：1
    @JSONField(name = "CHARGECARDTYPE")
    private int chargecardType;// numeric(1,0) 必需 记帐卡类型0: 后付费 1: 预付费
    @JSONField(name = "CARDLOCALNUM")
    private String cardlocalNum;// String 必需 记帐卡市话字冠，如杭州本地：0571
    @JSONField(name = "FEECONTROL")
    private int feeControl;// numeric(1,0) 必需 　用户话费是否敏感　０－不敏感 1-敏感
    
    
    @JSONField(name = "MOBILE")
    private String mobile;// String 20 可选 移动电话
    
    @JSONField(name = "MAILADDR")
    private String mailAddr;// String 64 可选 邮箱地址（废弃）
    
    
    @JSONField(name = "SOFTPHONENUMBER")
    private String softphoneNumber;// String 20 可选 软终端号码
    @JSONField(name = "SOFTPHONEPASSWORD")
    private String softphonePassword;// String 20 可选 软终端密码
    @JSONField(name = "GATEWAYIP")
    private String gatewayIp;// String 20 可选 网关ip地址
    @JSONField(name = "GATEWAYPORT")
    private String gatewayPort;// String 6 可选 端口号
    @JSONField(name = "CARDPREFIXID")
    private int cardpreFixid;// numeric(5,0) 可选 记帐卡前缀 默认0
    @JSONField(name = "CAPACITYFLAG")
    private String capacityFlag;
    /*- String  6   可选  　用户功能标记，每位用0～F表示，每位代表一个16进制数，转换成二进制，那位为1则表示有此功能，为0则表示无此功能。
    　第1位：
    　1000：具有呼叫能力
    　0100：具有电话会议能力
    　0010：预留
    　0001：预留
    　第2位：
    　预留
    　第3位：
    　预留
    　第4位：
    　预留
    　第5位：
    　1000：预留
    　0100：具有传真速递能力
    　0010：具有短信速递能力
    　0001：预留
    　第6位：
    　预留
     */

    @JSONField(name = "MAILPWD")
    private String mailPwd;// String 15 可选 　邮箱密码 默认为空（废弃）
    @JSONField(name = "CALLERDISPLAY")
    private String callerDisplay;// String 25 可选 　主叫号码，空则填入业务号码
    @JSONField(name = "PHS")
    private String phs;// String 20 可选 小灵通号码，
    @JSONField(name = "SMSPHONE")
    private String smsPhone;// String 20 可选 　短信接收号码，不能是自己或其他人的业务号码和短信显示号码,但可以是别人的短信接收号码
    @JSONField(name = "SMSDISPLAYPHONE")
    private String smsDisplayPhone;// String 20 可选 短信显示号码，如果为空则填业务号码,不能是别人的业务号码和短信显示号码
    @JSONField(name = "FAXPHONE")
    private String faxPhone;// String 20 可选 　传真总机号码
    @JSONField(name = "FAXEXTENSION")
    private String faxExtension;// String 20 可选 　传真分机号码，分机号可能没有。要求传真总机+分机在整个系统唯一
    @JSONField(name = "ECPVPNID")
    private String ecpVpnId;// String 32 可选 群ID,如果开户时没有填写，接口机填写0，表示不属于任何群
    @JSONField(name = "ECPVPNPHONE")
    private String ecpVpnPhone;// String 20 可选 群固话号码，用于判断群内通话，同时可作为初始的默认接听工作电话。企业超级管理员和用户都不能修改，只能通过CRM修改。要求与用户号码一致
    @JSONField(name = "ECPVPNMOBILE")
    private String ecpVpnMobile;// String 20 可选
    // 群手机号码：用于判断群内通话，同时可作为初始的默认接听工作手机号码。企业超级管理员和用户都不能修改，只能通过CRM修改。要求必须是中国电信手机号码。
    @JSONField(name = "DEFAULTWORKPHONE")
    private String defaultWorkPhone;// String 20 可选 默认工作电话
    @JSONField(name = "DEFAULTWORKMOBILE")
    private String defaultWorkMobile;// String 20 可选 默认工作手机
    @JSONField(name = "PHOTOINDEX")
    private String photoIndex;// String 128 可选 头像文件名（废弃）
    @JSONField(name = "OTHERADDRESS")
    private String otherAddress;// String 128 可选 个性签名（废弃）
    @JSONField(name = "FAX_AUTHORIZATION")
    private String faxAuthorization;// numeric(1,0) 可选 传真通话权限 1: 网内, 2: 市话权限, 3: 国内长途权限, 4: 国际长途权限 ,
    // 默认取CALL_AUTHORIZATION字段的值
    @JSONField(name = "SERVICE_SUM")
    private String serviceSum;// String 64 可选
    // 开通的业务能力，用竖线分隔，完整的业务能力包括SMS|CALL|FAX，分别表示短信、呼叫会议和传真业务能力。例如，只开通短信和呼叫会议时填写为SMS|CALL
    @JSONField(name = "ROLE_ID")
    private int roleId;// numeric(3,0) 可选 用户级别编号，从系统的用户级别类型中选择一个，若为0，或不携带该参数，则使用默认值
    
    @JSONField(name="PRIORITY")
    private int priority = IConstants.ZX_CMD_ENTRY_PRIORITY;//优先级：1，2，3，其中3优先级最高，如果前端不填该参数，系统默认优先级为

    /**
     * @return the businessPhone
     */
    public String getBusinessPhone() {
        return businessPhone;
    }

    /**
     * @param businessPhone
     *            the businessPhone to set
     */
    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber
     *            the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    /**
     * @return the userType
     */
    public int getUserType() {
        return userType;
    }

    /**
     * @param userType
     *            the userType to set
     */
    public void setUserType(int userType) {
        this.userType = userType;
    }

    /**
     * @return the businessPackage
     */
    public String getBusinessPackage() {
        return businessPackage;
    }

    /**
     * @param businessPackage
     *            the businessPackage to set
     */
    public void setBusinessPackage(String businessPackage) {
        this.businessPackage = businessPackage;
    }

    /**
     * @return the channelId
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            the channelId to set
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId
     *            the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the ecpcompanyId
     */
    public String getEcpcompanyId() {
        return ecpcompanyId;
    }

    /**
     * @param ecpcompanyId
     *            the ecpcompanyId to set
     */
    public void setEcpcompanyId(String ecpcompanyId) {
        this.ecpcompanyId = ecpcompanyId;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * @param realname
     *            the realname to set
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login
     *            the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the versionType
     */
    public int getVersionType() {
        return versionType;
    }

    /**
     * @param versionType
     *            the versionType to set
     */
    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }

    /**
     * @return the language
     */
    public int getLanguage() {
        return language;
    }

    /**
     * @param language
     *            the language to set
     */
    public void setLanguage(int language) {
        this.language = language;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return the bindAuthorization
     */
    public int getBindAuthorization() {
        return bindAuthorization;
    }

    /**
     * @param bindAuthorization
     *            the bindAuthorization to set
     */
    public void setBindAuthorization(int bindAuthorization) {
        this.bindAuthorization = bindAuthorization;
    }

    /**
     * @return the callAuthorization
     */
    public int getCallAuthorization() {
        return callAuthorization;
    }

    /**
     * @param callAuthorization
     *            the callAuthorization to set
     */
    public void setCallAuthorization(int callAuthorization) {
        this.callAuthorization = callAuthorization;
    }

    /**
     * @return the softphoneType
     */
    public int getSoftphoneType() {
        return softphoneType;
    }

    /**
     * @param softphoneType
     *            the softphoneType to set
     */
    public void setSoftphoneType(int softphoneType) {
        this.softphoneType = softphoneType;
    }

    /**
     * @return the chargecardType
     */
    public int getChargecardType() {
        return chargecardType;
    }

    /**
     * @param chargecardType
     *            the chargecardType to set
     */
    public void setChargecardType(int chargecardType) {
        this.chargecardType = chargecardType;
    }

    /**
     * @return the cardlocalNum
     */
    public String getCardlocalNum() {
        return cardlocalNum;
    }

    /**
     * @param cardlocalNum
     *            the cardlocalNum to set
     */
    public void setCardlocalNum(String cardlocalNum) {
        this.cardlocalNum = cardlocalNum;
    }

    /**
     * @return the feeControl
     */
    public int getFeeControl() {
        return feeControl;
    }

    /**
     * @param feeControl
     *            the feeControl to set
     */
    public void setFeeControl(int feeControl) {
        this.feeControl = feeControl;
    }


    /**
     * @return the officeTel
     */
    public String getOfficeTel() {
        return officeTel;
    }

    /**
     * @param officeTel
     *            the officeTel to set
     */
    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    /**
     * @return the homeTel
     */
    public String getHomeTel() {
        return homeTel;
    }

    /**
     * @param homeTel
     *            the homeTel to set
     */
    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
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
     * @return the mailAddr
     */
    public String getMailAddr() {
        return mailAddr;
    }

    /**
     * @param mailAddr
     *            the mailAddr to set
     */
    public void setMailAddr(String mailAddr) {
        this.mailAddr = mailAddr;
    }

    /**
     * @return the addrStreet
     */


    /**
     * @return the softphoneNumber
     */
    public String getSoftphoneNumber() {
        return softphoneNumber;
    }

    /**
     * @param softphoneNumber
     *            the softphoneNumber to set
     */
    public void setSoftphoneNumber(String softphoneNumber) {
        this.softphoneNumber = softphoneNumber;
    }

    /**
     * @return the softphonePassword
     */
    public String getSoftphonePassword() {
        return softphonePassword;
    }

    /**
     * @param softphonePassword
     *            the softphonePassword to set
     */
    public void setSoftphonePassword(String softphonePassword) {
        this.softphonePassword = softphonePassword;
    }

    /**
     * @return the gatewayIp
     */
    public String getGatewayIp() {
        return gatewayIp;
    }

    /**
     * @param gatewayIp
     *            the gatewayIp to set
     */
    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = gatewayIp;
    }

    /**
     * @return the gatewayPort
     */
    public String getGatewayPort() {
        return gatewayPort;
    }

    /**
     * @param gatewayPort
     *            the gatewayPort to set
     */
    public void setGatewayPort(String gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    /**
     * @return the cardpreFixid
     */
    public int getCardpreFixid() {
        return cardpreFixid;
    }

    /**
     * @param cardpreFixid
     *            the cardpreFixid to set
     */
    public void setCardpreFixid(int cardpreFixid) {
        this.cardpreFixid = cardpreFixid;
    }

    /**
     * @return the capacityFlag
     */
    public String getCapacityFlag() {
        return capacityFlag;
    }

    /**
     * @param capacityFlag
     *            the capacityFlag to set
     */
    public void setCapacityFlag(String capacityFlag) {
        this.capacityFlag = capacityFlag;
    }

    /**
     * @return the mailPwd
     */
    public String getMailPwd() {
        return mailPwd;
    }

    /**
     * @param mailPwd
     *            the mailPwd to set
     */
    public void setMailPwd(String mailPwd) {
        this.mailPwd = mailPwd;
    }

    /**
     * @return the callerDisplay
     */
    public String getCallerDisplay() {
        return callerDisplay;
    }

    /**
     * @param callerDisplay
     *            the callerDisplay to set
     */
    public void setCallerDisplay(String callerDisplay) {
        this.callerDisplay = callerDisplay;
    }

    /**
     * @return the phs
     */
    public String getPhs() {
        return phs;
    }

    /**
     * @param phs
     *            the phs to set
     */
    public void setPhs(String phs) {
        this.phs = phs;
    }

    /**
     * @return the smsPhone
     */
    public String getSmsPhone() {
        return smsPhone;
    }

    /**
     * @param smsPhone
     *            the smsPhone to set
     */
    public void setSmsPhone(String smsPhone) {
        this.smsPhone = smsPhone;
    }

    /**
     * @return the smsDisplayPhone
     */
    public String getSmsDisplayPhone() {
        return smsDisplayPhone;
    }

    /**
     * @param smsDisplayPhone
     *            the smsDisplayPhone to set
     */
    public void setSmsDisplayPhone(String smsDisplayPhone) {
        this.smsDisplayPhone = smsDisplayPhone;
    }

    /**
     * @return the faxPhone
     */
    public String getFaxPhone() {
        return faxPhone;
    }

    /**
     * @param faxPhone
     *            the faxPhone to set
     */
    public void setFaxPhone(String faxPhone) {
        this.faxPhone = faxPhone;
    }

    /**
     * @return the faxExtension
     */
    public String getFaxExtension() {
        return faxExtension;
    }

    /**
     * @param faxExtension
     *            the faxExtension to set
     */
    public void setFaxExtension(String faxExtension) {
        this.faxExtension = faxExtension;
    }

    /**
     * @return the ecpVpnId
     */
    public String getEcpVpnId() {
        return ecpVpnId;
    }

    /**
     * @param ecpVpnId
     *            the ecpVpnId to set
     */
    public void setEcpVpnId(String ecpVpnId) {
        this.ecpVpnId = ecpVpnId;
    }

    /**
     * @return the ecpVpnPhone
     */
    public String getEcpVpnPhone() {
        return ecpVpnPhone;
    }

    /**
     * @param ecpVpnPhone
     *            the ecpVpnPhone to set
     */
    public void setEcpVpnPhone(String ecpVpnPhone) {
        this.ecpVpnPhone = ecpVpnPhone;
    }

    /**
     * @return the ecpVpnMobile
     */
    public String getEcpVpnMobile() {
        return ecpVpnMobile;
    }

    /**
     * @param ecpVpnMobile
     *            the ecpVpnMobile to set
     */
    public void setEcpVpnMobile(String ecpVpnMobile) {
        this.ecpVpnMobile = ecpVpnMobile;
    }

    /**
     * @return the defaultWorkPhone
     */
    public String getDefaultWorkPhone() {
        return defaultWorkPhone;
    }

    /**
     * @param defaultWorkPhone
     *            the defaultWorkPhone to set
     */
    public void setDefaultWorkPhone(String defaultWorkPhone) {
        this.defaultWorkPhone = defaultWorkPhone;
    }

    /**
     * @return the defaultWorkMobile
     */
    public String getDefaultWorkMobile() {
        return defaultWorkMobile;
    }

    /**
     * @param defaultWorkMobile
     *            the defaultWorkMobile to set
     */
    public void setDefaultWorkMobile(String defaultWorkMobile) {
        this.defaultWorkMobile = defaultWorkMobile;
    }

    /**
     * @return the photoIndex
     */
    public String getPhotoIndex() {
        return photoIndex;
    }

    /**
     * @param photoIndex
     *            the photoIndex to set
     */
    public void setPhotoIndex(String photoIndex) {
        this.photoIndex = photoIndex;
    }

    /**
     * @return the otherAddress
     */
    public String getOtherAddress() {
        return otherAddress;
    }

    /**
     * @param otherAddress
     *            the otherAddress to set
     */
    public void setOtherAddress(String otherAddress) {
        this.otherAddress = otherAddress;
    }

    /**
     * @return the faxAuthorization
     */
    public String getFaxAuthorization() {
        return faxAuthorization;
    }

    /**
     * @param faxAuthorization
     *            the faxAuthorization to set
     */
    public void setFaxAuthorization(String faxAuthorization) {
        this.faxAuthorization = faxAuthorization;
    }

    /**
     * @return the serviceSum
     */
    public String getServiceSum() {
        return serviceSum;
    }

    /**
     * @param serviceSum
     *            the serviceSum to set
     */
    public void setServiceSum(String serviceSum) {
        this.serviceSum = serviceSum;
    }

    /**
     * @return the roleId
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     *            the roleId to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

	/*public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
*/
}

