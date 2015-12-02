package com.internet.service;

import java.util.Calendar;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;

import com.internet.tools.Constants;
import com.internet.tools.UserSession;
import com.internet.ui.AlterAct;

public class CommitService extends Service {

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, CommitService.class);
		context.startService(intent);
	}

	public static void actionStop(Context context) {
		Intent intent = new Intent(context, CommitService.class);
		context.stopService(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		registerDateTransReceiver();
		return super.onStartCommand(intent, flags, startId);
	}

	private MyReceive myReceive;

	private void registerDateTransReceiver() {

		if (myReceive == null) {
			myReceive = new MyReceive();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.setPriority(1000);
			registerReceiver(myReceive, filter);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (myReceive != null)
			unregisterReceiver(myReceive);
		super.onDestroy();

	}

	class MyReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (TextUtils.equals(action, Intent.ACTION_TIME_TICK)) {
				goToWork();
			}

		}
	}

	private void goToWork() {
		int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		String currentTime = transformStyle(hourOfDay) + ":"
				+ transformStyle(minute);
		for (int i = 1; i < 6; i++) {
			boolean check = UserSession.getCheck(getApplicationContext(), i);
			if (!check)
				continue;
			int type = UserSession.getType(getApplicationContext(), i);
			String time = UserSession.getTime(getApplicationContext(), i);
			System.out.println("Time:" + time);
			if (time.equals(""))
				continue;
			if (type == Constants.WEEK) {
				if (week >= 2 && week <= 6 && time.equals(currentTime)) {
					invokeAlterAct();
					return;
				}
			} else if (type == Constants.WEEKEND) {
				if ((week == 1 || week <= 7) && time.equals(currentTime)) {
					invokeAlterAct();
					return;
				}
			} else if (type == Constants.EVERYDAY) {
				if (time.equals(currentTime)) {
					invokeAlterAct();
					return;
				}
			}
		}
		System.out.println("currentTime:" + currentTime);

	}

	private void invokeAlterAct() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(getApplicationContext(), AlterAct.class);
		startActivity(intent);
	}

	private String transformStyle(int i) {
		if (i < 10)
			return "0" + i;
		return i + "";
	}
}
