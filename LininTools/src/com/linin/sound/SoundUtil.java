package com.linin.sound;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.linin.utils.L;

/**
 * �������Ź�����
 * 
 * @author linin
 * 
 */
public class SoundUtil {

	private final static String TAG = "linin.sound";

	private static SoundUtil me;

	private SoundPool sp;
	private Context context;
	private AssetManager am;

	public int maxStreams = 3;// ͬʱ���ŵ������������
	public int streamType = AudioManager.STREAM_MUSIC;// �������ͣ�һ��ΪSTREAM_MUSIC(������AudioManager�����г�)
	public int srcQuality = 0;// ������ת����������ǰ��Ч����ʹ��0��ΪĬ��ֵ

	public static SoundUtil init(Context context) {
		if (me == null) {
			me = new SoundUtil(context);
		}
		return me;
	}

	private SoundUtil(Context context) {
		this.context = context;
		sp = new SoundPool(maxStreams, streamType, srcQuality);
		am = context.getAssets();
	}

	/***/
	private SoundUtil(Context context, int maxStreams, int streamType,
			int srcQuality) {
		this.context = context;
		this.maxStreams = maxStreams;
		this.streamType = streamType;
		this.srcQuality = srcQuality;
		sp = new SoundPool(maxStreams, streamType, srcQuality);
		am = context.getAssets();
	}

	/** ����raw�������ļ� */
	public int playRaw(int resId, int priority) {
		int id = sp.load(context, resId, priority);
		return playSoundId(id, 1, 1, priority, 0, 1);
	}

	/** ����raw�������ļ� */
	public int playRaw(int resId) {
		return playRaw(resId, 0);
	}

	/**
	 * ����assets�ļ����ڵ��ļ�
	 * 
	 * @param fileName
	 *            ����one.mp3��sound/two.mp3
	 * @param priority
	 * @return streamId
	 */
	public int playAsset(String fileName, int priority) {
		AssetFileDescriptor afd = null;
		try {
			afd = am.openFd(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int id = sp.load(afd, priority);
		return playSoundId(id, 1, 1, priority, 0, 1);
	}

	/**
	 * ����assets�ļ����ڵ��ļ�
	 * 
	 * @param fileName
	 *            ����one.mp3��sound/two.mp3
	 * @return streamId
	 */
	public int playAsset(String fileName) {
		return playAsset(fileName, 0);
	}

	/**
	 * ����sd��·������������δ����
	 * 
	 * @param path
	 *            �²�����ǣ�/mnt/sdcard/one.mp3 ��������ʽ
	 * @param priority
	 * @return streamId
	 */
	public int playSdcard(String path, int priority) {
		int id = sp.load(path, priority);
		return playSoundId(id, 1, 1, priority, 0, 1);
	}

	/**
	 * ����sd��·������������δ����
	 * 
	 * @param path
	 *            �²�����ǣ�/mnt/sdcard/one.mp3 ��������ʽ
	 * @return streamId
	 */
	public int playSdcard(String path) {
		return playSdcard(path, 0);
	}

	/**
	 * ����soundId��������
	 * 
	 * @param soundId
	 * @param leftVolume
	 *            ����������
	 * @param rightVolume
	 *            ����������
	 * @param priority
	 *            ���ȼ�
	 * @param loop
	 *            0����һ�Σ�-1����ѭ����0+nѭ��n��
	 * @param rate
	 *            ���ʣ�1Ϊ�����ٶȣ�0.5~2
	 * @return streamId
	 */
	public int playSoundId(int soundId, float leftVolume, float rightVolume,
			int priority, int loop, float rate) {
		if (soundId != 0) {
			return sp.play(soundId, 1, 1, priority, 0, 1);
		} else {
			L.e(TAG, "����SoundPoolʧ��");
			return 0;
		}
	}

	public void pause(int streamId) {
		sp.pause(streamId);
	}

	public void stop(int streamId) {
		sp.stop(streamId);
	}

	/**
	 * ���ػ�ȡsoundId���������soundId����ʹ��playSoundId�������Ų���ȡstreamId
	 * 
	 * @param obj
	 *            ������
	 * @param priority
	 *            ������
	 * @return
	 */
	public int load(Object obj, int priority) {
		if (obj instanceof AssetFileDescriptor) {
			return sp.load((AssetFileDescriptor) obj, priority);
		} else if (obj instanceof String) {
			return sp.load((String) obj, priority);
		} else if (obj instanceof Integer) {
			return sp.load(context, (Integer) obj, priority);
		} else {
			return 0;
		}
	}

}
