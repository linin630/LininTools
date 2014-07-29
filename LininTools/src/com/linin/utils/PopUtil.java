package com.linin.utils;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * popupwindow的工具类
 * 
 * @author linin
 */
public class PopUtil {
	
	/**
	 * 创建一个包含自定义view的PopupWindow
	 */
	public static PopupWindow getPop(Context cx, View popView, int width,
			int height) {
		PopupWindow window;
		window = new PopupWindow(cx);

		window.setContentView(popView);
		window.setWidth(width);
		window.setHeight(height);

		// 设置PopupWindow外部区域是否可触摸
		window.setFocusable(true); // 设置PopupWindow可获得焦点
		window.setTouchable(true); // 设置PopupWindow可触摸
		window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		return window;
	}

}
