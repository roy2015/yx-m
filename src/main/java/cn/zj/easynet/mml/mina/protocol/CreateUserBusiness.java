package cn.zj.easynet.mml.mina.protocol;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zj.easynet.mml.mina.protocol.ZteBean;
import cn.zj.easynet.util.Util;

import com.alibaba.fastjson.JSONObject;

public class CreateUserBusiness {
	private static final Logger logger = LoggerFactory.getLogger(CreateUserBusiness.class);

	static String login;
	static String domain;
	static String businessPhone;
	static String companyId;
	static String realname;
	static String password;
	static String preFwareId;
	static String contactUrl;
	static String channelId;
	static String version;
	static String numberType;
	static String itFlag;
	static String billName;
	static String serialNo;
	static String cmpyName;
	static String nickName;
	static String mobile1;
	static String mobile2;
	static String homeTel;
	static String officeTel;
	static String address;
	static String postCode;
	static String describe;
	static String businessType;
	static String ip;
	static String port;
	static String marketChannel;
	static String userSubType;
	static String contract;
	static String messageType;
	static String messageCode;
	static String batchType;
	static String edition;
	static String faxMode;
	static String id;

	public static ZteBean doZteCreate( JSONObject jsonReq ){
	
		logger.debug("开户开始...");
		logger.info("OPINFO["+jsonReq.toString()+"]");
		
		id = jsonReq.getString("id");
		
		JSONObject jsonReqInside = JSONObject.parseObject(jsonReq.getString("entry"));
	
		//重组指令
		String new_opinfo = "CREATE ECP USER:";
		String temp = "";
		temp = jsonReqInside.getString("LOGIN");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += "LOGIN=" + temp;
		}
		
		temp = jsonReqInside.getString("DOMAIN");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",DOMAIN=" + temp;
		}
		
		temp = jsonReqInside.getString("BUSINESSPHONE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",BUSINESSPHONE=" + temp;
		}
	
		temp = jsonReqInside.getString("CMPYID");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",CMPYID=" + temp;
		}
		
		temp = jsonReqInside.getString("REALNAME");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",REALNAME=" + temp;
		}
		
		temp = jsonReqInside.getString("PASSWORD");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",PASSWORD=" + temp;
		}
	
		temp = jsonReqInside.getString("PREFWAREID");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",PREFWAREID=" + temp;
		}
		
		temp = jsonReqInside.getString("CONTACTURL");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",CONTACTURL=" + temp;
		}
		
		temp = jsonReqInside.getString("CHANNELID");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",CHANNELID=" + temp;
		}
	
		temp = jsonReqInside.getString("VERSION");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",VERSION=" + temp;
		}
		
		temp = jsonReqInside.getString("NUMBER_TYPE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",NUMBER_TYPE=" + temp;
		}
		
		temp = jsonReqInside.getString("IT_FLAG");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",IT_FLAG=" + temp;
		}
	
		temp = jsonReqInside.getString("BILL_NAME");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",BILL_NAME=" + temp;
		}
		
		temp = jsonReqInside.getString("SERIALNO");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",SERIALNO=" + temp;
		}
		
		temp = jsonReqInside.getString("CMPYNAME");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",CMPYNAME=" + temp;
		}
	
		temp = jsonReqInside.getString("NICKNAME");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",NICKNAME=" + temp;
		}
		
		temp = jsonReqInside.getString("MOBILE1");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",MOBILE1=" + temp;
		}
		
		temp = jsonReqInside.getString("MOBILE2");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",MOBILE2=" + temp;
		}
	
		temp = jsonReqInside.getString("HOMEPHONE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",HOMEPHONE=" + temp;
		}
		
		temp = jsonReqInside.getString("WORKPHONE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",WORKPHONE=" + temp;
		}
		
		temp = jsonReqInside.getString("ADDRESSS");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",ADDRESSS=" + temp;
		}
	
		temp = jsonReqInside.getString("POSTCODE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",POSTCODE=" + temp;
		}
		
		temp = jsonReqInside.getString("DESCRIBE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",DESCRIBE=" + temp;
		}
		
		temp = jsonReqInside.getString("BUSINESS_TYPE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",BUSINESS_TYPE=" + temp;
		}
	
		temp = jsonReqInside.getString("IP");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",IP=" + temp;
		}
		
		temp = jsonReqInside.getString("PORT");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",PORT=" + temp;
		}
		
		temp = jsonReqInside.getString("MARKET_CHANNEL");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",MARKET_CHANNEL=" + temp;
		}
	
		temp = jsonReqInside.getString("USER_SUBTYPE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",USER_SUBTYPE=" + temp;
		}
		
		temp = jsonReqInside.getString("CONTRACT");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",CONTRACT=" + temp;
		}
		
		temp = jsonReqInside.getString("MESSAGE_TYPE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",MESSAGE_TYPE=" + temp;
		}
	
		temp = jsonReqInside.getString("MESSAGE_CODE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",MESSAGE_CODE=" + temp;
		}
		
		temp = jsonReqInside.getString("BATCH_TYPE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",BATCH_TYPE=" + temp;
		}
		
		temp = jsonReqInside.getString("EDITION");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",EDITION=" + temp;
		}
	
		temp = jsonReqInside.getString("FAX_MODE");
		if(!StringUtils.isBlank(temp)){
			new_opinfo += ",FAX_MODE=" + temp;
		}
		
		return new ZteBean(id,new_opinfo);
	}	
}
