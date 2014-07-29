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
	 * ��ʾһ��֪ͨ
	 * 
	 * @param act
	 *            ���֪ͨ��ת����activity
	 * @param iconRes
	 *            ͼ��
	 * @param title
	 *            �ڵ�������ʾ�ı���
	 * @param contentTitle
	 *            ��֪ͨ���ı���
	 * @param contentText
	 *            ��֪ͨ��������
	 * @param canClear
	 *            �Ƿ�������������false������å
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
