package com.linin.sound;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.linin.utils.L;

/**
 * 声音播放工具类
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

	public int maxStreams = 3;// 同时播放的流的最大数量
	public int streamType = AudioManager.STREAM_MUSIC;// 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)
	public int srcQuality = 0;// 采样率转化质量，当前无效果，使用0作为默认值

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

	/** 播放raw的声音文件 */
	public int playRaw(int resId, int priority) {
		int id = sp.load(context, resId, priority);
		return playSoundId(id, 1, 1, priority, 0, 1);
	}

	/** 播放raw的声音文件 */
	public int playRaw(int resId) {
		return playRaw(resId, 0);
	}

	/**
	 * 加载assets文件夹内的文件
	 * 
	 * @param fileName
	 *            例：one.mp3、sound/two.mp3
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
	 * 加载assets文件夹内的文件
	 * 
	 * @param fileName
	 *            例：one.mp3、sound/two.mp3
	 * @return streamId
	 */
	public int playAsset(String fileName) {
		return playAsset(fileName, 0);
	}

	/**
	 * 根据sd卡路径加载声音，未测试
	 * 
	 * @param path
	 *            猜测可能是：/mnt/sdcard/one.mp3 这样的形式
	 * @param priority
	 * @return streamId
	 */
	public int playSdcard(String path, int priority) {
		int id = sp.load(path, priority);
		return playSoundId(id, 1, 1, priority, 0, 1);
	}

	/**
	 * 根据sd卡路径加载声音，未测试
	 * 
	 * @param path
	 *            猜测可能是：/mnt/sdcard/one.mp3 这样的形式
	 * @return streamId
	 */
	public int playSdcard(String path) {
		return playSdcard(path, 0);
	}

	/**
	 * 根据soundId播放声音
	 * 
	 * @param soundId
	 * @param leftVolume
	 *            左声道音量
	 * @param rightVolume
	 *            右声道音量
	 * @param priority
	 *            优先级
	 * @param loop
	 *            0播放一次；-1无限循环；0+n循环n次
	 * @param rate
	 *            速率，1为正常速度；0.5~2
	 * @return streamId
	 */
	public int playSoundId(int soundId, float leftVolume, float rightVolume,
			int priority, int loop, float rate) {
		if (soundId != 0) {
			return sp.play(soundId, 1, 1, priority, 0, 1);
		} else {
			L.e(TAG, "加载SoundPool失败");
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
	 * 加载获取soundId，根据这个soundId可以使用playSoundId方法播放并获取streamId
	 * 
	 * @param obj
	 *            不解释
	 * @param priority
	 *            不解释
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
