package com.linin.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.linin.utils.L;

/**
 * ���űȽϳ�����Ƶ�ļ�����Ƶ�Ĺ����࣬δ����
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

	/** ����raw�������ļ� */
	public void play(int resId) {
		release();
		mp = MediaPlayer.create(context, resId);
		mp.start();
	}

	/** ����·���µ������ļ�����·������Ϊ����·���� */
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

	/** �����ͷŲ�����ռ�õ���Դ��һ��ȷ������ʹ�ò�����ʱӦ������������ͷ���Դ�� */
	public void release() {
		try {
			mp.release();
		} catch (Exception e) {
			L.e(TAG, "release fail!");
			e.printStackTrace();
			reset();
		}
	}

	/** ����ʹ��������Error״̬�лָ����������»ᵽIdle״̬�� */
	public void reset() {
		try {
			mp.reset();
		} catch (Exception e) {
			L.e(TAG, "reset fail!");
			e.printStackTrace();
		}
	}

}
