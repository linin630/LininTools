package com.linin.net;

import android.os.Environment;
import android.widget.ImageView;

public class ImageLoader {

	/**
	 * ��������ͼƬ�����ص�sd��
	 */
	public static void loadUrl(ImageView imageview, String url) {
		new BitmapDownloaderTask(imageview).execute(url);
	}

	/**
	 * ����sd���Ļ���·����ʹ�����·������
	 * 
	 * @param path
	 *            ����"/HappyFairy/download/"
	 */
	public static void setPath(String path) {
		BitmapDownloaderTask.ALBUM_PATH = Environment
				.getExternalStorageDirectory() + path;
	}

}
