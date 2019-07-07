package com.internet.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.internet.db.DBTool;
import com.internet.db.MessageItem;
import com.internet.entity.SendBean;
import com.internet.entity.SendBean.ContentItem;
import com.internet.entity.SendBean.ImgItem;
import com.internet.intrface.TopBarClickListener;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.FileUtil;
import com.internet.tools.HttpUtil;
import com.internet.tools.JsonUtil;
import com.internet.tools.MessageSender;
import com.internet.tools.NormalUtil;
import com.internet.tools.UserSession;

public class MessageListAct extends Activity implements OnClickListener {
	private ListView listView;
	private View dataView;
	private TextView noData;
	private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtnSend, mBtnClean;
	private List<MessageItem> mItems;
	private MyAdapter adapter;
	private Dialog deleteDialog;

	private Dialog retryDialog;
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
		mBtnClean = (Button) findViewById(R.id.btn_clean);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtn1.setOnClickListener(this);
		mBtn2.setOnClickListener(this);
		mBtn3.setOnClickListener(this);
		mBtn4.setOnClickListener(this);
		mBtnClean.setOnClickListener(this);
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
				System.out.println(JsonUtil.objectToJson(mItems));
				adapter.setData(mItems);
				mHandler.sendEmptyMessage(0);
			}

		}).start();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_1:
			setDataByToday();
			break;
		case R.id.btn_2:
			setDataByWeek();
			break;
		case R.id.btn_3:
			setDataByMonth();
			break;
		case R.id.btn_4:
			adapter.setData(mItems);
			adapter.notifyDataSetChanged();
			break;
		case R.id.btn_clean:
			View view = getLayoutInflater().inflate(R.layout.dlg_delete, null);
			final Button button = (Button) view.findViewById(R.id.button);
			final Button btnCancle = (Button) view
					.findViewById(R.id.button_cancle);
			view.findViewById(R.id.text_tip).setVisibility(View.VISIBLE);

			btnCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteDialog.dismiss();
				}
			});
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DBTool.getInstance().deleteAll(MessageListAct.this);
					NormalUtil.deletePath();
					initListView();
					deleteDialog.dismiss();
				}
			});
			deleteDialog = new AlertDialog.Builder(this).setTitle("删除提示")
					.setView(view).create();
			deleteDialog.setCanceledOnTouchOutside(true);
			deleteDialog.show();
			break;
		case R.id.btn_send:
			showSendDialog();
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

	private void setDataByToday() {
		List<MessageItem> mSubItems = new ArrayList<MessageItem>();
		for (int i = 0; i < mItems.size(); i++) {
			MessageItem item = mItems.get(i);
			if (NormalUtil.IsToday(item.getDate())) {
				mSubItems.add(item);
			}
		}
		adapter.setData(mSubItems);
		adapter.notifyDataSetChanged();

	}

	private void setDataByWeek() {
		List<MessageItem> mSubItems = new ArrayList<MessageItem>();
		for (int i = 0; i < mItems.size(); i++) {
			MessageItem item = mItems.get(i);
			if (NormalUtil.isSameWeekWithToday(item.getDate())) {
				mSubItems.add(item);
			}
		}
		adapter.setData(mSubItems);
		adapter.notifyDataSetChanged();

	}

	private void setDataByMonth() {
		List<MessageItem> mSubItems = new ArrayList<MessageItem>();
		for (int i = 0; i < mItems.size(); i++) {
			MessageItem item = mItems.get(i);
			if (NormalUtil.isSameMonthWithToday(item.getDate())) {
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
			if (tasks != null && !tasks.isEmpty())
				this.items.addAll(tasks);

		}

		public ArrayList<ContentItem> getNetSendInfo() {
			ArrayList<ContentItem> contents = new ArrayList<ContentItem>();
			for (MessageItem messageItem : items) {
				ContentItem item = new ContentItem();
				item.setDate(messageItem.getDate());
				item.setTag(messageItem.getTag());
				item.setImgPath(messageItem.getPhotoPath());
				item.setInfo(messageItem.getInfo());
				item.setLocation(messageItem.getLocation());
				contents.add(item);
			}
			return contents;
		}

		public ArrayList<ImgItem> getNetImg() {
			ArrayList<ImgItem> contents = new ArrayList<ImgItem>();
			for (MessageItem messageItem : items) {
				if (TextUtils.equals(messageItem.getIsImgUp(), "0")) {
					ImgItem item = new ImgItem();
					item.setImgPath(messageItem.getPhotoPath());
					item.setImg(messageItem.getPhoto());
					item.setId(messageItem.getId());
					contents.add(item);

					System.out.println("WWWWWWWWWWWWWWWWWWWWWww");
				}

				System.out.println("JJJJJJJJJJJJJJJJJJJJJ");
			}
			return contents;
		}

		public List<String> getSendInfo() {

			List<String> temps = new ArrayList<String>();
			int size = items.size();
			int length = 6;
			int aaa = size / length + 1;
			int hhh = size % length;
			for (int i = 0; i < aaa; i++) {
				int ccc;
				if (i == aaa - 1) {
					ccc = hhh;
				} else {
					ccc = length;
				}
				String temp = "*";
				for (int j = 0; j < ccc; j++) {
					MessageItem item = items.get(i * length + j);
					if (item.getTag() != "")
						temp += (item.getDate() + " " + item.getTag() + "*");
					else
						temp += (item.getDate() + "*");
				}
				temps.add(temp);
			}
			return temps;
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
				holder.text3 = (TextView) convertView.findViewById(R.id.text3);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText((getCount() - position) + "");
			holder.text2.setText(item.getDate());
			holder.text3.setText(item.getTag());
			return convertView;

		}

	}

	class ViewHolder {
		TextView text1;
		TextView text2;
		TextView text3;
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

	private void sendInternet(final String sendData, Context context) {
//		sendInstallState(UserSession.getMyPhone(context), context);
		
		showWaitDialog();
		System.out.println(sendData);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				HttpUtil.getInstance().getHost(this) + "appapi_c/uploadCheckInRecord",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						dealImg();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Log.e("TAG", error.getMessage(), error);
						closeWaitDialog();
						NormalUtil.displayMessage(getApplicationContext(),
								"发送数据失败，请检查网络");

						retry();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put("data", sendData);
				return map;
			}
			
		};

		HttpUtil.getInstance().addRequest(stringRequest, context);
	}

	private ArrayList<ImgItem> sendImgs;
	private int index = 0;

	private void dealImg() {
		sendImgs = adapter.getNetImg();
		if (sendImgs != null && sendImgs.size() > 0) {
			if (index < sendImgs.size()) {
				uploadImg(sendImgs.get(index), this);
			} else {
				closeWaitDialog();
				initListView();
				NormalUtil.displayMessage(getApplicationContext(), "发送数据成功");
			}
		} else {
			closeWaitDialog();
			initListView();
			NormalUtil.displayMessage(getApplicationContext(), "发送数据成功");
		}

	}

	private void uploadImg(final ImgItem sendData, Context context) {
		System.out.println(sendData.getImg());
		// try {
		// FileUtil.writeStringToFile(NormalUtil.getRootDir() +
		// "222.txt", sendData.getImg());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				HttpUtil.getInstance().getHost(this) + "appapi_c/uploadImg",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						index++;
						dealImg();
						DBTool.getInstance().updatePhotoState(
								getApplicationContext(), sendData.getId());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Log.e("TAG", error.getMessage(), error);
						closeWaitDialog();
						NormalUtil.displayMessage(getApplicationContext(),
								"发送数据失败，请检查网络");

						retry();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put("img", sendData.getImg());
				map.put("imgPath", sendData.getImgPath());
				return map;
			}
		};

		HttpUtil.getInstance().addRequest(stringRequest, context);
	}

	private Dialog dialog;

	private void showSetMyPhoneDlg() {
		if (dialog == null) {
			View view = getLayoutInflater()
					.inflate(R.layout.set_my_phone, null);
			final EditText text1 = (EditText) view.findViewById(R.id.text1);
			final EditText text2 = (EditText) view.findViewById(R.id.text2);
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
					UserSession.setMyPhone(getApplicationContext(), phoneNo1);
//					sendInstallState(phoneNo1, getApplicationContext());
					dialog.dismiss();
					 netSend();
				}
			});
			dialog = new AlertDialog.Builder(this).setTitle("本机手机号码").setView(view).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		} else
			dialog.show();

	}

	private Dialog waitDialog;

	private void showWaitDialog() {
		if (waitDialog == null) {
			View view = getLayoutInflater().inflate(
					R.layout.common_dialog_loading_layout, null);
			waitDialog = new AlertDialog.Builder(this).setView(view).create();
			waitDialog.setCanceledOnTouchOutside(false);
			waitDialog.show();
		} else
			waitDialog.show();
	}

	private void closeWaitDialog() {
		if (waitDialog != null && waitDialog.isShowing())
			waitDialog.dismiss();
	}

	AlertDialog sendDialog;

	private void showSendDialog() {

		View view = getLayoutInflater().inflate(R.layout.dlg_send_select, null);
		Button button1 = (Button) view.findViewById(R.id.button1);
		Button button2 = (Button) view.findViewById(R.id.button2);
		view.findViewById(R.id.text_tip).setVisibility(View.VISIBLE);

		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				netSend();
				sendDialog.dismiss();
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				messageSend();
				sendDialog.dismiss();
			}
		});
		sendDialog = new AlertDialog.Builder(this).setTitle("请选择发送方式")
				.setView(view).create();
		sendDialog.show();
	}

	private void messageSend() {
		List<String> sendInfo = adapter.getSendInfo();
		for (String info : sendInfo) {
			MessageSender.getInstance().sendSms(
					UserSession.getSendReportPhoneNo(getApplicationContext()),
					info, getApplicationContext(), false);
			Log.e("LLL:", info);
		}

		Intent intent = new Intent();
		intent.setClass(this, OkAct.class);
		intent.putExtra("info", "发送报表成功！");
		startActivity(intent);
	}

	private void netSend() {
		SendBean sendBean = new SendBean();
		sendBean.setPhone(UserSession.getMyPhone(getApplicationContext()));
		sendBean.setRecivephone(UserSession
				.getSendReportPhoneNo(getApplicationContext()));
		sendBean.setComdate(System.currentTimeMillis() + "");
		sendBean.setContents(adapter.getNetSendInfo());

		sendImgs = null;
		index = 0;
		if (TextUtils.isEmpty(UserSession.getMyPhone(getApplicationContext()))) {
			showSetMyPhoneDlg();
		} else {
			sendInternet(JsonUtil.objectToJson(sendBean), this);
		}
	}

	private void retry() {
		View view = getLayoutInflater().inflate(R.layout.retry_dlg, null);
		final Button button = (Button) view.findViewById(R.id.button);
		final Button btnCancle = (Button) view.findViewById(R.id.button_cancle);
		view.findViewById(R.id.text_tip).setVisibility(View.VISIBLE);

		btnCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				retryDialog.dismiss();
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				netSend();
				retryDialog.dismiss();
			}
		});
		retryDialog = new AlertDialog.Builder(this).setTitle("删除提示")
				.setView(view).create();
		retryDialog.setCanceledOnTouchOutside(true);
		retryDialog.show();
	}

	private void sendInstallState1(final String sendData, Context context) {
		if (UserSession.getInstallState(getApplicationContext()) == 1)
			return;
		System.out.println(sendData);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				HttpUtil.getInstance().getHost(this) + "/appapi_c/uploadCheckInRecord",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						UserSession.setInstallState(getApplicationContext(), 1);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Log.e("TAG", error.getMessage(), error);

					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				// 在这里设置需要post的参数
				Map<String, String> map = new HashMap<String, String>();
				map.put("telephone", sendData);
				return map;
			}
			
			@Override
		    public String getBodyContentType() {
		        return "application/json; charset=utf-8";
		    }
		};

		HttpUtil.getInstance().addRequest(stringRequest, context);
	}

}
