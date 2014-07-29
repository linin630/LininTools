package com.linin.app;

import android.app.Application;

/**
 * 只是演示一下CrashHandler的用法，无用！
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
