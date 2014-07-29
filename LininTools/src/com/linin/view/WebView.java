package com.linin.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.linin.utils.L;

/**
 * 自定义WebView，自动设置好常用的功能，并提供一些特殊的功能
 * 
 * @author linin
 * 
 */
public class WebView extends android.webkit.WebView {

	private Context context;

	public WebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public WebView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		setInitialScale(100);
		getSettings().setJavaScriptEnabled(true);
		setWebChromeClient(new MyWebChromeClient());
		setWebViewClient(new MyWebViewClient());
	}

	final class MyWebChromeClient extends WebChromeClient {
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			try {
				L.d("onJsAlert-->" + message);
				Builder builder = new Builder(context);
				builder.setMessage(message);
				builder.setPositiveButton("OK", null);
				builder.create().show();
				result.confirm();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	final class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			L.d("shouldOverrideUrlLoading url->" + url);
			view.loadUrl(url);
			return false;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			L.d("onPageStarted url->" + url);
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url) {
			L.d("onPageFinished url->" + url);
			super.onPageFinished(view, url);
		}
	}

}
