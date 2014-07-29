package com.linin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.linin.java.NS;

/**
 * ʱ�乤����
 * 
 * @author linin
 * 
 */
public class TimeUtil {

	/**
	 * ����ʱ���longֵתΪString
	 */
	public static String getTime(String pattern, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(time));
	}

	/**
	 * ��ȡ��ǰʱ�䣨��ʽ��yyyy-MM-dd hh:mm:ss��
	 */
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd hh:mm:ss");
	}

	/**
	 * ��ȡ��ǰʱ��
	 */
	public static String getCurrentTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * ��ȡ��ǰ���
	 */
	public static int getYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * ��ȡ��ǰ�·�
	 */
	public static int getMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * ��ȡ��ǰ����
	 */
	public static int getDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * ��ȡ��ǰСʱ
	 */
	public static int getHour() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * ��ȡ��ǰ����
	 */
	public static int getMinute() {
		SimpleDateFormat sdf = new SimpleDateFormat("mm");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * ��ȡ��ǰ����
	 */
	public static int getSecond() {
		SimpleDateFormat sdf = new SimpleDateFormat("ss");
		return NS.toInt(sdf.format(new Date(System.currentTimeMillis())));
	}

	/**
	 * �õ�ָ���µ�����
	 * */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// ����������Ϊ���µ�һ��
		a.roll(Calendar.DATE, -1);// ���ڻع�һ�죬Ҳ�������һ��
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static int getMonthLastDay(int month) {
		return getMonthLastDay(getYear(), month);
	}

	/**
	 * ��ȡĳһ���ǰһ�죬������ʽyyyy-MM-dd
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
	 * ��ȡĳһ�µ�ǰһ�£�������ʽyyyy-MM
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
		if (getDay() > 8) {// 8��֮��
			return getPreMonth(getYear(), getMonth());
		} else {// 8��֮ǰ
			String preMonth = getPreMonth(getYear(), getMonth());
			int year = NS.toInt(preMonth.split("-")[0]);
			int month = NS.toInt(preMonth.split("-")[1]);
			return getPreMonth(year, month);
		}
	}

	/**
	 * �Ƿ��ڵ�ǰ����֮��
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

	/** ��ʼ��ʱ */
	public static void start() {
		t = System.currentTimeMillis();
	}

	/** ������ʱ */
	public static float end(String tag) {
		long f = System.currentTimeMillis() - t;
		L.d(tag + "->��ʱ(ms)��" + f);
		return f;
	}

	/**
	 * �ַ���ת����ʱ���ʽ����
	 * 
	 * @param dateStr
	 *            ��Ҫת�����ַ���
	 * @param formatStr
	 *            ��Ҫ��ʽ��Ŀ���ַ��� ���� yyyy-MM-dd
	 * @return Date ����ת�����ʱ��
	 * @throws ParseException
	 *             ת���쳣
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
