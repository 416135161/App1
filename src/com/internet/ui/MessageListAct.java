package com.internet.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.internet.db.DBTool;
import com.internet.db.MessageItem;
import com.internet.intrface.TopBarClickListener;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.MessageSender;
import com.internet.tools.UserSession;

public class MessageListAct extends Activity implements OnClickListener {
	private ListView listView;
	private View dataView;
	private TextView noData;
	private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtnSend;
	private List<MessageItem> mItems;
	private MyAdapter adapter;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
			if (adapter.getCount() == 0) {
				noData.setVisibility(View.VISIBLE);
				dataView.setVisibility(View.GONE);
			} else {
				noData.setVisibility(View.INVISIBLE);
				dataView.setVisibility(View.VISIBLE);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTopBar();
		dataView = findViewById(R.id.data_view);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		noData = (TextView) findViewById(R.id.nodata);
		mBtn1 = (Button) findViewById(R.id.btn_1);
		mBtn2 = (Button) findViewById(R.id.btn_2);
		mBtn3 = (Button) findViewById(R.id.btn_3);
		mBtn4 = (Button) findViewById(R.id.btn_4);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtn1.setOnClickListener(this);
		mBtn2.setOnClickListener(this);
		mBtn3.setOnClickListener(this);
		mBtn4.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initListView();
	}

	private void initListView() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mItems = DBTool.getInstance().getSavedMessage(
						getApplicationContext());
				System.out.println("JJJJJJJJJJJJJJJJJJ");
				if (mItems != null) {
					adapter.setData(mItems);
				}
				mHandler.sendEmptyMessage(0);
			}

		}).start();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_1:
			List<MessageItem> items = new ArrayList<MessageItem>();
			items.add(mItems.get(0));
			adapter.setData(items);
			adapter.notifyDataSetChanged();
			break;
		case R.id.btn_2:
			setDataByDay(7);
			break;
		case R.id.btn_3:
			setDataByDay(30);
			break;
		case R.id.btn_4:
			adapter.setData(mItems);
			adapter.notifyDataSetChanged();
			break;
		case R.id.btn_send:
			String sendInfo = adapter.getSendInfo();
			System.out.println("LLL:" + sendInfo);
			MessageSender.getInstance().sendSms(
					UserSession.getSendReportPhoneNo(getApplicationContext()),
					sendInfo, getApplicationContext(), false);
			Intent intent = new Intent();
			intent.setClass(this, OkAct.class);
			intent.putExtra("info", "发送报表成功！");
			startActivity(intent);
			break;
		}
	}

	private void setDataByDay(long day) {
		List<MessageItem> mSubItems = new ArrayList<MessageItem>();
		long time = day * 24 * 60 * 60 * 1000;
		for (int i = 0; i < mItems.size(); i++) {
			MessageItem item = mItems.get(i);
			if (item.getId() > (System.currentTimeMillis() - time)) {
				mSubItems.add(item);
			}

		}
		adapter.setData(mSubItems);
		adapter.notifyDataSetChanged();
	}

	class MyAdapter extends BaseAdapter {
		private List<MessageItem> items = new ArrayList<MessageItem>();

		public void setData(List<MessageItem> tasks) {
			this.items.clear();
			this.items.addAll(tasks);

		}

		public String getSendInfo() {
			String temp = "*";
			for (int i = 0; i < items.size(); i++) {
				temp += (items.get(i).getDate() + "*");
			}
			return temp;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			MessageItem item = this.items.get(position);
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.message_item, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView.findViewById(R.id.text1);
				holder.text2 = (TextView) convertView.findViewById(R.id.text2);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText((position + 1) + "");
			holder.text2.setText(item.getDate());

			return convertView;

		}

	}

	class ViewHolder {
		TextView text1;
		TextView text2;

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
