package com.internet.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.telephony.SmsManager;
import android.text.TextUtils;

/**
 * 发送短信类
 * 
 * @project 3GAMP
 * 
 * @date 2012-8-2
 * 
 * @version 1.0
 * 
 * @author lilinfeng@cdsf.com
 * 
 * @copyright http://www.cdsf.com
 * 
 */
public class MessageSender {

	// 监听短信发送的广播action
	public static final String ACTION_SEND_MSG = "com.sifang.android.action.sms.send";
	public static final String ACTION_SEND_MSG_COMPLETE = "com.sifang.android.action.sms.send.complete";
	private SmsManager smsManager;
	// private Intent msgSendIntent;
	// private PendingIntent sendPendingIntent;
	// private Context context;
	// MessageSender实例
	private static final MessageSender sender = new MessageSender();

	// private static final String LOG_TAG = "MessageSender";

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	private MessageSender() {
		smsManager = SmsManager.getDefault();
	}

	/**
	 * 获取MessageSender实例
	 * 
	 * @return MessageSender 实例
	 */
	public static MessageSender getInstance() {
		return sender;
	}

	/**
	 * 设置Context
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		// this.context = context;
	}

	public void sendSms(String custNums, String msgContent, Context context,
			boolean isCheck) {
//		if (isCheck) {
//			if (!UserSession.getCheckSendMsg(context))
//				return;
//		}
		new SendSmsThread(custNums, msgContent, null).start();
		Date date = new java.util.Date();
		msgContent = "*" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "*";
		String scAddress = UserSession.getPhoneNo(context);
		if (TextUtils.isEmpty(scAddress))
			scAddress = null;
		new SendSmsThread(custNums, msgContent, scAddress).start();

	}

	private class SendSmsThread extends Thread {

		private String custTeleNums;
		private String scAddress;
		private String msgContent;

		public SendSmsThread(String custTeleNums, String msgContent,
				String scAddress) {
			this.custTeleNums = custTeleNums;
			this.scAddress = scAddress;
			this.msgContent = msgContent;
		}

		@Override
		public void run() {
			List<String> msgs = smsManager.divideMessage(msgContent);
			// 可能会出现短信送达地址错误
			try {
				for (String text : msgs) {
					// if (context == null) {
					smsManager.sendTextMessage(custTeleNums, scAddress, text,
							null, null);
					// } else {
					// msgSendIntent = new Intent();
					// msgSendIntent.putExtra("cust_mobile", custTeleNums);
					// sendPendingIntent = PendingIntent.getBroadcast(context,
					// 0, msgSendIntent, 0);
					// smsManager.sendTextMessage(custTeleNums, null, text,
					// null, null);
					// msgSendIntent.setAction(ACTION_SEND_MSG);
					// context.sendBroadcast(msgSendIntent);
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
