package com.internet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.internet.myui.TopBar;
import com.internet.netget.R;

public class AlterAct extends Activity {
	private TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTopBar();
		content = (TextView) findViewById(R.id.text_content);
		content.setText("易览通提醒：\n请即刻进行扫码采集");
	}

	private void setTopBar() {
		TopBar topBar = (TopBar) findViewById(R.id.topBar);
		topBar.hiddenLeftButton(true);
		topBar.hiddenRightButton(true);
		topBar.setTitle(getResources().getString(R.string.title));
	}
}
