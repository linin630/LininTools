package com.linin.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.linin.other.Base64;

/**
 * Preference������
 * @author linin
 *
 */
public class PreferenceUtil {

	public static boolean isFirstOpen(Context context) {
		String key = "IsAppFirstOpen";
		boolean isFirstOpen = getPrefBoolean(context, key, true);
		setPrefBoolean(context, key, false);
		return isFirstOpen;
	}

	public static String getPrefString(Context context, String key,
			final String defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	public static void setPrefString(Context context, final String key,
			final String value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	public static boolean getPrefBoolean(Context context, final String key,
			final boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static boolean hasKey(Context context, final String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(
				key);
	}

	public static void setPrefBoolean(Context context, final String key,
			final boolean value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	public static void setPrefInt(Context context, final String key,
			final int value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	public static int getPrefInt(Context context, final String key,
			final int defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	public static void setPrefFloat(Context context, final String key,
			final float value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	public static float getPrefFloat(Context context, final String key,
			final float defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	public static void setSettingLong(Context context, final String key,
			final long value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	public static long getPrefLong(Context context, final String key,
			final long defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	public static void clearPreference(Context context,
			final SharedPreferences p) {
		final Editor editor = p.edit();
		editor.clear();
		editor.commit();
	}

	// ------------�������ķ���-------------�����ܻ������

	private static final String SAVETAG = "list";

	/**
	 * ʹ��SharedPreferences����������͵����� �Ƚ���������ת��Ϊ���������ݣ�Ȼ�����ض����ַ���������ַ������б���
	 * 
	 * @param object
	 *            Ҫ����Ķ���
	 * @param context
	 * @param shaPreName
	 *            ������ļ���
	 */
	public static void saveObject(Context context, String shaPreName,
			Object object) {
		L.d("test2", "save key=" + shaPreName + ";object = " + object);
		if (object == null) {
			L.e("test2", "null");
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				shaPreName, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		List<Object> list = getObjectList(context, shaPreName);
		if (null == list) {
			list = new ArrayList<Object>();
		} else {
			list.clear();
		}
		list.add(object);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(list);
			String strList = new String(Base64.encodeBytes(baos.toByteArray()));
			editor.putString(SAVETAG, strList);
			editor.commit();
			oos.close();
		} catch (IOException e) {
			L.e("test2", "IOException");
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				L.e("test2", "IOException 2");
				e.printStackTrace();
			}
		}
	}

	public static Object getObject(Context context, String shaPreName) {
		List<Object> list = getObjectList(context, shaPreName);
		try {
			return list.get(list.size() - 1);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * �����ļ���ȡ�ô洢�����ݶ��� �Ƚ�ȡ�õ�����ת���ɶ��������飬Ȼ��ת���ɶ���
	 * 
	 * @param context
	 * @param shaPreName
	 *            ��ȡ���ݵ��ļ���
	 * @return
	 */
	public static List<Object> getObjectList(Context context, String shaPreName) {
		L.d("test2", "get key=" + shaPreName);
		List<Object> list;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				shaPreName, Activity.MODE_PRIVATE);
		String message = sharedPreferences.getString(SAVETAG, "");
		byte[] buffer = null;
		try {
			buffer = Base64.decode(message.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			list = (List<Object>) ois.readObject();
			ois.close();
			L.i("test2", "��ȡ����ɹ�");
			return list;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		L.e("test2", "��ȡ�������");
		return null;
	}

}
