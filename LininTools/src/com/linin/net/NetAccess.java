package com.linin.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.linin.utils.DialogUtil;
import com.linin.utils.L;
import com.linin.utils.T;

/**
 * 封装网络访问的类
 * 
 * @author linin
 * 
 */
public class NetAccess {
	
	/**
	 * 建议在Application的onCreate里设置头文件
	 */
	public static Map<String, String> head;
	/** 这个是用来防止多次加载同个url的，并且仅对get有效 */
	private static String urls = "";

	private static final int TIMEOUT = 10000;

	public interface NetAccessListener {
		public void onAccessComplete(String url, String object,
				AjaxStatus status, String flag);
	}

	private static AQuery aq;
	private static String host;
	private static int port;
	private static boolean hasSetProxy = false;// 是否设置了代理
	private static boolean shouldAllProxy = false;// 是否需要全程使用代理
	
	private static Dialog loading;
	private static void showLoading(Context context){
		if (loading==null) {
			loading = DialogUtil.getLoadingDialog((Activity) context);
			loading.show();
		}else {
			if (loading.isShowing()) {//这段代码是为了防止同时使用两个Dialog导致一个永久存在
				loading.cancel();
			}
			loading.show();
		}
	}
	private static void cancelLoading(){
		if (loading!=null) {
			if (loading.isShowing()) {
				loading.cancel();
				loading = null;
			}
		}
	}

	/**
	 * 使用post方法请求数据
	 */
	public static void post(Context context, String url,
			final NetAccessListener listener, Map<String, Object> params,
			final String flag,final boolean hasDialog) {
		aq = new AQuery(context);
		AjaxCallback<String> callback = new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				super.callback(url, object, status);
				cancelLoading();
				L.i("post:"+url+"-->"+object);
				listener.onAccessComplete(url, object, status, flag);
			}
		};
		callback.url(url)
		.type(String.class)
		.timeout(TIMEOUT)
		.encoding("UTF-8")
		.header("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		if (params != null) {
			callback.params(params);
		}
		if (head != null) {
			for (String key : head.keySet()) {
				callback.header(key, head.get(key));
			}
		}
		// cookie
		Iterator iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			callback.cookie(key, value);
		}
		if (hasSetProxy) {// 如果需要一次代理
			aq.proxy(flag, port);
		}
		aq.ajax(callback);
		if (!shouldAllProxy) {// 如果不需要全局
			cancelProxy();
		}
		showLoading(context);
	}
	
	public static void post(Context context, String url,
			final NetAccessListener listener) {
		post(context, url, listener, null, null);
	}

	public static void post(Context context, String url,
			final NetAccessListener listener, Map<String, Object> params) {
		post(context, url, listener, params, null);
	}

	public static void post(Context context, String url,
			final NetAccessListener listener, Map<String, Object> params,
			final String flag) {
		post(context, url, listener, params, flag, true);
	}

	/**
	 * 使用get方式获取数据
	 */
	public static void get(Context context, String url,
			final NetAccessListener listener, Map<String, Object> params,
			final String flag,final boolean hasDialog) {
		aq = new AQuery(context);
		AjaxCallback<String> callback = new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				super.callback(url, object, status);
				cancelLoading();
				L.i("get:"+url+"-->"+object);
				urls = urls.replaceAll(url, "");
				L.i("urls replace-->" + urls);
				listener.onAccessComplete(url, object, status, flag);
			}
		};
		callback.type(String.class).timeout(TIMEOUT).encoding("UTF-8");
		if (params != null) {
			url = url + "?";
			for (String key : params.keySet()) {
				url = url + key + "=" + params.get(key) + "&";
			}
			url = url.substring(0, url.length() - 1);
		}
		if (head != null) {
			for (String key : head.keySet()) {
				callback.header(key, head.get(key));
			}
		}
		callback.url(url).header("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		callback.url(url);

		// cookie
		Iterator iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			callback.cookie(key, value);
		}

		if (!urls.contains(url)) {
			urls += url;
			L.d("url is loading!-->" + url);
			if (hasSetProxy) {// 如果需要一次代理
				aq.proxy(flag, port);
			}
			aq.ajax(callback);
			if (!shouldAllProxy) {// 如果不需要全局
				cancelProxy();
			}
			showLoading(context);
		}
	}

	public static void get(Context context, String url,
			final NetAccessListener listener, Map<String, Object> params) {
		get(context, url, listener, params, null);
	}

	public static void get(Context context, String url,
			final NetAccessListener listener) {
		get(context, url, listener, null, null);
	}

	public static void get(Context context, String url,
			final NetAccessListener listener, Map<String, Object> params,
			final String flag) {
		get(context, url, listener, params, flag, true);
	}

	/** ------------以下是单独用于登录的，用于解决 需要登录状态的session才可以调用其他的接口 的需求-------------- */
	public static void login(final Context context, String url,
			final NetAccessListener listener, final Map<String, Object> params,
			final String flag) {
		login(context, url, listener, params, flag, null);
	}

	public interface NetAccessFailListener {
		public void onLoginFail();
	}

	public static void login(final Context context, String url,
			final NetAccessListener listener, final Map<String, Object> params,
			final String flag, final NetAccessFailListener failListener) {
		if (!NetUtil.init(context).isNetworkConnected()
				|| NetUtil.init(context).getNetworkType() == 0) {
			T.showLong(context, "网络未连接，请检查本地网络");
			if (failListener != null) {
				failListener.onLoginFail();
			}
			return;
		}
		final Dialog dialog = DialogUtil.getLoadingDialog((Activity) context);
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (params != null) {
			url = url + "?";
			for (String key : params.keySet()) {
				url = url + key + "=" + params.get(key) + "&";
			}
			url = url.substring(0, url.length() - 1);
		}
		final String u = url;
		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... arg0) {
				DefaultHttpClient httpclient = new DefaultHttpClient();
				httpclient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
				httpclient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
				if (hasSetProxy) {// 如果需要代理
					HttpHost proxy = new HttpHost(host, port);
					httpclient.getParams().setParameter(
							ConnRouteParams.DEFAULT_PROXY, proxy);
				}
				HttpGet httpget = new HttpGet(u);
				HttpResponse response = null;
				try {
					response = httpclient.execute(httpget);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (response == null) {
					httpclient.getConnectionManager().shutdown();
					return null;
				}
				String result = null;
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获取返回的数据
					try {
						result = EntityUtils.toString(response.getEntity(),
								"UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SaveCookies(response);

				// 保存cookie为字符串
				List<Cookie> cookies = httpclient.getCookieStore().getCookies();
				if (!cookies.isEmpty()) {
					for (int i = cookies.size(); i > 0; i--) {
						Cookie cookie = cookies.get(i - 1);
						if (cookie.getName().equalsIgnoreCase("jsessionid")) {
							// 使用一个常量来保存这个cookie，用于做session共享之用
							appCookie = cookie;
						}
					}
				}

				httpclient.getConnectionManager().shutdown();
				if (!shouldAllProxy) {// 如果不需要全局代理
					cancelProxy();
				}
				return result;
			}

			protected void onPostExecute(final String result) {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						listener.onAccessComplete(u, result, null, flag);
						try {
							dialog.cancel();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}.execute("");
	}

	/**
	 * 临时cookie，用于测试，如果成功，保存到数据库
	 */
	private static HashMap<String, String> CookieContiner = new HashMap<String, String>();
	public static Cookie appCookie;

	/**
	 * 保存Cookie
	 */
	public static void SaveCookies(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getHeaders("Set-Cookie");
		String headerstr = headers.toString();
		if (headers == null)
			return;
		for (int i = 0; i < headers.length; i++) {
			String cookie = headers[i].getValue();
			String[] cookievalues = cookie.split(";");
			for (int j = 0; j < cookievalues.length; j++) {
				String[] keyPair = cookievalues[j].split("=");
				String key = keyPair[0].trim();
				String value = keyPair.length > 1 ? keyPair[1].trim() : "";
				CookieContiner.put(key, value);
			}
		}
	}

	/**
	 * 用于在WebView中设置登录状态的session，在使用WebView前保证调用此方法一次
	 * 
	 * @param url
	 *            需要登陆的接口
	 */
	public static void setCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		Cookie sessionCookie = appCookie;
		if (sessionCookie != null) {
			String cookieString = sessionCookie.getName() + "="
					+ sessionCookie.getValue() + "; domain="
					+ sessionCookie.getDomain();
			cookieManager.setCookie(url, cookieString);
			CookieSyncManager.getInstance().sync();
		}
	}

	/**
	 * 设置代理，设置完代理后，下一次的访问会使用代理去访问
	 * 
	 * @param host
	 *            192.168.1.1
	 * @param port
	 *            8080
	 */
	public static void setProxy(String host, int port) {
		hasSetProxy = true;
		shouldAllProxy = true;
		NetAccess.host = host;
		NetAccess.port = port;
	}

	/**
	 * 设置代理，设置完代理后，下一次的访问会使用代理去访问
	 * 
	 * @param host
	 *            192.168.1.1
	 * @param port
	 *            8080
	 */
	public static void setProxyNext(String host, int port) {
		hasSetProxy = true;
		shouldAllProxy = false;
		NetAccess.host = host;
		NetAccess.port = port;
	}

	public static void cancelProxy() {
		hasSetProxy = false;
	}

}
