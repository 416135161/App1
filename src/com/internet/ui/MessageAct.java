package com.internet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.internet.db.MessageItem;
import com.internet.myui.TopBar;
import com.internet.netget.R;

public class MessageAct extends Activity {
	private TextView data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);
		setTopBar();
		data = (TextView) findViewById(R.id.data);
		MessageItem message = (MessageItem) getIntent().getSerializableExtra(
				"message");
		data.setText(message.getDate() + "\n" + message.getBody());
	}

	private void setTopBar() {
		TopBar topBar = (TopBar) findViewById(R.id.topBar);
		topBar.hiddenLeftButton(true);
		topBar.hiddenRightButton(true);
		topBar.setTitle(getResources().getString(R.string.title));
	}
}
