package com.linin.utils;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.linin.at.AT;

public class AssetUtil {

	/**
	 * 根据asset地址获取bitmap对象
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String url) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getAssets().open(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 根据asset的地址将图片直接加载到imageview中
	 * 
	 * @param context
	 * @param imageview
	 * @param url
	 */
	public static void setImage(final Context context,
			final ImageView imageview, final String url) {
		new AT() {
			Bitmap bitmap = null;

			public void doInBG() {
				bitmap = getBitmap(context, url);
			}

			public void end() {
				if (bitmap != null) {
					imageview.setImageBitmap(bitmap);
				}
			}
		}.start();
	}

}
