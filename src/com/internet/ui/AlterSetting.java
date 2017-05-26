package com.internet.ui;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.internet.intrface.TopBarClickListener;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.Constants;
import com.internet.tools.NormalUtil;
import com.internet.tools.SecretKeyTool;
import com.internet.tools.UserSession;

public class AlterSetting extends Activity implements OnCheckedChangeListener,
		RadioGroup.OnCheckedChangeListener, OnClickListener {
	private final int TIME_DIALOG1 = 1;

	private final int TIME_DIALOG2 = 2;

	private final int TIME_DIALOG3 = 3;

	private final int TIME_DIALOG4 = 4;

	private final int TIME_DIALOG5 = 5;

	private Calendar c = null;

	private String time = null;

	private TextView text1, text2, text4, text3, text5;

	private RadioGroup group1, group2, group3, group4, group5;

	private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;

	private CheckBox check_send_msg;

	private Button mBtnSendReport, mBtnResetNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alter_setting);
		setTopBar();

		check_send_msg = (CheckBox) findViewById(R.id.check_send_msg);
		check_send_msg.setChecked(UserSession
				.getCheckSendMsg(getApplicationContext()));
		check_send_msg.setOnCheckedChangeListener(this);

		checkBox1 = (CheckBox) findViewById(R.id.btn1);
		checkBox2 = (CheckBox) findViewById(R.id.btn2);
		checkBox3 = (CheckBox) findViewById(R.id.btn3);
		checkBox4 = (CheckBox) findViewById(R.id.btn4);
		checkBox5 = (CheckBox) findViewById(R.id.btn5);

		text1 = (TextView) findViewById(R.id.edit1);
		text1.setOnClickListener(this);
		text2 = (TextView) findViewById(R.id.edit2);
		text2.setOnClickListener(this);
		text3 = (TextView) findViewById(R.id.edit3);
		text3.setOnClickListener(this);
		text4 = (TextView) findViewById(R.id.edit4);
		text4.setOnClickListener(this);
		text5 = (TextView) findViewById(R.id.edit5);
		text5.setOnClickListener(this);
		if (!UserSession.getTime(getApplicationContext(), 1).equals(""))
			text1.setText(UserSession.getTime(getApplicationContext(), 1));
		if (!UserSession.getTime(getApplicationContext(), 2).equals(""))
			text2.setText(UserSession.getTime(getApplicationContext(), 2));
		if (!UserSession.getTime(getApplicationContext(), 3).equals(""))
			text3.setText(UserSession.getTime(getApplicationContext(), 3));
		if (!UserSession.getTime(getApplicationContext(), 4).equals(""))
			text4.setText(UserSession.getTime(getApplicationContext(), 4));
		if (!UserSession.getTime(getApplicationContext(), 5).equals(""))
			text5.setText(UserSession.getTime(getApplicationContext(), 5));

		group1 = (RadioGroup) this.findViewById(R.id.group1);
		group1.setOnCheckedChangeListener(this);
		group2 = (RadioGroup) this.findViewById(R.id.group2);
		group2.setOnCheckedChangeListener(this);
		group3 = (RadioGroup) this.findViewById(R.id.group3);
		group3.setOnCheckedChangeListener(this);
		group4 = (RadioGroup) this.findViewById(R.id.group4);
		group4.setOnCheckedChangeListener(this);
		group5 = (RadioGroup) this.findViewById(R.id.group5);
		group5.setOnCheckedChangeListener(this);

		initRadioButton(group1, 1);
		initRadioButton(group2, 2);
		initRadioButton(group3, 3);
		initRadioButton(group4, 4);
		initRadioButton(group5, 5);

		checkBox1.setOnCheckedChangeListener(this);
		checkBox2.setOnCheckedChangeListener(this);
		checkBox3.setOnCheckedChangeListener(this);
		checkBox4.setOnCheckedChangeListener(this);
		checkBox5.setOnCheckedChangeListener(this);

		checkBox1.setChecked(UserSession.getCheck(getApplicationContext(), 1));
		checkBox2.setChecked(UserSession.getCheck(getApplicationContext(), 2));
		checkBox3.setChecked(UserSession.getCheck(getApplicationContext(), 3));
		checkBox4.setChecked(UserSession.getCheck(getApplicationContext(), 4));
		checkBox5.setChecked(UserSession.getCheck(getApplicationContext(), 5));

		mBtnSendReport = (Button) findViewById(R.id.btn_send_report);
		mBtnSendReport.setOnClickListener(this);

		mBtnResetNo = (Button) findViewById(R.id.btn_reset_no);
		mBtnResetNo.setOnClickListener(this);

		if (TextUtils.isEmpty(UserSession
				.getSendReportPhoneNo(getApplicationContext())))
			mBtnResetNo.setVisibility(View.GONE);
		else
			mBtnResetNo.setVisibility(View.VISIBLE);

		// setGoneBtn();
	}

	private void initRadioButton(RadioGroup group, int index) {
		int type = UserSession.getType(getApplicationContext(), index);
		switch (type) {
		case Constants.NO:
			group.check(-1);
			break;
		case Constants.WEEK:
			if (index == 1)
				group.check(R.id.r11);
			else if (index == 2)
				group.check(R.id.r21);
			else if (index == 3)
				group.check(R.id.r31);
			else if (index == 4)
				group.check(R.id.r41);
			else if (index == 5)
				group.check(R.id.r51);
			break;
		case Constants.WEEKEND:
			if (index == 1)
				group.check(R.id.r12);
			else if (index == 2)
				group.check(R.id.r22);
			else if (index == 3)
				group.check(R.id.r32);
			else if (index == 4)
				group.check(R.id.r42);
			else if (index == 5)
				group.check(R.id.r52);
			break;
		case Constants.EVERYDAY:
			if (index == 1)
				group.check(R.id.r13);
			else if (index == 2)
				group.check(R.id.r23);
			else if (index == 3)
				group.check(R.id.r33);
			else if (index == 4)
				group.check(R.id.r43);
			else if (index == 5)
				group.check(R.id.r53);
			break;
		}
	}

	private void refreshCalendar(int index, String hint) {
		c = Calendar.getInstance();
		time = UserSession.getTime(getApplicationContext(), index);
		if (!time.equals("")) {
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
			c.set(Calendar.MINUTE, Integer.parseInt(time.substring(3, 5)));
			System.out.println("3333333333");
		} else {
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hint.substring(0, 2)));
			c.set(Calendar.MINUTE, Integer.parseInt(hint.substring(3, 5)));
			System.out.println("44444444444444444");
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {

		case TIME_DIALOG1:
			refreshCalendar(1, (String) text1.getText());
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							text1.setText(transformStyle(hourOfDay) + ":"
									+ transformStyle(minute));
							UserSession.setTime(getApplicationContext(),
									(String) text1.getText(), 1);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true);
			break;
		case TIME_DIALOG2:
			refreshCalendar(2, (String) text2.getText());
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							text2.setText(transformStyle(hourOfDay) + ":"
									+ transformStyle(minute));
							UserSession.setTime(getApplicationContext(),
									(String) text2.getText(), 2);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true);
			break;
		case TIME_DIALOG3:
			refreshCalendar(3, (String) text3.getText());
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							text3.setText(transformStyle(hourOfDay) + ":"
									+ transformStyle(minute));
							UserSession.setTime(getApplicationContext(),
									(String) text3.getText(), 3);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true);
			break;
		case TIME_DIALOG4:
			refreshCalendar(4, (String) text4.getText());
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							text4.setText(transformStyle(hourOfDay) + ":"
									+ transformStyle(minute));
							UserSession.setTime(getApplicationContext(),
									(String) text4.getText(), 4);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true);
			break;
		case TIME_DIALOG5:
			refreshCalendar(5, (String) text5.getText());
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							text5.setText(transformStyle(hourOfDay) + ":"
									+ transformStyle(minute));
							UserSession.setTime(getApplicationContext(),
									(String) text5.getText(), 5);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true);
			break;

		}
		return dialog;
	}

	private String transformStyle(int i) {
		if (i < 10)
			return "0" + i;
		return i + "";
	}

	private void setTopBar() {
		TopBar topBar = (TopBar) findViewById(R.id.topBar);
		topBar.hiddenLeftButton(true);
		topBar.hiddenRightButton(false);
		topBar.setRightDrawable(R.drawable.close);
		topBar.setTopBarClickListener(new TopBarClickListener() {

			@Override
			public void rightBtnClick() {

				finish();

			}

			@Override
			public void leftBtnClick() {

			}
		});
		topBar.setTitle(getResources().getString(R.string.title));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edit1:
			showDialog(TIME_DIALOG1);
			break;
		case R.id.edit2:
			showDialog(TIME_DIALOG2);
			break;
		case R.id.edit3:
			showDialog(TIME_DIALOG3);
			break;
		case R.id.edit4:
			showDialog(TIME_DIALOG4);
			break;
		case R.id.edit5:
			showDialog(TIME_DIALOG5);
			break;
		case R.id.btn_send_report:
			if (TextUtils.isEmpty(UserSession
					.getSendReportPhoneNo(getApplicationContext())))
				showSendReportPhoneDlg();
			else
				startActivity(new Intent(AlterSetting.this,
						MessageListAct.class));
			break;
		case R.id.btn_reset_no:
			showSendReportPhoneDlg();
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		System.out.println(checkedId);
		switch (checkedId) {
		case R.id.r11:
			if (!checkBox1.isChecked()) {
				group1.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEK, 1);
			break;
		case R.id.r12:
			if (!checkBox1.isChecked()) {
				group1.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEKEND, 1);
			break;
		case R.id.r13:
			if (!checkBox1.isChecked()) {
				group1.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.EVERYDAY, 1);
			break;
		case R.id.r21:
			if (!checkBox2.isChecked()) {
				group2.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEK, 2);
			break;
		case R.id.r22:
			if (!checkBox2.isChecked()) {
				group2.clearCheck();
				return;
			}

			UserSession.setType(getApplicationContext(), Constants.WEEKEND, 2);
			break;
		case R.id.r23:
			if (!checkBox2.isChecked()) {
				group2.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.EVERYDAY, 2);
			break;
		case R.id.r31:
			if (!checkBox3.isChecked()) {
				group3.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEK, 3);
			break;
		case R.id.r32:
			if (!checkBox3.isChecked()) {
				group3.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEKEND, 3);
			break;
		case R.id.r33:
			if (!checkBox3.isChecked()) {
				group3.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.EVERYDAY, 3);
			break;
		case R.id.r41:
			if (!checkBox4.isChecked()) {
				group4.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEK, 4);
			break;
		case R.id.r42:
			if (!checkBox4.isChecked()) {
				group4.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEKEND, 4);
			break;
		case R.id.r43:
			if (!checkBox4.isChecked()) {
				group4.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.EVERYDAY, 4);
			break;
		case R.id.r51:
			if (!checkBox5.isChecked()) {
				group5.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEK, 5);
			break;
		case R.id.r52:
			if (!checkBox5.isChecked()) {
				group5.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.WEEKEND, 5);
			break;
		case R.id.r53:
			if (!checkBox5.isChecked()) {
				group5.clearCheck();
				return;
			}
			UserSession.setType(getApplicationContext(), Constants.EVERYDAY, 5);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

		switch (buttonView.getId()) {
		case R.id.btn1:
			if (!isChecked) {
				UserSession.setType(getApplicationContext(), Constants.NO, 1);
				group1.clearCheck();
			} else {
				((RadioButton) group1.getChildAt(0)).setChecked(true);
			}
			UserSession.setCheck(getApplicationContext(), isChecked, 1);
			break;
		case R.id.btn2:
			if (!isChecked) {
				UserSession.setType(getApplicationContext(), Constants.NO, 2);
				group2.clearCheck();
			} else {
				((RadioButton) group2.getChildAt(0)).setChecked(true);
			}
			UserSession.setCheck(getApplicationContext(), isChecked, 2);
			break;
		case R.id.btn3:
			if (!isChecked) {
				UserSession.setType(getApplicationContext(), Constants.NO, 3);
				group3.clearCheck();
			} else {
				((RadioButton) group3.getChildAt(0)).setChecked(true);
			}
			UserSession.setCheck(getApplicationContext(), isChecked, 3);
			break;
		case R.id.btn4:
			if (!isChecked) {
				UserSession.setType(getApplicationContext(), Constants.NO, 4);
				group4.clearCheck();
			} else {
				((RadioButton) group4.getChildAt(0)).setChecked(true);
			}
			UserSession.setCheck(getApplicationContext(), isChecked, 4);
			break;
		case R.id.btn5:
			if (!isChecked) {
				UserSession.setType(getApplicationContext(), Constants.NO, 5);
				group5.clearCheck();
			} else {
				((RadioButton) group5.getChildAt(0)).setChecked(true);
			}
			UserSession.setCheck(getApplicationContext(), isChecked, 5);
			break;

		case R.id.check_send_msg:
			UserSession.setCheckSendMeg(getApplicationContext(), isChecked);
		}
	}

	private Dialog dialog;

	private void showSendReportPhoneDlg() {
		if (dialog == null) {
			View view = getLayoutInflater().inflate(R.layout.send_report_phone,
					null);
			final EditText text1 = (EditText) view.findViewById(R.id.text1);
			final EditText text2 = (EditText) view.findViewById(R.id.text2);
			final EditText text3 = (EditText) view.findViewById(R.id.text3);
			final Button button = (Button) view.findViewById(R.id.button);
			final Button btnCancle = (Button) view
					.findViewById(R.id.button_cancle);

			btnCancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String phoneNo1 = text1.getEditableText().toString();
					String phoneNo2 = text2.getEditableText().toString();
					String secretKey = text3.getEditableText().toString();
					if (TextUtils.isEmpty(phoneNo1) || phoneNo1.length() != 11) {
						NormalUtil.displayMessage(getApplicationContext(),
								"输入的非手机号码！");
						dialog.show();
						return;
					}

					if (TextUtils.isEmpty(phoneNo2) || phoneNo2.length() != 11) {
						NormalUtil.displayMessage(getApplicationContext(),
								"重新输入的非手机号码！");
						dialog.show();
						return;
					}

					if (!TextUtils.equals(phoneNo1, phoneNo2)) {
						NormalUtil.displayMessage(getApplicationContext(),
								"请两次输入相同的手机号码！");
						dialog.show();
						return;
					}

					if (!SecretKeyTool.isSecretKeyIn(secretKey)) {
						NormalUtil.displayMessage(getApplicationContext(),
								"请输入正确的秘钥！");
						dialog.show();
						return;
					}

					UserSession.setSendReportPhoneNo(getApplicationContext(),
							phoneNo1);
					startActivity(new Intent(AlterSetting.this,
							MessageListAct.class));
					dialog.dismiss();
				}
			});
			text1.setText(UserSession.getPhoneNo(getApplicationContext()));
			dialog = new AlertDialog.Builder(AlterSetting.this)
					.setTitle("汇报手机号码").setView(view).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		} else
			dialog.show();

	}

	private void setGoneBtn() {
		((ViewGroup) check_send_msg.getParent()).setVisibility(View.GONE);
		mBtnSendReport.setVisibility(View.GONE);
		mBtnResetNo.setVisibility(View.GONE);
	}

}
