package com.linin.sql;

import java.io.File;
import java.io.IOException;

import com.linin.utils.L;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseContext extends ContextWrapper{

	public DatabaseContext(Context base) {
		super(base);
	}
	
	/**
     * ������ݿ�·������������ڣ��򴴽��������
     * @param    name
     * @param    mode
     * @param    factory
     */
    @Override
    public File getDatabasePath(String name) {
        //�ж��Ƿ����sd��
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if(!sdExist){//���������,
            L.e("SD��������", "SD�������ڣ������SD��");
            return null;
        } 
        else{//�������
            //��ȡsd��·��
            String dbDir=SQL.sql_path;//���ݿ�����Ŀ¼
            String dbPath = dbDir+"/"+SQL.sql_name;//���ݿ�·��
            //�ж�Ŀ¼�Ƿ���ڣ��������򴴽���Ŀ¼
            File dirFile = new File(dbDir);
            if(!dirFile.exists())
                dirFile.mkdirs();
            //���ݿ��ļ��Ƿ񴴽��ɹ�
            boolean isFileCreateSuccess = false; 
            //�ж��ļ��Ƿ���ڣ��������򴴽����ļ�
            File dbFile = new File(dbPath);
            if(!dbFile.exists()){
                try {                    
                    isFileCreateSuccess = dbFile.createNewFile();//�����ļ�
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else     
                isFileCreateSuccess = true;
            //�������ݿ��ļ�����
            if(isFileCreateSuccess)
                return dbFile;
            else 
                return null;
        }
    }
 
    /**
     * ���������������������SD���ϵ����ݿ�ģ�android 2.3�����»�������������
     * 
     * @param    name
     * @param    mode
     * @param    factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, 
            SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }
    
    /**
     * Android 4.0����ô˷�����ȡ���ݿ⡣
     * 
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int, 
     *              android.database.sqlite.SQLiteDatabase.CursorFactory,
     *              android.database.DatabaseErrorHandler)
     * @param    name
     * @param    mode
     * @param    factory
     * @param     errorHandler
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
            DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }

}