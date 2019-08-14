package com.internet.ui;

import com.internet.intrface.TopBarClickListener;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.HttpUtil;
import com.internet.tools.UserSession;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HostActivity extends Activity {
	EditText mEtIp, mEtPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host_set);
		setTopBar();
		mEtIp = (EditText) findViewById(R.id.et_ip);
		mEtPort = (EditText) findViewById(R.id.et_port);
		
		mEtIp.setText(UserSession.getIp(this));
		mEtPort.setText(UserSession.getPort(this));
		TextView btnSave = (TextView) findViewById(R.id.btn_save);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserSession.setIp(HostActivity.this, mEtIp.getEditableText()
						.toString());
				UserSession.setPort(HostActivity.this, mEtPort.getEditableText()
						.toString());
				((TextView)findViewById(R.id.tv_host)).setText(HttpUtil.getInstance().getHost(HostActivity.this));
			}

		});
		
		((TextView)findViewById(R.id.tv_host)).setText(HttpUtil.getInstance().getHost(this));

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
}
