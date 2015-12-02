package com.internet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.internet.service.WatchService;

/**
 * 服务开机自动启动
 * 
 * @author Administrator
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(intent.getAction());
		WatchService.actionReschedule(context);
	}
}
