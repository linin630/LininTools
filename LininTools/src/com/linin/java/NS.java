package com.linin.java;

import java.text.DecimalFormat;

/**
 * 将字符串转换成数字的工具类
 * 
 * @author linin
 * 
 */
public class NS {

	public static int toInt(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public static float toFloat(String s) {
		float i = 0;
		try {
			i = Float.parseFloat(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * 保留几位小数的方法，pattern传入.00之类的
	 */
	public static float toFloat(String s, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		float f = toFloat(df.format(toDouble(s)));
		int i = (int) f;
		if (f * 10000 == i * 10000) {
			return i;
		}
		return f;
	}

	public static double toDouble(String s) {
		double i = 0;
		try {
			i = Double.parseDouble(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/** 根据长度length把该数字变为0001这样的形式，以字符串返回 */
	public static String toString(String number, int length) {
		while (number.length() < length) {
			number = "0" + number;
		}
		return number;
	}

	/** 给数字加上逗号，例：3,333.33 */
	public static String addDou(String num) {
		String result = "";
		String rest = "";
		if (num.contains(".")) {
			num = num.replace(".", "=");
			rest = "." + num.split("=")[1];
			num = num.split("=")[0];
		}
		int j = 0;
		for (int i = num.length() - 1; i >= 0; i--) {
			result = num.charAt(i) + result;
			j++;
			if (j >= 3) {
				j = 0;
				result = "," + result;
			}
		}
		if (result.startsWith(",")) {
			result = result.substring(1, result.length());
		}
		if (result.startsWith("-,")) {
			result = result.replace("-,", "-");
		}
		if (toInt(rest.replace(".", "")) == 0) {
			return result;
		}
		return result + rest;
	}

	/** 是否整数 */
	public static boolean isInt(String num) {
		int i = -1;
		try {
			i = Integer.parseInt(num);
		} catch (Exception e) {
			return false;
		}
		return i == toFloat(num);
	}

	/**
	 * 比较两个数的大小
	 * 
	 * @return true n1>n2 ; false n1<=n2
	 */
	public static boolean compare(String n1, String n2) {
		return toFloat(n1) > toFloat(n2);
	}

	public static String add(String... num) {
		float f = 0;
		for (int i = 0; i < num.length; i++) {
			f += toFloat(num[i]);
		}
		return toFloat(f + "", ".00") + "";
	}

	public static String getStringNum(String str) {
		str = str.trim();
		String str2 = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		return str2;
	}

}
