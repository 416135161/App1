package com.internet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import com.internet.myui.TopBar;
import com.internet.netget.R;

public class OkAct extends Activity {
	private TextView content, text_info;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			finish();
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
		text_info.setText( "\n" + info );
	}

	
	private void startAnimation(){
		Animation animation = new AlphaAnimation(1f, 0.0f);
		animation.setDuration(1000);
		animation.setInterpolator(this,
				android.R.anim.anticipate_interpolator);
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
}
