package com.linin.net;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;

import com.linin.utils.L;

/**
 * 网络工具类
 * @author linin
 *
 */
public class NetUtil {
	
	private static final String TAG = "linin.net";
	
	private Context context;
	private long ll = 0;//计算流量用，不计入wifi流量
	
	private static NetUtil me;
	
	public static NetUtil init(Context context){
		if (me==null) {
			me = new NetUtil(context);
		}else if (me.context!=context) {
			me = new NetUtil(context);
		}
		return me;
	}
	
	public NetUtil(Context context){
		this.context = context;
	}
	
	/**
	 * 开始计算网络流量（不计算wifi流量，单位：B）
	 */
	public long start(){
		ll = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
		L.i(TAG, "gprs use -> " + ll + "B");
		return ll;
	}
	/**
	 * 结束计算流量（单位：B）
	 */
	public long stop(){
		long l = getLL();
		ll = 0;//归零
		L.i(TAG, "gprs use -> " + l + "B");
		return l;
	}
	/**
	 * 得到目前使用的流量（单位：B）
	 */
	public long getLL(){
		long l = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
		L.i(TAG, "gprs use -> " + (l - ll) + "B");
		return l - ll;
	}
	
	/**
	 * WIFI网络开关
	 */
	public void toggleWiFi(boolean enabled) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(enabled);
		L.i(TAG, "toggleWiFi -> " + enabled);
	}
	
	/**
	 * 移动网络开关
	 */
	public void toggleMobileData(boolean enabled) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
			L.i(TAG, "toggleMobileData -> " + enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		boolean bl = true;
		if (ni != null) {
			bl = ni.isConnectedOrConnecting();
		}
		L.i(TAG, "isNetworkConnected -> " + bl);
		return bl;
	}
	
	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			netType = ConnectivityManager.TYPE_MOBILE;
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = ConnectivityManager.TYPE_WIFI;
		}
		L.i(TAG, "network's type -> " + netType
				+ "(0：nonet;1：WIFI;2：WAP;3：NET)");
		return netType;
	}
	
}
