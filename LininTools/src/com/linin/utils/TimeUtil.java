package com.linin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.linin.java.NS;

/**
 * 时间工具类
 * 
 * @author linin
 * 
 */
public class TimeUtil {

	/**
	 * 根据时间的long值转为String
	 */
	public static String getTime(String pattern, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(time));
	}

	/**
	 * 获取当前时间（形式：yyyy-MM-dd hh:mm:ss）
	 */
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd hh:mm:ss");
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurrentTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 获取当前年份
	 */
	public static int getYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * 获取当前月份
	 */
	public static int getMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * 获取当前天数
	 */
	public static int getDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * 获取当前小时
	 */
	public static int getHour() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * 获取当前分钟
	 */
	public static int getMinute() {
		SimpleDateFormat sdf = new SimpleDateFormat("mm");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * 获取当前秒数
	 */
	public static int getSecond() {
		SimpleDateFormat sdf = new SimpleDateFormat("ss");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * 得到指定月的天数
	 * */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static int getMonthLastDay(int month) {
		return getMonthLastDay(getYear(), month);
	}

	/**
	 * 获取某一天的前一天，返回形式yyyy-MM-dd
	 */
	public static String getPreDay(int year, int month, int day) {
		day = day - 1;
		if (day == 0) {
			day = getMonthLastDay(year, month);
			return year + "-" + NS.toString((month - 1) + "", 2) + "-"
					+ NS.toString(day + "", 2);
		}
		return year + "-" + NS.toString(month + "", 2) + "-"
				+ NS.toString(day + "", 2);
	}

	public static String getPreDay(String time) {
		String[] times = time.split("-");
		int year = NS.toInt(times[0]);
		int month = NS.toInt(times[1]);
		int day = NS.toInt(times[2]);
		return getPreDay(year, month, day);
	}

	public static String getPreDay() {
		return getPreDay(getYear(), getMonth(), getDay());
	}

	/**
	 * 获取某一月的前一月，返回形式yyyy-MM
	 */
	public static String getPreMonth(int year, int month) {
		month = month - 1;
		if (month == 0) {
			month = 12;
			return (year - 1) + "-" + NS.toString(month + "", 2);
		}
		return year + "-" + NS.toString(month + "", 2);
	}

	public static String getPreMonth() {
		if (getDay() > 8) {// 8号之后
			return getPreMonth(getYear(), getMonth());
		} else {// 8号之前
			String preMonth = getPreMonth(getYear(), getMonth());
			int year = NS.toInt(preMonth.split("-")[0]);
			int month = NS.toInt(preMonth.split("-")[1]);
			return getPreMonth(year, month);
		}
	}

	/**
	 * 是否在当前日期之后
	 */
	public static boolean after(int year, int month, int day) {
		L.d("after-->year=" + year + ";month=" + month + ";day=" + day);
		return new Date(year, month, day).after(new Date(getYear(), getMonth(),
				getDay()));
	}

	public static boolean after(int year, int month) {
		return after(year, month, 1);
	}

	private static long t = 0;

	/** 开始计时 */
	public static void start() {
		t = System.currentTimeMillis();
	}

	/** 结束计时 */
	public static float end(String tag) {
		long f = System.currentTimeMillis() - t;
		L.d(tag + "->费时(ms)：" + f);
		return f;
	}

	/**
	 * 字符串转换到时间格式搜索
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
