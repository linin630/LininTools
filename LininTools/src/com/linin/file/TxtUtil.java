package com.linin.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

import com.linin.at.AT;

/**
 * �ı�������
 * @author linin
 *
 */
public class TxtUtil {
	
	/**
	 * 
	 * @param txtName �ļ�����t.txt��û�к�׺��Ӧ��Ҳû��...��
	 * @param txtBody ����
	 * @param path ·��
	 */
	public static void saveTxt(String txtName,final String txtBody,String path){
		path = FileUtil.getRealPath(path);
		if (path.charAt(path.length()-1)!='/') {
			path += "/";
		}
		if (!txtName.endsWith(".txt")) {
			txtName = txtName + ".txt";
        }
		final File txtFile = FileUtil.createFile(path+txtName);
		new AT(){
			public void doInBG() {
				try {
					FileOutputStream fos = new FileOutputStream(txtFile);
					fos.write(txtBody.getBytes());
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
     * ��ȡ�ļ�����
     * 
     * @param fileName �ļ���
     * @return �ļ�����
     * @throws Exception
     */
    public static String readTxt(Context context,String fileName) throws Exception {
        // ����ҳ������Ķ����ı���Ϣ�����Ե��ļ���������.txt��׺����βʱ���Զ�����.txt��׺
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        FileInputStream fis = context.openFileInput(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        //����ȡ������ݷ������ڴ���---ByteArrayOutputStream
        while ((len = fis.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        fis.close();
        baos.close();
        //�����ڴ��д洢������
        return baos.toString();
    }

}
