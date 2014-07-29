package com.linin.net;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.linin.image.ImageUtil;
import com.linin.java.Encrypt;
import com.linin.utils.CacheUtil;
import com.linin.utils.L;
import com.linin.utils.ScreenUtil;
import com.linin.utils.ViewUtil;

class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

	private final String pwd = "linin630";// 异或密码
	public static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/Linin/download/";

	private CacheUtil cc;// 缓存用的
	private final WeakReference<ImageView> imageViewReference; // 使用WeakReference解决内存问题

	public BitmapDownloaderTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);
		cc = CacheUtil.init();
	}

	@Override
	protected Bitmap doInBackground(String... params) { // 实际的下载线程，内部其实是concurrent线程，所以不会阻塞
		String imgName = Encrypt.code(params[0], pwd);
		Bitmap bitmap = null;

		// 这里判断一下，如果缓存有了，用缓存
		bitmap = (Bitmap) cc.getCache(imgName);
		if (bitmap != null) {
			L.d("使用了缓存！" + imgName);
			return bitmap;
		}

		// 如果sd卡有下载过了，直接读取sd卡，并加入缓存
		String imgPath = ALBUM_PATH + imgName + ".png";
		if (new File(imgPath).exists()) {
			bitmap = ImageUtil.getSmallBitmap(imgPath, ScreenUtil.width,
					ScreenUtil.height);
			cc.addCache(imgName, bitmap);
			L.d("读取了sd卡！" + imgName);
			return bitmap;
		}

		// 缓存和sd卡都没有的时候就直接访问后台，并存入缓存和sd卡
		bitmap = downloadBitmap(params[0]);
		ImageUtil.saveFile(bitmap, imgPath);
		L.d("访问了后台！" + imgName);
		cc.addCache(imgName, bitmap);
		ImageUtil.saveFile(bitmap, imgPath);

		return bitmap;

	}

	@Override
	protected void onPostExecute(Bitmap bitmap) { // 下载完后执行的
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap); // 下载完设置imageview为刚才下载的bitmap对象
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				ViewUtil.setViewHeight(imageView, ScreenUtil.width * h / w);
			}
		}
	}

	public static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient.newInstance("Linin");
		final HttpGet getRequest = new HttpGet(url);

        try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("cwjDebug", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			// Log.e("android123Debug", "Error while retrieving bitmap from "
			// + url, e.toString());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

}