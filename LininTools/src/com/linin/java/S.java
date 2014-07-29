package com.linin.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linin
 *
 */
public class S {

	/**
	 * 用于判断两个字符串是否一样，并且做好处理不会报空指针异常，传入两个null返回的是true
	 */
	public static boolean equals(String s1, String s2) {
		if (s1 != null) {
			return s1.equals(s2);
		} else if (s2 != null) {
			return s2.equals(s1);
		} else {// 两个都是空的时候，返回true
			return true;
		}
	}

	/**
	 * 根据字符串进行切割，如果传入("123456","2","5")会返回"34"
	 * 注意：如果有重复，会取第一个符合条件的，即传入("12323","1","3")会返回"2"
	 */
	public static String cut(String source, String start, String end) {
		if (source.contains(start) && source.contains(end)) {
			source = source.split(start)[1].split(end)[0];
		}
		return source;
	}

	/**
	 * 清除所有特殊字符串
	 */
	public static String filter(String str) {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		return filter(str, regEx);
	}

	/**
	 * 利用正则表达式处理字符串
	 */
	public static String filter(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
