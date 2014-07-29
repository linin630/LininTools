package com.linin.java;

import com.linin.utils.TimeUtil;

/**
 * 异或加密
 * 
 * @author linin
 * 
 */
public class Encrypt {

	/**
	 * 对字符串进行加密/解密
	 * 
	 * @param source
	 * @param code
	 * @return
	 */
	public static String code(String source, String code) {
		TimeUtil.start();
		byte[] bs = source.getBytes();
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte) ((bs[i]) ^ code.hashCode());
		}
		TimeUtil.end("异或加密/解密");
		return new String(bs);
	}

	/**
	 * 对int类型进行加密/解密
	 * 
	 * @param source
	 * @param code
	 * @return
	 */
	public static int code(int source, String code) {
		return source ^ code.hashCode();
	}

}
