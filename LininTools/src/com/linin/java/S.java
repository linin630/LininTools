package com.linin.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linin
 *
 */
public class S {

	/**
	 * �����ж������ַ����Ƿ�һ�����������ô����ᱨ��ָ���쳣����������null���ص���true
	 */
	public static boolean equals(String s1, String s2) {
		if (s1 != null) {
			return s1.equals(s2);
		} else if (s2 != null) {
			return s2.equals(s1);
		} else {// �������ǿյ�ʱ�򣬷���true
			return true;
		}
	}

	/**
	 * �����ַ��������и�������("123456","2","5")�᷵��"34"
	 * ע�⣺������ظ�����ȡ��һ�����������ģ�������("12323","1","3")�᷵��"2"
	 */
	public static String cut(String source, String start, String end) {
		if (source.contains(start) && source.contains(end)) {
			source = source.split(start)[1].split(end)[0];
		}
		return source;
	}

	/**
	 * ������������ַ���
	 */
	public static String filter(String str) {
		// ֻ������ĸ������
		// String regEx = "[^a-zA-Z0-9]";
		// ��������������ַ�
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]";
		return filter(str, regEx);
	}

	/**
	 * ����������ʽ�����ַ���
	 */
	public static String filter(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
