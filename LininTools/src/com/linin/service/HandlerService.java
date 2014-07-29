package com.linin.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

/**
 * 用于让UI主线程处理费时操作时不出现“无响应”的对话框，需要在AndroidManifest.xml中注册HandlerService（未测试）
 * 
 * @author linin
 * 
 */
public class HandlerService extends Service {

	public interface DoSomething {
		void doInBg();
	}

	private static DoSomething mDos;

	public static void load(Activity act, DoSomething dos) {
		mDos = dos;
		act.startService(new Intent(act, HandlerService.class));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 先在主线程里新建（实例化）一个HandlerThread 对象
		HandlerThread handlerThread = new HandlerThread("handler_thread");
		// 在使用HandlerThread的getLooper()方法之前，必须先调用该类的start();
		handlerThread.start();
		// 利用HandlerThread .getLooper实例化一个handler对象,该HandlerThread
		// 与该messageQueue关联
		MyHandler myHandler = new MyHandler(handlerThread.getLooper());
		// 获得该handler里的消息
		Message msg = myHandler.obtainMessage();
		// 将msg发送到目标对象，所谓的目标对象，就是生成该msg对象的handler对象
		msg.sendToTarget();

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// Handler类
	class MyHandler extends Handler {
		public MyHandler() {

		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {// 接收message发过来的消息
			// 耗时的操作
			if (mDos != null) {
				mDos.doInBg();
			}
		}
	}

}
