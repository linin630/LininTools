package com.linin.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {

	private static FragmentUtil instance;

	public FragmentActivity act;
	private FragmentManager fm;

	public static FragmentUtil init(FragmentActivity act) {
		if (instance == null) {
			instance = new FragmentUtil(act);
		} else if (instance.act != act) {
			instance = new FragmentUtil(act);
		}
		return instance;
	}

	public FragmentUtil(FragmentActivity act) {
		this.act = act;
		fm = act.getSupportFragmentManager();
	}

	public void replace(int containerId, Fragment fragment) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(containerId, fragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	/** ÿ�ζ������¼��أ�������ͬһ��fragmentҲ��Ч�������¼��� */
	public void replaceEver(int containerId, Fragment fragment) {
		remove(fragment);
		replace(containerId, fragment);
	}

	/** ��add�ᾭ���������Ƽ�ʹ�� */
	public void add(int containerId, Fragment fragment) {
		fm.beginTransaction().add(containerId, fragment)
				.commitAllowingStateLoss();
	}

	public void remove(Fragment fragment) {
		fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
	}

}
