package cn.zj.easynet.remote.dto;

public class EcpInfoResp extends RespBaseInfo{
	private String ecpAccount;
	private String realName;
	private String businessPhone;
	private String pass;
	private String validFlag;
	public String getEcpAccount() {
		return ecpAccount;
	}
	public void setEcpAccount(String ecpAccount) {
		this.ecpAccount = ecpAccount;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getBusinessPhone() {
		return businessPhone;
	}
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	
	
}
