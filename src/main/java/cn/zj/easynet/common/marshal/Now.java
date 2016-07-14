package cn.zj.easynet.common.marshal;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Now {
	
	static ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>();
	
	private static ThreadLocal<SimpleDateFormat> formatter2 = new ThreadLocal<SimpleDateFormat>();

	public static String getNowString() {
		return Long.toString((new java.util.Date()).getTime() / 1000);
	}

	public static int getNow() {
		return (int) ((new java.util.Date()).getTime() / 1000);
	}

	public static long getTime() {
		return (new java.util.Date()).getTime() / 1000;
	}

	public static long getNow_millisecond() {
		return (new java.util.Date()).getTime();
	}

	public static String getDatetime() {
		return getSdf1().format(new java.util.Date());
	}

	/*
	 * 返回格式2指定的时间类型yyyyMMddHHmmss
	 */
	public static String getDatetime2() {
		return (getSdf2().format(new java.util.Date()));

	}

	public static String monthsAfter(int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, months);
		return getSdf1().format(calendar.getTime());

	}

	public static String daysAfter(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return getSdf1().format(calendar.getTime());

	}

	/**
	 * 按小时调整时间
	 * 
	 * @param hours
	 *            前进/后退小时数
	 * @return yy-MM-dd
	 */
	public static String hoursAfter(int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return getSdf1().format(calendar.getTime());

	}

	public static void main(String... strings) {
		System.out.println(daysAfter(30));
	}
	
	
	private static SimpleDateFormat getSdf1(){
		SimpleDateFormat sdf = formatter.get();
		if(sdf == null){
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatter.set(sdf);
		}
		return sdf;
	}
	private static SimpleDateFormat getSdf2(){
		SimpleDateFormat sdf = formatter2.get();
		if(sdf == null){
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			formatter2.set(sdf);
		}
		return sdf;
	}
}
