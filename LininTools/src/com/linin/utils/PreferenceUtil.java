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
 * Preference工具类
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

	// ------------保存对象的方法-------------（可能会废弃）

	private static final String SAVETAG = "list";

	/**
	 * 使用SharedPreferences保存对象类型的数据 先将对象类型转化为二进制数据，然后用特定的字符集编码成字符串进行保存
	 * 
	 * @param object
	 *            要保存的对象
	 * @param context
	 * @param shaPreName
	 *            保存的文件名
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
	 * 根据文件名取得存储的数据对象 先将取得的数据转化成二进制数组，然后转化成对象
	 * 
	 * @param context
	 * @param shaPreName
	 *            读取数据的文件名
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
			L.i("test2", "获取缓存成功");
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
		L.e("test2", "获取缓存出错");
		return null;
	}

}
