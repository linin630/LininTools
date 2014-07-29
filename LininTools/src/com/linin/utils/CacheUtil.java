package com.linin.utils;

import android.support.v4.util.LruCache;

/**
 * ���湤����
 * 
 * @author linin
 * 
 */
public class CacheUtil {

	private static CacheUtil cm = null;

	private LruCache<Object, Object> cache;

	private CacheUtil() {
		// ��ȡϵͳ�����ÿ��Ӧ�ó��������ڴ棬ÿ��Ӧ��ϵͳ����32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// ����Ĭ�ϸ����� 1/8 ���ڴ棬Ҳ����4M
		int maxSize = maxMemory / 8;
		cache = new LruCache<Object, Object>(maxSize);
	}

	public static CacheUtil init() {
		if (cm == null) {
			cm = new CacheUtil();
		}
		return cm;
	}

	/**
	 * ���û��������ڴ棬����ϵͳ��ÿ��Ӧ�÷�����ڴ���32M��������ñ𳬹�32
	 */
	public void setMaxSize(int maxSize) {
		cache = new LruCache<Object, Object>(maxSize);
	}

	public void addCache(Object key, Object value) {
		cache.put(key, value);
	}

	public Object getCache(Object key) {
		return cache.get(key);
	}

}
