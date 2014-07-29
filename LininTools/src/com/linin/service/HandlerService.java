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
 * ������UI���̴߳����ʱ����ʱ�����֡�����Ӧ���ĶԻ�����Ҫ��AndroidManifest.xml��ע��HandlerService��δ���ԣ�
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
		// �������߳����½���ʵ������һ��HandlerThread ����
		HandlerThread handlerThread = new HandlerThread("handler_thread");
		// ��ʹ��HandlerThread��getLooper()����֮ǰ�������ȵ��ø����start();
		handlerThread.start();
		// ����HandlerThread .getLooperʵ����һ��handler����,��HandlerThread
		// ���messageQueue����
		MyHandler myHandler = new MyHandler(handlerThread.getLooper());
		// ��ø�handler�����Ϣ
		Message msg = myHandler.obtainMessage();
		// ��msg���͵�Ŀ�������ν��Ŀ����󣬾������ɸ�msg�����handler����
		msg.sendToTarget();

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// Handler��
	class MyHandler extends Handler {
		public MyHandler() {

		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {// ����message����������Ϣ
			// ��ʱ�Ĳ���
			if (mDos != null) {
				mDos.doInBg();
			}
		}
	}

}
