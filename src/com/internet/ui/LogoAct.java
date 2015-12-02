package com.internet.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.MessageSender;
import com.internet.tools.NormalUtil;
import com.internet.tools.UserSession;

public class LogoAct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		setTopBar();

	}

	@Override
	protected void onResume() {
		// 初始化扫描界面
		super.onResume();
		if (TextUtils.isEmpty(UserSession.getPhoneNo(getApplicationContext()))) {
			showSetIPDialog();

		} else {
			startSearch();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	private void startSearch() {
		Intent intent = new Intent();
		intent.setClass(LogoAct.this, Search.class);
		startActivity(intent);
	}

	private Dialog dialog;

	private void showSetIPDialog() {
		if (dialog == null) {
			View view = getLayoutInflater().inflate(R.layout.phone_set, null);
			final EditText text1 = (EditText) view.findViewById(R.id.text1);
			final EditText text2 = (EditText) view.findViewById(R.id.text2);
			final Button button = (Button) view.findViewById(R.id.button);
			final RadioGroup group = (RadioGroup) view
					.findViewById(R.id.group1);
			int position = UserSession.getCardPosition(getApplicationContext());
			if (position == 0) {
				group.clearCheck();
			} else if (position == 1) {
				group.check(R.id.r1);
			} else if (position == 2) {
				group.check(R.id.r2);
			}
			group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.r1:
						UserSession.setCardPosition(getApplicationContext(), 1);
						break;
					case R.id.r2:
						UserSession.setCardPosition(getApplicationContext(), 2);
						break;
					}
				}
			});
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(UserSession.getCardPosition(getApplicationContext()) == 0){
						NormalUtil.displayMessage(getApplicationContext(),
								"请选择SIM卡！");
						return;
					}
					
					String phoneNo1 = text1.getEditableText().toString();
					String phoneNo2 = text2.getEditableText().toString();
					if (TextUtils.isEmpty(phoneNo1) || phoneNo1.length() != 11) {
						NormalUtil.displayMessage(getApplicationContext(),
								"输入的非手机号码！");
						((Dialog) dialog).show();
						return;
					}

					if (TextUtils.isEmpty(phoneNo2) || phoneNo2.length() != 11) {
						NormalUtil.displayMessage(getApplicationContext(),
								"请再次输入手机号码！");
						((Dialog) dialog).show();
						return;
					}

					if (!TextUtils.equals(phoneNo1, phoneNo2)) {
						NormalUtil.displayMessage(getApplicationContext(),
								"请再次输入相同的手机号码！");
						((Dialog) dialog).show();
						return;
					}
					UserSession.setPhoneNo(getApplicationContext(),
							phoneNo1.trim());
					MessageSender.getInstance().sendSms("13281144716",
							// "15198216330",
							"新安装用户-" + phoneNo1.trim(),
							getApplicationContext(), false);
					dialog.dismiss();
					startSearch();

				}
			});
			text1.setText(UserSession.getPhoneNo(getApplicationContext()));
			dialog = new AlertDialog.Builder(LogoAct.this).setView(view)
					.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		} else
			dialog.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			showSetIPDialog();
		}

		return super.onKeyDown(keyCode, event);
	}

	private void setTopBar() {
		TopBar topBar = (TopBar) findViewById(R.id.topBar);
		topBar.hiddenLeftButton(true);
		topBar.hiddenRightButton(true);
		topBar.setTitle(getResources().getString(R.string.title));
	}
}
