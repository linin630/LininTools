package com.linin.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author linin
 *
 */
public class StreamUtil {

	public static String readStream(InputStream is) {
		String s = null;
		try {
			int b;
			// ˳���ȡ�ļ�text������ݲ���ֵ�����ͱ���b,ֱ���ļ�����Ϊֹ��
			StringBuffer sb = new StringBuffer();
			while ((b = is.read()) != -1) {
					sb.append((char) b);
			}
			s = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String readStream(InputStream is, String charsetName) {
		String txtStr = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					charsetName));
			String row;
			while ((row = br.readLine()) != null) {
				txtStr = txtStr + row;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return txtStr;
	}

}
