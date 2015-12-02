package com.internet.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

import com.internet.myui.PopEntrance;
import com.internet.netget.R;

public class EntranceAct extends Activity {
	private View cover;
	private View tempView;
	private TextView mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrance);

		initView();
		cover = findViewById(R.id.cover);
		mButton = (TextView) findViewById(R.id.mButton);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (cover != null)
			cover.setVisibility(View.GONE);
		if (tempView != null)
			tempView.setVisibility(View.VISIBLE);
		PopEntrance.getInstance().closePop();

	}

	private void initView() {
		inflateInstance("攒位揽客更轻松", R.drawable.yltn, R.id.text_1,
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		inflateInstance("管理更轻松", R.drawable.ylt, R.id.text_2,
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						cover.setVisibility(View.VISIBLE);
						v.setVisibility(View.INVISIBLE);
						tempView = v;
						PopEntrance.getInstance().initPop_Tables(
								EntranceAct.this, 2);
						PopEntrance.getInstance().show(v);
						setBottonText("www.eposton.com");
						return true;
					}
				});
		inflateInstance("游遍我华夏", R.drawable.yltx, R.id.text_3,
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						cover.setVisibility(View.VISIBLE);
						v.setVisibility(View.INVISIBLE);
						tempView = v;
						PopEntrance.getInstance().initPop_Tables(
								EntranceAct.this, 5);
						PopEntrance.getInstance().show(v);
						setBottonText("www.netsget.com");
						return true;
					}
				});
		inflateInstance("实惠时刻订", R.drawable.skd, R.id.text_4,
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		inflateInstance("e镖天下足", R.drawable.ypj, R.id.text_5,
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		
		inflateInstance("任挑选", R.drawable.zcwy, R.id.text_6,
				new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});

	}

	private void inflateInstance(String name, int iconId, int textId,
			OnLongClickListener onLongClickListener) {
		TextView textView = (TextView) findViewById(textId);
		textView.setText(name);
		Drawable drawable = getResources().getDrawable(iconId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		textView.setCompoundDrawables(null, drawable, null, null);
		textView.setOnLongClickListener(onLongClickListener);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (PopEntrance.getInstance().isShow()) {
				cover.setVisibility(View.GONE);
				tempView.setVisibility(View.VISIBLE);
				PopEntrance.getInstance().closePop();
				setBottonText("");
			}

		}
		return super.dispatchTouchEvent(ev);
	}
	
	private void setBottonText(String text){
		mButton.setText(text);
	}

}
