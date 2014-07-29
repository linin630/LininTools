package com.linin.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.linin.utils.L;

/**
 * 播放比较长的音频文件或视频的工具类，未测试
 * 
 * @author linin
 * 
 */
public class MediaUtil {
	private final static String TAG = "linin.media";

	private static MediaUtil me;

	private MediaPlayer mp;
	private Context context;

	public static MediaUtil init(Context context) {
		if (me == null) {
			me = new MediaUtil(context);
		}
		return me;
	}

	private MediaUtil(Context context) {
		this.context = context;
		mp = new MediaPlayer();
	}

	/** 播放raw的声音文件 */
	public void play(int resId) {
		release();
		mp = MediaPlayer.create(context, resId);
		mp.start();
	}

	/** 播放路径下的声音文件（该路径可以为网络路径） */
	public void play(String path) {
		release();
		mp = new MediaPlayer();
		try {
			mp.setDataSource(path);
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void pause() {
		try {
			mp.pause();
		} catch (Exception e) {
			L.e(TAG, "pause fail!");
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			mp.stop();
		} catch (Exception e) {
			L.e(TAG, "stop fail!");
			e.printStackTrace();
		}
	}

	/** 可以释放播放器占用的资源，一旦确定不再使用播放器时应当尽早调用它释放资源。 */
	public void release() {
		try {
			mp.release();
		} catch (Exception e) {
			L.e(TAG, "release fail!");
			e.printStackTrace();
			reset();
		}
	}

	/** 可以使播放器从Error状态中恢复过来，重新会到Idle状态。 */
	public void reset() {
		try {
			mp.reset();
		} catch (Exception e) {
			L.e(TAG, "reset fail!");
			e.printStackTrace();
		}
	}

}
