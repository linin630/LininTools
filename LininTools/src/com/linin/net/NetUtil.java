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
 * ���繤����
 * @author linin
 *
 */
public class NetUtil {
	
	private static final String TAG = "linin.net";
	
	private Context context;
	private long ll = 0;//���������ã�������wifi����
	
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
	 * ��ʼ��������������������wifi��������λ��B��
	 */
	public long start(){
		ll = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
		L.i(TAG, "gprs use -> " + ll + "B");
		return ll;
	}
	/**
	 * ����������������λ��B��
	 */
	public long stop(){
		long l = getLL();
		ll = 0;//����
		L.i(TAG, "gprs use -> " + l + "B");
		return l;
	}
	/**
	 * �õ�Ŀǰʹ�õ���������λ��B��
	 */
	public long getLL(){
		long l = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
		L.i(TAG, "gprs use -> " + (l - ll) + "B");
		return l - ll;
	}
	
	/**
	 * WIFI���翪��
	 */
	public void toggleWiFi(boolean enabled) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(enabled);
		L.i(TAG, "toggleWiFi -> " + enabled);
	}
	
	/**
	 * �ƶ����翪��
	 */
	public void toggleMobileData(boolean enabled) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<?> conMgrClass = null; // ConnectivityManager��
		Field iConMgrField = null; // ConnectivityManager���е��ֶ�
		Object iConMgr = null; // IConnectivityManager�������
		Class<?> iConMgrClass = null; // IConnectivityManager��
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled����
		try {
			// ȡ��ConnectivityManager��
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// ȡ��ConnectivityManager���еĶ���mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// ����mService�ɷ���
			iConMgrField.setAccessible(true);
			// ȡ��mService��ʵ������IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// ȡ��IConnectivityManager��
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// ȡ��IConnectivityManager���е�setMobileDataEnabled(boolean)����
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// ����setMobileDataEnabled�����ɷ���
			setMobileDataEnabledMethod.setAccessible(true);
			// ����setMobileDataEnabled����
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
	 * ��������Ƿ����
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
	 * ��ȡ��ǰ��������
	 * 
	 * @return 0��û������ 1��WIFI���� 2��WAP���� 3��NET����
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
				+ "(0��nonet;1��WIFI;2��WAP;3��NET)");
		return netType;
	}
	
}
