package com.linin.java;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ����ȡ������Ĺ�����
 * 
 * @author linin
 * 
 */
public class Random {

	/**
	 * ����һ�� 0~2147483647 ����������ƱȽ����õ���
	 */
	public static int getInt() {
		return (int) (Math.random() * Integer.MAX_VALUE);
	}

	/**
	 * ����һ�� 0~9223372036854775807 ����������ƱȽ����õ���
	 */
	public static long getLong() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	/**
	 * ����һ�����ڵ���0��С��size�������
	 */
	public static int getInt(int size) {
		return getInt(0, size);
	}

	/**
	 * ����һ�����ڵ���a��С��b�������
	 * 
	 * @ע�⣺���a��b��С���� ���ص��� ����a��С�ڵ���b �������
	 * @ע�⣺���aС���㡢b������ ���ص��� ����a��С��b �������
	 */
	public static int getInt(int a, int b) {
		return (int) ((Math.random() * (b - a)) + a);
	}

	/**
	 * ��Map��ȡ�����һ������
	 */
	public static Object getSetValue(Set<?> set) {
		int i = getInt(set.size());
		for (Object s : set) {
			i--;
			if (i <= 0) {
				return s;
			}
		}
		return null;
	}

	/**
	 * ��Map��ȡ�����һ��Keyֵ
	 */
	public static Object getMapKey(Map<?, ?> map) {
		return getSetValue(map.keySet());
	}

	/**
	 * ��Map��ȡ�����һ��Valueֵ
	 */
	public static Object getMapValue(Map<?, ?> map) {
		return map.get(getMapKey(map));
	}

	/**
	 * ��List��ȡ�����һ������
	 */
	public static Object getListValue(List<?> list) {
		return list.get(getInt(list.size()));
	}

}
