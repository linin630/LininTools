package com.linin.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toastͳһ������
 * 
 * @author linin
 * 
 */
public class T {
	// Toast
	private static Toast toast;
	// �Զ���view
	private static View toastView;
	// �Զ���view��textview
	private static TextView textView;
	// �Ƿ��Զ���
	private static boolean isCustom = false;
	// �Ƿ�������λ��
	private static boolean hasSetGravity = false;
	private static int gravity, xOffset, yOffset;

	public final static int SHORT = Toast.LENGTH_SHORT;
	public final static int LONG = Toast.LENGTH_LONG;

	/**
	 * ��ʱ����ʾToast
	 */
	public static void showShort(Context context, CharSequence message) {
		show(context, message, SHORT);
	}

	/**
	 * ��ʱ����ʾToast
	 */
	public static void showShort(Context context, int message) {
		show(context, message, SHORT);
	}

	/**
	 * ��ʱ����ʾToast
	 */
	public static void showLong(Context context, CharSequence message) {
		show(context, message, LONG);
	}

	/**
	 * ��ʱ����ʾToast
	 */
	public static void showLong(Context context, int message) {
		show(context, message, LONG);
	}

	/**
	 * �Զ�����ʾToastʱ��
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isCustom) {// �Զ���
			if (null == toast) {
				toast = new Toast(context);
				toast.setDuration(duration);
				toast.setView(toastView);
			}
			textView.setText(message);
		} else {
			if (null == toast) {
				toast = Toast.makeText(context, message, duration);
			} else {
				toast.setText(message);
			}
		}
		if (hasSetGravity) {
			toast.setGravity(gravity, xOffset, yOffset);
		}
		toast.show();
	}

	/**
	 * �Զ�����ʾToastʱ��
	 */
	public static void show(Context context, int message, int duration) {
		if (isCustom) {// �Զ���
			if (null == toast) {
				toast = new Toast(context);
				toast.setDuration(duration);
				toast.setView(toastView);
			}
			textView.setText(message);
		} else {
			if (null == toast) {
				toast = Toast.makeText(context, message, duration);
			} else {
				toast.setText(message);
			}
		}
		if (hasSetGravity) {
			toast.setGravity(gravity, xOffset, yOffset);
		}
		toast.show();
	}

	/** Hide the toast, if any. */
	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
		}
	}

	public static void setView(View view, int textId) {
		toastView = view;
		textView = (TextView) toastView.findViewById(textId);
		isCustom = true;
	}

	public static void setGravity(int gravity0, int xOffset0, int yOffset0) {
		hasSetGravity = true;
		gravity = gravity0;
		xOffset = xOffset0;
		yOffset = yOffset0;
	}

	public static void reSet() {
		isCustom = false;
	}

}
