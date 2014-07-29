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
 * ר�Ŷ�ͼƬ���д���Ĺ�����
 * 
 * @author linin
 * 
 */
public class ImageUtil {

	/**
	 * ��Bitmapת��ΪBase64�ַ���
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
	 * ��Drawableת��ΪBase64�ַ���
	 */
	public static String drawableToBase64(Drawable d) {
		return bitmapToBase64(drawableToBitmap(d));
	}

	/**
	 * ��Drawableת��ΪBitmap
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
	 * ��Bitmapת��ΪDrawable
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * ����BitmapͼƬΪ�����ļ�
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
	 * ����·�����ͼƬ��ѹ��������bitmap������ʾ
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
	 * ����ͼƬ������ֵ
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
	 * ����ѹ������
	 * 
	 * @param max
	 *            ѹ�����ͼƬ���ó��� max kb
	 */
	public static Bitmap compressImage(Bitmap image, int max) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > max) { // ѭ���ж����ѹ����ͼƬ�Ƿ����max
															// kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		try {
			isBm.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * ����view������bitmapͼƬ�������ڽ�ͼ����
	 */
	public static Bitmap getViewBitmap(View v) {

		v.clearFocus(); //
		v.setPressed(false); //
		// �ܻ�����ͷ���false
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
	 * ��ȡwebView��������Ľ�ͼ
	 * 
	 * @param webView
	 *            ǰ�᣺WebViewҪ����webView.setDrawingCacheEnabled(true);
	 * @return
	 */
	public static Bitmap captureWebViewVisibleSize(WebView webView) {
		Bitmap bmp = webView.getDrawingCache();
		return bmp;
	}

	/**
	 * ��ȡwebView����(webView���ص��������ݵĴ�С)
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
	 * ����
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
				height - statusBarHeight);// ����ȥ���˱�����
		view.destroyDrawingCache();
		return bitmap2;
	}

}
