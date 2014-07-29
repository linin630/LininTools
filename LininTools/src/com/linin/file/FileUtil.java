package com.linin.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;

import com.linin.utils.L;

/**
 * 文件工具类
 * @author linin
 *
 */
public class FileUtil {

	public Context context;
	public static final String SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String TAG = "linin.file";

	private static FileUtil me;

	public static FileUtil init(Context context){
		if (me==null) {
			me = new FileUtil(context);
		}else if (me.context!=context) {
			me = new FileUtil(context);
		}
		return me;
	}

	public FileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 判断SD是否存在
	 */
	public static boolean isSdcardExist() {
		boolean bl = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			bl = true;
		}
		L.i(TAG, "isSdcardExist -> "+bl);
		return bl;
	}
	/**
	 * 创建根目录
	 * 
	 * @param path 目录路径
	 */
	public static void createDir(String path) {
		path = getRealPath(path);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
			L.i(TAG, "createDir -> "+path);
		}
	}
	/**
	 * 创建文件
	 * 
	 * @param path 文件路径
	 * @return 创建的文件
	 */
	public static File createFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
				L.i(TAG, "createFile -> "+path);
			} catch (IOException e) {
				L.w(TAG, "createFile -> "+path+" fail!!");
				return null;
			}
		}
		return file;
	}
	/**
	 * 删除文件夹
	 * 
	 * @param folderPath 文件夹的路径
	 */
	public static void delDir(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		if (myFilePath.isDirectory()) {
			myFilePath.delete();
			L.i(TAG, "delDir -> "+folderPath);
		}else {
			L.w(TAG, "delDir -> "+folderPath+" is not a dir!!");
		}
	}
	/**
	 * 删除文件夹内所有文件
	 * 
	 * @param path 文件的路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			L.w(TAG, "delAllFile -> "+path+" no exists!!");
			return;
		}
		if (!file.isDirectory()) {
			L.w(TAG, "delAllFile -> "+path+" is not a dir!!");
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delDir(path + "/" + tempList[i]);
			}
		}
		L.i(TAG, "delAllFile -> "+path);
	}
	/**
	 * 删除单个文件
	 * 
	 * @param path 文件的路径及文件名
	 */
	public static void delFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			L.w(TAG, "delFile -> "+path+" no exists!!");
			return;
		}
		if (file.isFile()) {
			file.delete();
			L.i(TAG, "delFile -> "+path);
		}
	}
	/**
	 * 文件拷贝
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public static boolean CopySdcardFile(String fromFile, String toFile){
		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			L.i(TAG, "copy -> "+fromFile+" to "+toFile);
			return true;
		} catch (Exception ex) {
			L.w(TAG, "copy -> "+fromFile+" to "+toFile+" fail!!");
			return false;
		}
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size（单位B，可以换算成其他单位）
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 得到某个文件的大小
	 * @param file
	 * @return 文件大小（单位B）
	 */
	public static long getFileSize(File f) {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				s = fis.available();
			} catch (Exception e) {
				L.w(TAG, "getFileSize -> fail!!");
				e.printStackTrace();
			}
		}
		else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				L.w(TAG, "getFileSize -> createNewFile fail!!");
				e.printStackTrace();
			}
			System.out.println("文件不存在");
		}
		L.i(TAG, "getFileSize -> "+s);
		return s;
	}
	
	/**
	 * 获取目录下所有文件
	 */
	public static File[] getAllFile(String path){
		File dir = new File(path);
		if (!dir.isDirectory()) {
			return new File[]{};
		}else {
			return dir.listFiles();
		}
	}
	
	/**
	 * 自动判断是绝对路径还是相对路径（相对路径字符串需为"/"开头）
	 */
	public static String getRealPath(String path){
		if (path.contains(SDCardPath)) {
			return path;
		}else {
			return SDCardPath+path;
		}
	}
	
	/**
	 * 将assets里的文件copy到sd卡上
	 */
	public static void CopyAssets(Context context, String assetDir, String dir) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {
				L.e(TAG, "cannot create directory.");
			}
		}
		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssets(context, fileName, dir + fileName + "/");
					} else {
						CopyAssets(context, assetDir + "/" + fileName, dir
								+ fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
