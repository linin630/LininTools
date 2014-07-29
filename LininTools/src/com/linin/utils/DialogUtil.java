package com.linin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.linin.tools.R;

public class DialogUtil {
	
	private static Dialog dialog;

	/**
	 * ��ȡ�Զ���view��dialog
	 */
	public static Dialog getDialog(Activity context, View view) {
		int screenW = ScreenUtil.getScreenWidth(context);
		int screenH = ScreenUtil.getScreenHeight(context);
		return getDialog(context, view, screenW - 100, screenH - 300);
	}

	public static Dialog getDialog(Activity context, View view, int width,
			int height) {
		final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		lp.height = height;
		window.setGravity(Gravity.CENTER); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.MenuDialogAnimation); // ��Ӷ���
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	public static Dialog getMenuDialog(Activity context, View view, int width,
			int height) {
		final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		lp.height = height;
		window.setGravity(Gravity.BOTTOM); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.MenuDialogAnimation); // ��Ӷ���
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	public static Dialog getLoadingDialog(Activity context) {
		dialog = new Dialog(context, R.style.DialogStyle);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.dialog_loading);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = ScreenUtil.getScreenWidth(context);
		lp.height = ScreenUtil.getScreenHeight(context);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	public static void loadingDone() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

	/** ����һ���򵥵�dialog */
	public static void alert(Context context, String title, String message,
			String buttonLeft, String buttonRight, OnClickListener onLeftClick,
			OnClickListener onRightClick) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		if (buttonLeft != null) {
			builder.setPositiveButton(buttonLeft, onLeftClick);
		}
		if (buttonRight != null) {
			builder.setNegativeButton(buttonRight, onRightClick);
		}
		builder.create().show();
	}

}