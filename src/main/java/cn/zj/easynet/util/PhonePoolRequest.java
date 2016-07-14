package cn.zj.easynet.util;

import org.apache.commons.lang3.StringUtils;


public class PhonePoolRequest {
	private String code;//一号通号码
	private String cityCode;//区号
	private String channelCode;//渠道编码
	private String uids;//易信ID串 ‘_’隔开
	
	private Double userId;


    public Long getUserId() {
        if (userId != null)
            return userId.longValue();
        return null;
    }

    public void setUserId(Double userId) {
        this.userId = userId;
    }

    public void setUserId(Long userId) {
        if (userId != null)
            this.userId = userId.doubleValue();
    }

    public void setUserId(String userId) {
        if (!StringUtils.isBlank(userId))
            this.userId = Double.valueOf(userId);
    }
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getUids() {
		return uids;
	}
	public void setUids(String uids) {
		this.uids = uids;
	}
	
	
}
