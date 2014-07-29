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
			// 顺序读取文件text里的内容并赋值给整型变量b,直到文件结束为止。
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
