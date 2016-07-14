package cn.zj.easynet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author qbk
 * @version 创建时间：2014年7月2日 上午9:39:43
 * 
 */
public class DateUtil {
	private static Date inputDate = null;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat dateFormatFull = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormatGeneral = new SimpleDateFormat(
			"yyyyMMddHHmmss");		
			
	public static SimpleDateFormat getGeneralFormat(){
		return dateFormatGeneral;
	}
	
	/**
	 * 获取当天日期的8天前的最小时间
	 */
	public static String get8Daysago() {
		return dateFormatFull.format(get8DaysagoDate());
	}

	/**
	 * 获取当天日期的8天前的最小时间
	 */
	public static Date get8DaysagoDate() {
		if (inputDate == null) {
			genDate();
		} else {
			if (new Date().getTime() - inputDate.getTime() > 24 * 60 * 60 * 1000) {
				genDate();
			}
		}

		return inputDate;
	}

	private static void genDate() {
		synchronized (DateUtil.class) {
			try {
				inputDate = dateFormat.parse(dateFormat.format(new Date()));
			} catch (ParseException e) {
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear - 8);
			inputDate = cal.getTime();
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(DateUtil.get8Daysago());
		System.out.println(dateFormat.parse("2014-05-07").getTime()
				- dateFormat.parse("2014-05-08").getTime());
		System.out.println(dateFormatFull.parse("2015-08-20 16:00:02").getTime());
	}
}
