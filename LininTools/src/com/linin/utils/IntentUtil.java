package com.linin.utils;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;

/**
 * intent相关工具类
 * 
 */
public class IntentUtil {

	/**
	 * 启动activity
	 */
	public static void start_activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		if (name != null) {
			for (int i = 0; i < name.length; i++) {
				intent.putExtra(name[i].getName(), name[i].getValue());
			}
		}
		activity.startActivity(intent);
	}

	public static void start_service(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		if (name != null) {
			for (int i = 0; i < name.length; i++) {
				intent.putExtra(name[i].getName(), name[i].getValue());
			}
		}
		activity.startService(intent);
	}

}
