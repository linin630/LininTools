package com.linin.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

import com.linin.at.AT;

/**
 * 文本工具类
 * @author linin
 *
 */
public class TxtUtil {
	
	/**
	 * 
	 * @param txtName 文件名（t.txt或没有后缀名应该也没事...）
	 * @param txtBody 内容
	 * @param path 路径
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
     * 读取文件内容
     * 
     * @param fileName 文件名
     * @return 文件内容
     * @throws Exception
     */
    public static String readTxt(Context context,String fileName) throws Exception {
        // 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        FileInputStream fis = context.openFileInput(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        //将读取后的数据放置在内存中---ByteArrayOutputStream
        while ((len = fis.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        fis.close();
        baos.close();
        //返回内存中存储的数据
        return baos.toString();
    }

}
