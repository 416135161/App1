package com.internet.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
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

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 判断是否为今天(效率比较高)
	 * 
	 * @param day
	 *            传入的 时间 "2016-06-28 10:10:30" "2016-06-28" 都可以
	 * @return true今天 false不是
	 * @throws ParseException
	 */
	public static boolean IsToday(String day) {

		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = null;
		try {
			date = getDateFormat().parse(day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 0) {
				return true;
			}
		}
		return false;
	}

	public static SimpleDateFormat getDateFormat() {
		if (null == DateLocal.get()) {
			DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
		}
		return DateLocal.get();
	}

	private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
}
