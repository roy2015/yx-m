package cn.zj.easynet.util;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class AuthCodeUtil {
	public static String generateAuthCode(){
		Random r = new Random();
		String authCode = r.nextInt(1000000) +"";
		String value = StringUtils.leftPad(authCode, 6, "0");
		return value;
	}
}
