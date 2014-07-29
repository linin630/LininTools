package com.linin.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * ÆÁÄ»¹¤¾ßÀà
 * @author linin
 *
 */
public class ScreenUtil {
	
	public static int width = 480, height = 800;

	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		height = dm.heightPixels;
		return dm.heightPixels;
	}

}
