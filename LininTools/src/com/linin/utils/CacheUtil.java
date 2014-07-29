package com.linin.utils;

import android.support.v4.util.LruCache;

/**
 * 缓存工具类
 * 
 * @author linin
 * 
 */
public class CacheUtil {

	private static CacheUtil cm = null;

	private LruCache<Object, Object> cache;

	private CacheUtil() {
		// 获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// 这里默认给缓存 1/8 的内存，也就是4M
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
	 * 设置缓存的最大内存，由于系统给每个应用分配的内存是32M，所以最好别超过32
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
