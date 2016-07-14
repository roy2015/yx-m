package cn.zj.easynet.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class Util {

	public Util() {
	}

	/**
	 * 鑾峰彇瀹㈡埛绔湡瀹瀒p鍦板潃
	 * 
	 * @description
	 * @param request
	 * @return
	 * @author
	 * @date 2010-3-31
	 * @history
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	// 璇诲彇鎸囧畾鐨勪俊鎭�
	public static String readString(String ResourceBundleStr, String name) {
		String result = "";
		try {
			if (isEmpty(ResourceBundleStr)) {
				ResourceBundleStr = "mml";
			}
			ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleStr);
			result = rb.getString(name);
		} catch (Exception e) {
			System.out.println("参数" + name + "不存在!");
		}
		return result;
	}
	//获取关键字值
	public static String queryString(String param,String objectStr){
		if(objectStr.contains(param)){
			int startSite = objectStr.indexOf(param)+param.length();
			String dempStr = objectStr.substring(startSite);
			int endSite = (dempStr.indexOf(",")==-1)?objectStr.length()-startSite:dempStr.indexOf(",");
			return dempStr.substring(0, endSite);
		}else{
			return "";
		}
		
	}
	
	//获取相关位置的字符串，比如123456789,调用该函数时(3,3,"123456789"),获得的结果为456
	public static String queryStringFromSeat(int curSeat,int stringSize,String objectStr){
		String demp = objectStr.substring(curSeat, objectStr.length());
		return demp.substring(0, stringSize);
	}
	
	//判断是否为16进制的
	public static boolean judge16(String param){
		int result = Integer.parseInt(param, 16);
		if(result==1){
			return true;
		}else{
			return false;
		}
	}
	
	//获得包括中文的字符串长度
	public static int queryChineseNumber(int start,int end,String str){
		String demp = str.substring(start,end);
		if (demp == null)
			return 0;
		StringBuffer buff = new StringBuffer(demp);
		int chineseNumber = 0;
		String stmp;
		for (int i = 0; i < buff.length(); i++) {
			stmp = buff.substring(i, i + 1);
			try {
				stmp = new String(stmp.getBytes("utf-8"));
				} catch (Exception e) {
			    e.printStackTrace();
			   }
			if (stmp.getBytes().length > 1) {
				chineseNumber += 1;
			  }
			}
		  return chineseNumber;
	}
	
	//将16进程转变为10进制
	public static String into10(String param){
		return Integer.valueOf(param,16).toString();
	}
	
	//删除str中的某个关键字和值
	public static String deleteFromStr(String key,String objectStr){
		if(objectStr.contains(key)){
			int startSite = objectStr.indexOf(key);
			String firstStr = objectStr.substring(0, startSite);
			startSite += key.length();
			String secondStr = objectStr.substring(startSite);
			int endSite = secondStr.indexOf(",");
			secondStr = secondStr.substring(endSite, secondStr.length());
			return firstStr + secondStr;
		}else{
			return objectStr;
		}
	}

	public static boolean isEmpty(String string) {
		return (string == null || "".equalsIgnoreCase(string.trim()));
	}

	public static String getCurDate() {
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
		return yestedayDate;
	}
	
	public static String getCurrentDate() {
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        String  yestedayDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(calendar.getTime());
		return yestedayDate;
	}
	
	public static String eatSpace(String str){
		String resultStr = "";
		for (int i=0;i<str.length();i++){
			String temp = str.substring(i,i+1);
			if(temp.equals(" ")||temp==null){
				continue;
			}
			resultStr +=temp;
		}
		return resultStr;
	}
	
	public static String eatSign(String str,String signs){
		String resultStr = "";
		for (int i=0;i<str.length();i++){
			String temp = str.substring(i,i+1);
			if(temp.equals(signs)){
				continue;
			}
			resultStr +=temp;
		}

		return resultStr;
	}
	
	public static List sortDate(Set<String> object){
		List<String> list = new ArrayList<String>();
		for(String str:object){
			list.add(str);
		}
		Collections.sort(list);		
		return list;
	}
	
	//获取工单类型
	public static int GetOperType(String getsOpinfo) {
		String[] reqs =  getsOpinfo.split(":");
		if(reqs[0].equals("CREATE ODA USER")){
			return 0;
		}else if(reqs[0].equals("DELE ODA USER")){
			return 1;
		}else{
			return -1;
		}		
	}
	
	//获取随机密码
		public static String GetSrand_SIR(int pswdlength) {
			String pswd= "";
			String sFormat="01234567890123456789abcdefghijklmnopqrstuvwxyz";
			Random rand = new Random(); 
			for(int i=1;i<=pswdlength;i++){
				int randNum = rand.nextInt(10000);
				pswd += sFormat.substring(randNum%sFormat.length()-1, randNum%sFormat.length());
			}
			return pswd;	
		}

	public static void main(String[] args) {		
		String req= eatSign( eatSpace(getCurrentDate() ),":");
		//int a  = Util.queryChineseNumber( 0,3,req );
		Map<String,Integer> failedActionTimes = new HashMap<String,Integer>();
		failedActionTimes.put("1", 1);
		failedActionTimes.put("2", 2);
		failedActionTimes.put("3", 3);
		
		failedActionTimes.put("3", 100);
		
		System.out.println(failedActionTimes.get("3"));
	}
}
