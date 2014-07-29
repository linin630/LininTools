package com.linin.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.webkit.WebView;

import com.linin.other.Base64;
import com.linin.utils.L;

/**
 * 专门对图片进行处理的工具类
 * 
 * @author linin
 * 
 */
public class ImageUtil {

	/**
	 * 将Bitmap转化为Base64字符串
	 */
	public static String bitmapToBase64(Bitmap b) {
		String s = "";
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, bao);
		byte[] ba = bao.toByteArray();
		s = Base64.encodeBytes(ba);
		return s;
	}

	/**
	 * 将Drawable转化为Base64字符串
	 */
	public static String drawableToBase64(Drawable d) {
		return bitmapToBase64(drawableToBitmap(d));
	}

	/**
	 * 将Drawable转化为Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 将Bitmap转化为Drawable
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * 保存Bitmap图片为本地文件
	 */
	public static void saveFile(Bitmap bitmap, String filename) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filename);
			if (fileOutputStream != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			L.d("Exception:FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			L.d("IOException:IOException");
			e.printStackTrace();
		}
	}

	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 */
	public static Bitmap getSmallBitmap(String filePath, int w, int h) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, w, h);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算图片的缩放值
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 质量压缩方法
	 * 
	 * @param max
	 *            压缩后的图片不得超过 max kb
	 */
	public static Bitmap compressImage(Bitmap image, int max) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > max) { // 循环判断如果压缩后图片是否大于max
															// kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		try {
			isBm.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 根据view来生成bitmap图片，可用于截图功能
	 */
	public static Bitmap getViewBitmap(View v) {

		v.clearFocus(); //
		v.setPressed(false); //
		// 能画缓存就返回false
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/**
	 * 截取webView可视区域的截图
	 * 
	 * @param webView
	 *            前提：WebView要设置webView.setDrawingCacheEnabled(true);
	 * @return
	 */
	public static Bitmap captureWebViewVisibleSize(WebView webView) {
		Bitmap bmp = webView.getDrawingCache();
		return bmp;
	}

	/**
	 * 截取webView快照(webView加载的整个内容的大小)
	 * 
	 * @param webView
	 * @return
	 */
	public static Bitmap captureWebView(WebView webView) {
		Picture snapShot = webView.capturePicture();

		Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
				snapShot.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		snapShot.draw(canvas);
		return bmp;
	}

	/**
	 * 截屏
	 * 
	 * @param context
	 * @return
	 */
	public static Bitmap captureScreen(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;

		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
				height - statusBarHeight);// 这里去掉了标题栏
		view.destroyDrawingCache();
		return bitmap2;
	}

}
