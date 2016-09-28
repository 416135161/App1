package com.internet.tools;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.internet.netget.R;

public class NormalUtil {
	public static String getRootDir() {
		if (isHasSdcard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/YLTbak/";
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/";
		}
	}

	public static void deletePath() {
		String path;
		if (isHasSdcard()) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/YLTbak";
		} else {
			path = Environment.getDataDirectory().getAbsolutePath() + "";
		}

		try {
			FileUtil.del(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSystemTime() {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM-dd-HH-mm-ss");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	public static void displayFrameworkBugMessageAndExit(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.app_name));
		builder.setMessage(activity
				.getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.sure, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
		builder.show();
	}

	public static void displayMessage(Context context, String message) {
		Toast.makeText(context.getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}

	public static DisplayMetrics getDM(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * 获取存储的公共数据
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getPreference(Context context, String key) {
		return context.getSharedPreferences(Constants.SPNAME,
				Context.MODE_PRIVATE).getString(key, "");
	}

	public static void setPreference(Context context, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SPNAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static void delPreference(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SPNAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().remove(key).commit();
	}

	public static void clearPreference(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SPNAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().clear().commit();
	}
}
