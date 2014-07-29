package com.linin.sql;

import java.util.ArrayList;
import java.util.List;

import com.linin.utils.L;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper{
	
	public static List<String> CREATE_TABLES = new ArrayList<String>();

	public SQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String ct:CREATE_TABLES) {
			db.execSQL(ct);
		}
		L.i("SQLHelper-->onCreate!!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		L.i("SQLHelper-->onUpgrade!!Ver:"+oldVersion+" to "+newVersion);
	}

}
