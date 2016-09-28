package com.internet.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserSession {
	private static final String TIME_KEY = "time";

	private static final String TYPE_KEY = "type";

	private static final String CHECK_KEY = "check";

	private static final String CHECK_SEND_MSG_KEY = "check_send_msg";

	private static final String PHONE_NO_KEY = "phone_no";

	private static final String SEND_REPORT_PHONE_KEY = "send_report_phone";

	private static final String CARD_POSITION = "card_position";

	private static final String IS_FIRST = "is_first";

	public static int getType(Context context, int index) {
		String type = NormalUtil.getPreference(context, TYPE_KEY + index);
		if (type.equals("")) {
			type = Constants.NO + "";
		}
		return Integer.parseInt(type);
	}

	public static void setType(Context context, int type, int index) {
		NormalUtil.setPreference(context, TYPE_KEY + index, type + "");
	}

	public static void setTime(Context context, String time, int index) {
		NormalUtil.setPreference(context, TIME_KEY + index, time);
	}

	public static String getTime(Context context, int index) {
		return NormalUtil.getPreference(context, TIME_KEY + index);
	}

	public static void setCheck(Context context, boolean isCheck, int index) {
		NormalUtil.setPreference(context, CHECK_KEY + index, (isCheck ? 1 : 0)
				+ "");
	}

	public static boolean getCheck(Context context, int index) {
		String flag = NormalUtil.getPreference(context, CHECK_KEY + index);
		if (flag.equals("") || flag.equals("0"))
			return false;
		return true;
	}

	public static void setCheckSendMeg(Context context, boolean isCheck) {
		NormalUtil.setPreference(context, CHECK_SEND_MSG_KEY, (isCheck ? 1 : 0)
				+ "");
	}

	public static boolean getCheckSendMsg(Context context) {
		String flag = NormalUtil.getPreference(context, CHECK_SEND_MSG_KEY);
		if (flag.equals("") || flag.equals("0"))
			return false;
		return true;
	}

	public static void setPhoneNo(Context context, String phoneNo) {
		NormalUtil.setPreference(context, PHONE_NO_KEY, phoneNo);
	}

	public static String getPhoneNo(Context context) {
		return NormalUtil.getPreference(context, PHONE_NO_KEY);
	}

	public static void setCardPosition(Context context, int position) {
		NormalUtil.setPreference(context, CARD_POSITION, position + "");
	}

	public static int getCardPosition(Context context) {
		String position = NormalUtil.getPreference(context, CARD_POSITION);
		if (TextUtils.isEmpty(position)) {
			return 0;
		}
		return Integer.valueOf(position);
	}

	public static void setSendReportPhoneNo(Context context, String phoneNo) {
		NormalUtil.setPreference(context, SEND_REPORT_PHONE_KEY, phoneNo);
	}

	public static String getSendReportPhoneNo(Context context) {
		return NormalUtil.getPreference(context, SEND_REPORT_PHONE_KEY);
	}

	public static boolean isFirst(Context context) {
		return context.getSharedPreferences(Constants.SPNAME,
				Context.MODE_PRIVATE).getBoolean(IS_FIRST, true);
	}

	public static void setFirstFalse(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SPNAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(IS_FIRST, false).commit();
	}

}
