package com.linin.system;

import android.content.Context;
import android.os.Vibrator;

/**
 * 系统服务工具类
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
	 * 震动（单位：毫秒）
	 */
	public void vibrate(long time){
		vibrator.vibrate(time);
	}
	/**
	 * 震动（单位：毫秒）
	 * @param times 待机时间+震动时间 的数组
	 * @param loop 循环次数
	 */
	public void vibrate(long[] times,int loop){
		vibrator.vibrate(times, loop);
	}

}
