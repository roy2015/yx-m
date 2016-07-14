package cn.zj.easynet.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSON;

import cn.zj.easynet.device.http.HttpClientPool;

public class AreaCodeUtil {
	public static String getAreaCode4PhoneNumber(String httpUrl, String phoneNbr){
		String resp="";
		
		try {
			resp = HttpClientPool.getInstance().getMethod(httpUrl + phoneNbr, 10000);
		} catch (Exception e) {
			resp = "{\"hcode\":\""+ phoneNbr +"\","
					+ "\"message\":\""+ e.getMessage() +"\","
					+ "\"areaCode\":\"-1\"}";
		}
		
		return (String)JSON.parseObject(resp).get("areaCode");
	}
}
