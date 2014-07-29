package com.linin.utils;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * popupwindow�Ĺ�����
 * 
 * @author linin
 */
public class PopUtil {
	
	/**
	 * ����һ�������Զ���view��PopupWindow
	 */
	public static PopupWindow getPop(Context cx, View popView, int width,
			int height) {
		PopupWindow window;
		window = new PopupWindow(cx);

		window.setContentView(popView);
		window.setWidth(width);
		window.setHeight(height);

		// ����PopupWindow�ⲿ�����Ƿ�ɴ���
		window.setFocusable(true); // ����PopupWindow�ɻ�ý���
		window.setTouchable(true); // ����PopupWindow�ɴ���
		window.setOutsideTouchable(true); // ���÷�PopupWindow����ɴ���
		return window;
	}

}
