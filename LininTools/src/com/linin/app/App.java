package com.linin.app;

import android.app.Application;

/**
 * ֻ����ʾһ��CrashHandler���÷������ã�
 * 
 * @author linin
 * 
 */
public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.getInstance().init(this);
	}

}
