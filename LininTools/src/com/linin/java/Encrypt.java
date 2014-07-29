package com.linin.java;

import com.linin.utils.TimeUtil;

/**
 * ������
 * 
 * @author linin
 * 
 */
public class Encrypt {

	/**
	 * ���ַ������м���/����
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
		TimeUtil.end("������/����");
		return new String(bs);
	}

	/**
	 * ��int���ͽ��м���/����
	 * 
	 * @param source
	 * @param code
	 * @return
	 */
	public static int code(int source, String code) {
		return source ^ code.hashCode();
	}

}
