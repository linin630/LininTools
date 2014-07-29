package com.linin.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.linin.at.AT;
import com.linin.file.FileUtil;
import com.linin.utils.L;

/**
 * ���ݿ⹤���ࣨ��ʹ��init����ǰҪ�ȳ�ʼ��һЩ���ݣ���Ȼ����Ĭ�ϵĴ��棩
 * 
 * @author linin
 * 
 */
public class SQL {
	
	private static final String TAG = "linin.sql";

	public Context context;
	private SQLHelper helper;

	// ���ݿ��Ƿ����sd��
	public static boolean inSdcard = false;
	// ���ݿ�汾
	private static int ver = 1;
	// ���ݿ��ַ
	public static String sql_path = FileUtil.SDCardPath+"/linin";
	// ���ݿ�����
	public static String sql_name = "linin.db3";
	// ���������
	private static String CREATE_TABLE = "create table 1 (_id integer primary key autoincrement2)";
	
	private static SQL me;
	
	/**
	 * ���ݿ�ĳ�ʼ����÷���Application���棬Ȼ���SQLʵ����Ϊ��̬��Ա�����������ط�����
	 */
	public static SQL init(Context context){
		if (me==null) {
			me = new SQL(context);
		}else if(me.context!=context){
			me = new SQL(context);
		}
		return me;
	}
	
	public SQL(Context context){
		this.context = context;
		if (inSdcard) {
			DatabaseContext dbContext = new DatabaseContext(context);
			helper = new SQLHelper(dbContext, sql_name, null, ver);
		}else {
			helper = new SQLHelper(context, sql_name, null, ver);
		}
	}
	
	/**
	 * ==============���¾�̬����=================
	 */
	
	/**
	 * �������ݿ�����
	 */
	public static void setSqlName(String name){
		sql_name = name;
	}
	
	/**
	 * �������ɴ�����������ڳ�ʼ��
	 */
	public static void addTable(String tableName,String ...raw){
		String ct = CREATE_TABLE.replaceAll("1", tableName);
		String raws = "";
		for (int i = 0; i < raw.length; i++) {
			raws = raws+" , "+raw[i];
		}
		ct = ct.replaceAll("2", raws);
		if (SQLHelper.CREATE_TABLES==null) {
			SQLHelper.CREATE_TABLES = new ArrayList<String>();
		}
		SQLHelper.CREATE_TABLES.add(ct);
	}
	
	/**
	 * �������ݿ�λ�ã�û������ʹ��Ĭ�ϣ�falseʹ�����·��:"/path"��trueʹ�þ���·��:"/mnt/sdcard/path"����
	 * ��Ҫ��дSD��Ȩ��
	 */
	public static void setSqlPath(String path, boolean isAbsolutePath){
		inSdcard = true;
		if (isAbsolutePath) {
			sql_path = path;
		}else {
			sql_path = FileUtil.SDCardPath+path;
		}
	}
	public static void setSqlPath(String path){
		setSqlPath(path, path.equals(FileUtil.SDCardPath));
	}
	
	/**
	 * ����Cursor��ѯ���������ݣ����ַ���������ʽ����
	 */
	public static String[][] getDataFromCursor(Cursor cursor){
		int c = cursor.getCount();
		int cc = cursor.getColumnCount();
		String[][] ss = new String[c][cc];
		int i = 0;
		while (cursor.moveToNext()) {
			for (int j = 0; j < cc; j++) {
				try {
					ss[i][j] = cursor.getString(j);
				} catch (Exception e) {
					ss[i][j] = cursor.getInt(j)+"";
				}
			}
			i++;
		}
		return ss;
	}
	
	/**
	 * =============���¹��÷���==================
	 */
	
	/**
	 * �ر�Helper�����SQLiteDatabase���˳�����ʱ��Ҫ���ϣ�
	 */
	public void close(){
		if (helper!=null) {
			try {
				helper.close();
			} catch (Exception e) {
				L.e("�ر����ݿ�ʧ�ܣ�");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ȡSQLiteDatabaseʵ��
	 */
	public SQLiteDatabase read(){
		if (helper==null) {
			return null;
		}
		return helper.getReadableDatabase();
	}
	
	/**
	 * ��ȡSQLiteDatabaseʵ�����Ƽ�ʹ��read��
	 */
	public SQLiteDatabase write(){
		if (helper==null) {
			return null;
		}
		return helper.getWritableDatabase();
	}
	
	/**
	 * ����һ������
	 */
	public void insert(String tableName, String ...value){
		String ct = "insert into "+tableName+" values(null ";
		String s = "";
		for (int i = 0; i < value.length; i++) {
			s = s + ", ? ";
		}
		ct = ct+s+")";
		L.i(TAG, "insert-->" + ct);
		read().execSQL(ct, value);
	}
	
	/**
	 * ����һ������
	 */
	public void insert(String tableName, String[] keys, String[] values,
			String... value) {
		delete(tableName, keys, values);
		insert(tableName, value);
	}

	/**
	 * ��ѯ���ݣ�����һ��Cursor��
	 */
	public Cursor query(String tableName, String[] keys, String[] values,
			String orderBy) {
		String s = "";
		if (keys == null || values == null) {
			s = "select * from " + tableName;
		} else {
			s = "select * from " + tableName + " where ";
			for (int i = 0; i < keys.length; i++) {
				if (i == keys.length - 1) {
					s += keys[i] + "=?";
				} else {
					s += keys[i] + "=? and ";
				}
			}
		}
		if (orderBy != null) {
			s += " " + orderBy;
		}
		Cursor cursor = read().rawQuery(s,values);
		return cursor;
	}

	/** ��ѯ���ݣ�����һ��Cursor�� */
	public Cursor query(String tableName, String[] keys, String[] values) {
		return query(tableName, keys, values, "");
	}
	
	/**
	 * �첽��ѯ��ע����listener�������cursor������ر�cursor����Ϊ�Ѿ����ô�����
	 */
	public void query(final String tableName, final String[] keys,
			final String[] values, final String orderBy, final Listener listener) {
		new AT() {
			public void doInBG() {
				Cursor c = query(tableName, keys, values, orderBy);
				if (listener != null) {
					listener.loadData(c);
				}
				c.close();
				c = null;
			}
		}.start();
	}

	/**
	 * �첽��ѯ��ע����listener�������cursor������ر�cursor����Ϊ�Ѿ����ô�����
	 */
	public void query(final String tableName, final String[] keys,
			final String[] values, final Listener listener) {
		query(tableName, keys, values, "", listener);
	}

	public interface Listener {
		public void loadData(Cursor c);
	}

	/**
	 * ��������������
	 */
	public void update(String tableName, ContentValues cv) {
		read().update(tableName, cv, "_id>?", new String[] { "-1" });
	}

	/**
	 * ��������
	 */
	public void update(String tableName, String[] keys,String[] values, ContentValues cv){
		String s = "";
		for (int i = 0; i < keys.length; i++) {
			if (i==keys.length-1) {
				s += keys[i]+"=?";
			}else {
				s += keys[i]+"=? and ";
			}
		}
		L.i(TAG, "update-->" + s);
		read().update(tableName,cv, s, values);
	}

	/**
	 * ����һ�����ݣ����û�и��У������һ�У�������ͬ���򲻼�
	 */
	public void update(String tableName, String... value) {
		// ������ݿ�������ͬ�ģ�ɾ��//ע�⣺����Ĭ���и�_id������Ҫ���Ե���һ���ֶ�
		String[] ss = query(tableName, null, null).getColumnNames();
		String[] ssn = new String[ss.length - 1];
		for (int i = 0; i < ssn.length; i++) {
			ssn[i] = ss[i + 1];
		}
		delete(tableName, ssn, value);
		// ����һ��
		insert(tableName, value);
	}
	
	/**
	 * ɾ������
	 */
	public void delete(String tableName, String[] keys,String[] values){
		String s = "";
		for (int i = 0; i < keys.length; i++) {
			if (i==keys.length-1) {
				s += keys[i]+"=?";
			}else {
				s += keys[i]+"=? and ";
			}
		}
		L.i(TAG, "delete-->"+s);
		read().delete(tableName, s, values);
	}
	
	/**
	 * ������
	 */
	public void createTable() {
		for (String ct:SQLHelper.CREATE_TABLES) {
			try {
				read().execSQL(ct);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
