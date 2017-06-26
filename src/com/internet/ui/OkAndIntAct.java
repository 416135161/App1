package com.internet.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.internet.db.DBTool;
import com.internet.db.MessageItem;
import com.internet.entity.SendBean;
import com.internet.entity.SendBean.ContentItem;
import com.internet.entity.SendBean.ImgItem;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.HttpUtil;
import com.internet.tools.JsonUtil;
import com.internet.tools.NormalUtil;

public class OkAndIntAct extends Activity {
	private TextView content, text_info;
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			netSend();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTopBar();
		text_info = (TextView) findViewById(R.id.text_info);
		content = (TextView) findViewById(R.id.text_content);
		startAnimation();
		String info = getIntent().getStringExtra("info");
	}

	private void startAnimation() {
		Animation animation = new AlphaAnimation(1f, 0.0f);
		animation.setDuration(1000);
		animation.setInterpolator(this, android.R.anim.anticipate_interpolator);
		animation.setRepeatCount(2);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				 handler.sendEmptyMessageDelayed(0, 500);
			}
		});
		content.startAnimation(animation);
	}

	private void setTopBar() {
		TopBar topBar = (TopBar) findViewById(R.id.topBar);
		topBar.hiddenLeftButton(true);
		topBar.hiddenRightButton(true);
		topBar.setTitle(getResources().getString(R.string.title));
	}

	private void netSend() {
		SendBean sendBean = new SendBean();
		sendBean.setPhone("19500000000");
		sendBean.setRecivephone("15900000000");
		sendBean.setComdate(System.currentTimeMillis() + "");
		sendBean.setContents(getNetSendInfo());

		sendInternet(JsonUtil.objectToJson(sendBean), this);

	}
	
	private ImgItem itemImg;

	public ArrayList<ContentItem> getNetSendInfo() {
		ArrayList<ContentItem> contents = new ArrayList<ContentItem>();
		List<MessageItem> mItems = DBTool.getInstance().getSavedMessage(
				getApplicationContext());
		MessageItem messageItem = mItems.get(0);
		ContentItem item = new ContentItem();
		item.setDate(messageItem.getDate());
		item.setTag(messageItem.getTag());
		item.setImgPath(messageItem.getPhotoPath());
		item.setInfo(messageItem.getInfo());
		item.setLocation(messageItem.getLocation());
		contents.add(item);
		
		
		itemImg = new ImgItem();
		itemImg.setImgPath(messageItem.getPhotoPath());
		itemImg.setImg(messageItem.getPhoto());
		itemImg.setId(messageItem.getId());
		return contents;
	}

	private void sendInternet(final String sendData, final Context context) {
		showWaitDialog();
		System.out.println(sendData);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				HttpUtil.SERVER_ADDRESS + "app/comment/add.do",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						uploadImg(itemImg, context);
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

	private void uploadImg(final ImgItem sendData, Context context) {
		System.out.println(sendData.getImg());
		
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				HttpUtil.SERVER_ADDRESS + "app/comment/updateImg.do",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						closeWaitDialog();
						NormalUtil.displayMessage(getApplicationContext(),
								"发送数据成功");
						finish();
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

	private Dialog retryDialog;

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
		retryDialog = new AlertDialog.Builder(this).setTitle("重新传送")
				.setView(view).create();
		retryDialog.setCanceledOnTouchOutside(true);
		retryDialog.show();
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
}