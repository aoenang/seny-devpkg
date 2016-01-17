package org.senydevpkg.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 *
 */
public class DateUtils {
	/**
	 * 将long得到-- 小时:分
	 * @param lefttime
	 * @return 小时:分
	 */
	public static String formatTimeSimple(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	public static String formatTime(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	/**
	 * 得到: 年-月-日 小时:分钟
	 * @param lefttime
	 * @return 年-月-日 小时:分钟
	 */
	public static String formatDateAndTime(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	
	/**
	 * 得到: 年-月-日
	 * @param lefttime
	 * @return 年-月-日
	 */
	public static String formatDate(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	
	/**
	 * 字符串转为long
	 * @param time 字符串时间,注意:格式要与template定义的一样
	 * @param template 要格式化的格式:如time为09:21:12那么template为"HH:mm:ss"
	 * @return long
	 */
	public static long formatToLong(String time,String template) {
		SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.CHINA);
		try {
			Date d = sdf.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			long l = c.getTimeInMillis();
			return l;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * 得到年份
	 * @param lefttime
	 * @return 得到年份
	 */
	public static int formatYear(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		int i = Integer.parseInt(sDateTime);
		return i;
	}
	/**
	 * 得到月份
	 * @param lefttime
	 * @return 得到月份
	 */
	public static int formatMonth(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		int i = Integer.parseInt(sDateTime);
		return i;
	}
}
