package com.internet.db;

import com.internet.tools.NormalUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 8;
	// 本地数据库的名字
	public static final String DATA_NAME = "sms_data.db";// 聊天记录信息表
	// 巡检项表
	public static final String TABLE_SMS = "table_sms";

	// 巡检项类型表

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context) {
		this(context, NormalUtil.getRootDir() + DATA_NAME, VERSION);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sqlStr1 = "create table "
				+ TABLE_SMS
				+ "(id text primary key not null, body text, date text,"
				+ " phoneNo text, photoPath text, tag text, info text, photo text, location text, isImgUp text)";
		db.execSQL(sqlStr1);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("DatabaseHelper onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
		onCreate(db);

	}
}
