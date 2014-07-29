package com.linin.net;

import android.os.Environment;
import android.widget.ImageView;

public class ImageLoader {

	/**
	 * 加载网络图片并下载到sd卡
	 */
	public static void loadUrl(ImageView imageview, String url) {
		new BitmapDownloaderTask(imageview).execute(url);
	}

	/**
	 * 设置sd卡的缓存路径，使用相对路径即可
	 * 
	 * @param path
	 *            例："/HappyFairy/download/"
	 */
	public static void setPath(String path) {
		BitmapDownloaderTask.ALBUM_PATH = Environment
				.getExternalStorageDirectory() + path;
	}

}
