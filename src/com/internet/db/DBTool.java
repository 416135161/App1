package com.internet.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.internet.tools.FileUtil;

public class DBTool {
	private static DBTool dbTool;

	private DBTool() {

	}

	public static DBTool getInstance() {
		if (dbTool == null)
			dbTool = new DBTool();
		return dbTool;

	}

	public synchronized List<MessageItem> getSavedMessage(Context context) {
		List<MessageItem> list = null;

		SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
		String sql = "SELECT * FROM table_sms ";
		sql += " ORDER BY id DESC";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				if (list == null) {
					list = new ArrayList<MessageItem>();
				}
				MessageItem message = new MessageItem();
				message.setId(Long.valueOf(
						cursor.getString(cursor.getColumnIndex("id")))
						.longValue());
				message.setBody(cursor.getString(cursor.getColumnIndex("body")));
				message.setDate(cursor.getString(cursor.getColumnIndex("date")));
				message.setPhoneNo(cursor.getString(cursor
						.getColumnIndex("phoneNo")));
				message.setPhotoPath(cursor.getString(cursor
						.getColumnIndex("photoPath")));
				list.add(message);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return list;
	}

	public synchronized void saveMessage(Context context, MessageItem item) {
		if (item == null)
			return;
		String photoPath = null;
		SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.clear();
		contentValues.put("id", System.currentTimeMillis());
		contentValues.put("phoneNo", item.getPhoneNo());
		contentValues.put("body", item.getBody());
		contentValues.put("date", item.getDate());
		System.out.println(contentValues.get("date"));
		contentValues.put("photoPath", item.getPhotoPath());
		db.insert(DatabaseHelper.TABLE_SMS, null, contentValues);
		Cursor cursor = db.rawQuery("SELECT * FROM table_sms ", null);
		int count = cursor.getCount();
		if (count > 99) {
			cursor.moveToFirst();
			photoPath = cursor.getString(cursor.getColumnIndex("photoPath"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			db.delete(DatabaseHelper.TABLE_SMS, "id = ?", new String[] { id
					+ "" });

		}
		db.close();
		if (photoPath != null) {
			FileUtil.deleteFile(photoPath);
		}
	}

	public synchronized void clearTimeout(Context context) {
		String sql = "SELECT *  FROM table_sms WHERE date(table_sms.date) < strftime( '%Y-%m-%d', date('now', '-3 month')) ";
		List<MessageItem> list = null;
		SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				if (list == null) {
					list = new ArrayList<MessageItem>();
				}
				MessageItem message = new MessageItem();
				message.setBody(cursor.getString(cursor.getColumnIndex("body")));
				message.setDate(cursor.getString(cursor.getColumnIndex("date")));
				message.setPhoneNo(cursor.getString(cursor
						.getColumnIndex("phoneNo")));
				message.setPhotoPath(cursor.getString(cursor
						.getColumnIndex("photoPath")));

				list.add(message);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		if (list != null) {
			for (MessageItem item : list) {
				FileUtil.deleteFile(item.getPhotoPath());
			}
		}

		String delsql = "delete  FROM table_sms WHERE date(table_sms.date) < strftime( '%Y-%m-%d', date('now', '-3 month')) ";
		new DatabaseHelper(context).getWritableDatabase().execSQL(delsql);

	}

	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		return sdf.format(System.currentTimeMillis());
	}
}
