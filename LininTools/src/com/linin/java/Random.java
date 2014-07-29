package com.linin.java;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于取随机数的工具类
 * 
 * @author linin
 * 
 */
public class Random {

	/**
	 * 返回一个 0~2147483647 随机数（估计比较少用到）
	 */
	public static int getInt() {
		return (int) (Math.random() * Integer.MAX_VALUE);
	}

	/**
	 * 返回一个 0~9223372036854775807 随机数（估计比较少用到）
	 */
	public static long getLong() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	/**
	 * 返回一个大于等于0、小于size的随机数
	 */
	public static int getInt(int size) {
		return getInt(0, size);
	}

	/**
	 * 返回一个大于等于a、小于b的随机数
	 * 
	 * @注意：如果a、b都小于零 返回的是 大于a、小于等于b 的随机数
	 * @注意：如果a小于零、b大于零 返回的是 大于a、小于b 的随机数
	 */
	public static int getInt(int a, int b) {
		return (int) ((Math.random() * (b - a)) + a);
	}

	/**
	 * 从Map中取出随机一个对象
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
	 * 从Map中取出随机一个Key值
	 */
	public static Object getMapKey(Map<?, ?> map) {
		return getSetValue(map.keySet());
	}

	/**
	 * 从Map中取出随机一个Value值
	 */
	public static Object getMapValue(Map<?, ?> map) {
		return map.get(getMapKey(map));
	}

	/**
	 * 从List中取出随机一个对象
	 */
	public static Object getListValue(List<?> list) {
		return list.get(getInt(list.size()));
	}

}
