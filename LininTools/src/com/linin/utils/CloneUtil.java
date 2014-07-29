package com.linin.utils;

import org.apache.http.client.utils.CloneUtils;

/**
 * @author linin
 *
 */
public class CloneUtil {

	public static Object clone(Object obj) {
		try {
			return CloneUtils.clone(obj);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
