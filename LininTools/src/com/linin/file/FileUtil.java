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
 * �ļ�������
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
	 * �ж�SD�Ƿ����
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
	 * ������Ŀ¼
	 * 
	 * @param path Ŀ¼·��
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
	 * �����ļ�
	 * 
	 * @param path �ļ�·��
	 * @return �������ļ�
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
	 * ɾ���ļ���
	 * 
	 * @param folderPath �ļ��е�·��
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
	 * ɾ���ļ����������ļ�
	 * 
	 * @param path �ļ���·��
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
	 * ɾ�������ļ�
	 * 
	 * @param path �ļ���·�����ļ���
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
	 * �ļ�����
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
	 * �����ļ���С
	 * 
	 * @param size����λB�����Ի����������λ��
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "δ֪��С";
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
	 * �õ�ĳ���ļ��Ĵ�С
	 * @param file
	 * @return �ļ���С����λB��
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
			System.out.println("�ļ�������");
		}
		L.i(TAG, "getFileSize -> "+s);
		return s;
	}
	
	/**
	 * ��ȡĿ¼�������ļ�
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
	 * �Զ��ж��Ǿ���·���������·�������·���ַ�����Ϊ"/"��ͷ��
	 */
	public static String getRealPath(String path){
		if (path.contains(SDCardPath)) {
			return path;
		}else {
			return SDCardPath+path;
		}
	}
	
	/**
	 * ��assets����ļ�copy��sd����
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
