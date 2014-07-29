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
 * 数据库工具类（在使用init方法前要先初始化一些数据，不然会用默认的代替）
 * 
 * @author linin
 * 
 */
public class SQL {
	
	private static final String TAG = "linin.sql";

	public Context context;
	private SQLHelper helper;

	// 数据库是否放在sd卡
	public static boolean inSdcard = false;
	// 数据库版本
	private static int ver = 1;
	// 数据库地址
	public static String sql_path = FileUtil.SDCardPath+"/linin";
	// 数据库名字
	public static String sql_name = "linin.db3";
	// 创建表语句
	private static String CREATE_TABLE = "create table 1 (_id integer primary key autoincrement2)";
	
	private static SQL me;
	
	/**
	 * 数据库的初始化最好放在Application里面，然后把SQL实例设为静态成员可以在其他地方引用
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
	 * ==============以下静态方法=================
	 */
	
	/**
	 * 设置数据库名字
	 */
	public static void setSqlName(String name){
		sql_name = name;
	}
	
	/**
	 * 创建表，可创建多个表，用于初始化
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
	 * 设置数据库位置，没设置则使用默认（false使用相对路径:"/path"，true使用绝对路径:"/mnt/sdcard/path"）；
	 * 需要读写SD卡权限
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
	 * 根据Cursor查询出所有数据，以字符串数组形式返回
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
	 * =============以下公用方法==================
	 */
	
	/**
	 * 关闭Helper里面的SQLiteDatabase（退出程序时需要加上）
	 */
	public void close(){
		if (helper!=null) {
			try {
				helper.close();
			} catch (Exception e) {
				L.e("关闭数据库失败！");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取SQLiteDatabase实例
	 */
	public SQLiteDatabase read(){
		if (helper==null) {
			return null;
		}
		return helper.getReadableDatabase();
	}
	
	/**
	 * 获取SQLiteDatabase实例（推荐使用read）
	 */
	public SQLiteDatabase write(){
		if (helper==null) {
			return null;
		}
		return helper.getWritableDatabase();
	}
	
	/**
	 * 插入一行数据
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
	 * 插入一行数据
	 */
	public void insert(String tableName, String[] keys, String[] values,
			String... value) {
		delete(tableName, keys, values);
		insert(tableName, value);
	}

	/**
	 * 查询数据（返回一个Cursor）
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

	/** 查询数据（返回一个Cursor） */
	public Cursor query(String tableName, String[] keys, String[] values) {
		return query(tableName, keys, values, "");
	}
	
	/**
	 * 异步查询，注意在listener里操作完cursor后无需关闭cursor，因为已经做好处理了
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
	 * 异步查询，注意在listener里操作完cursor后无需关闭cursor，因为已经做好处理了
	 */
	public void query(final String tableName, final String[] keys,
			final String[] values, final Listener listener) {
		query(tableName, keys, values, "", listener);
	}

	public interface Listener {
		public void loadData(Cursor c);
	}

	/**
	 * 更新所有行数据
	 */
	public void update(String tableName, ContentValues cv) {
		read().update(tableName, cv, "_id>?", new String[] { "-1" });
	}

	/**
	 * 更新数据
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
	 * 更新一行数据，如果没有该行，则添加一行；已有相同的则不加
	 */
	public void update(String tableName, String... value) {
		// 如果数据库已有相同的，删了//注意：由于默认有个_id，所以要忽略掉第一个字段
		String[] ss = query(tableName, null, null).getColumnNames();
		String[] ssn = new String[ss.length - 1];
		for (int i = 0; i < ssn.length; i++) {
			ssn[i] = ss[i + 1];
		}
		delete(tableName, ssn, value);
		// 加上一行
		insert(tableName, value);
	}
	
	/**
	 * 删除数据
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
	 * 创建表
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
