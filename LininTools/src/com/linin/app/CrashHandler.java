package com.linin.app;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;



/**
 * 截取异常的类，但是有bug，最好不要用（后期会删掉）
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private String saveUrl = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "Linin"
			+ File.separator
			+ "crash";

	private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private static CrashHandler INSTANCE;// CrashHandler实例
	private Context mContext;// 程序的Context对象

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {

	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;

		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true 如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null || mContext == null)
			return false;
		final String crashReport = getCrashReport(mContext, ex);
		new Thread() {
			public void run() {
				Looper.prepare();
				File file = save2File(crashReport);
//				sendAppCrashReport(mContext, crashReport, file);
				// 退出
				android.os.Process.killProcess(android.os.Process
						.myPid());
				System.exit(1);
				Looper.loop();
			}

		}.start();
		return true;
	}

	private File save2File(String crashReport) {
		String fileName = "crash-" + System.currentTimeMillis() + ".txt";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(saveUrl);
				if (!dir.exists())
					dir.mkdir();
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(crashReport.toString().getBytes());
				fos.close();
				return file;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取APP崩溃异常报告
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = getPackageInfo(context);
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "("
				+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
				+ "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("报错信息: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		Throwable cause = ex.getCause();
		exceptionStr.append("报错原因: " + E.getE(exceptionStr.toString()) + "\n");
		elements = cause.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	private PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			 e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

}

class E {
	/** 常见的错误 */
	public final static String[] e = new String[] { "NullPointerException",
			"IndexOutOfBoundsException", "OutOfMemoryError",
			"UnsupportedOperationException", "IllegalArgumentException",
			"IllegalStateException" };
	public final static String[] ez = new String[] { "空指针", "下标越界", "内存溢出",
			"不支持的操作", "非法参数", "非法状态" };

	public static String getE(String es) {
		String result = "无法预计到的错误！";
		for (int i = 0; i < e.length; i++) {
			if (es.contains(e[i])) {
				result = ez[i];
				break;
			}
		}
		return result;
	}
}