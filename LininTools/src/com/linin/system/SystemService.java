package com.linin.system;

import android.content.Context;
import android.os.Vibrator;

/**
 * ϵͳ���񹤾���
 * @author linin
 *
 */
public class SystemService {
	
	public  Context context;
	
	private Vibrator vibrator;
	
	private static SystemService me;
	
	public static SystemService init(Context context){
		if (me==null) {
			me = new SystemService(context);
		}else if (me.context!=context) {
			me = new SystemService(context);
		}
		return me;
	}
	
	public SystemService(Context context){
		this.context = context;
		this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	/**
	 * �𶯣���λ�����룩
	 */
	public void vibrate(long time){
		vibrator.vibrate(time);
	}
	/**
	 * �𶯣���λ�����룩
	 * @param times ����ʱ��+��ʱ�� ������
	 * @param loop ѭ������
	 */
	public void vibrate(long[] times,int loop){
		vibrator.vibrate(times, loop);
	}

}
