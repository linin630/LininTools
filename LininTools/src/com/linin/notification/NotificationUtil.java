/**
 * 
 */
package com.linin.notification;

import android.app.Activity;

/**
 * @author linin
 *
 */
public class NotificationUtil {

	// public interface Listener {
	// void onNotificationClick();
	// }

	/**
	 * 显示一个通知
	 * 
	 * @param act
	 *            点击通知跳转到的activity
	 * @param iconRes
	 *            图标
	 * @param title
	 *            在导航栏显示的标题
	 * @param contentTitle
	 *            在通知栏的标题
	 * @param contentText
	 *            在通知栏的详情
	 * @param canClear
	 *            是否可以清除，传入false就是流氓
	 */
	public static void show(Activity act, int iconRes, String title,
			CharSequence contentTitle, CharSequence contentText,
			boolean canClear) {
		NotificationExtend noti = new NotificationExtend(act);
		noti.showNotification(iconRes, title, contentTitle, contentText,
				canClear);
	}

	// public static void show(Context context, int iconRes, String title,
	// CharSequence contentTitle, CharSequence contentText,
	// boolean canClear, Listener listener) {
	//
	// }

}
