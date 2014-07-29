package com.linin.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * rootȨ�޹�����
 * 
 * @author linin
 * 
 */
public class RootUtil {

	private static final String TAG = "linin.root";
	private static boolean mHaveRoot = false;

	/**
	 * �жϻ���Android�Ƿ��Ѿ�root�����Ƿ��ȡrootȨ��
	 */
	public static boolean haveRoot() {
		if (!mHaveRoot) {
			int ret = execRootCmdSilent("echo test"); // ͨ��ִ�в������������
			if (ret != -1) {
				L.i(TAG, "have root!");
				mHaveRoot = true;
			} else {
				L.i(TAG, "not root!");
			}
		} else {
			L.i(TAG, "mHaveRoot = true, have root!");
		}
		return mHaveRoot;
	}

	/** ��ȡrootȨ�� */
	public static boolean root() {
		try {
			Runtime.getRuntime().exec(
					new String[] { "/system/bin/su", "-c",
							"chmod 777 /dev/graphics/fb0" });
		} catch (IOException e) {
			e.printStackTrace();
			L.i(TAG, "root fail!");
			return false;
		}
		L.i(TAG, "root success!");
		return true;
	}

	/**
	 * ִ�������������
	 */
	public static String execRootCmd(String cmd) {
		String result = "";
		DataOutputStream dos = null;
		DataInputStream dis = null;

		try {
			Process p = Runtime.getRuntime().exec("su");// ����Root�����androidϵͳ����su����
			dos = new DataOutputStream(p.getOutputStream());
			dis = new DataInputStream(p.getInputStream());

			L.i(TAG, cmd);
			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			String line = null;
			while ((line = dis.readLine()) != null) {
				L.d("result", line);
				result += line;
			}
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * ִ���������ע������
	 */
	public static int execRootCmdSilent(String cmd) {
		int result = -1;
		DataOutputStream dos = null;

		try {
			Process p = Runtime.getRuntime().exec("su");
			dos = new DataOutputStream(p.getOutputStream());

			L.i(TAG, cmd);
			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			p.waitFor();
			result = p.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
