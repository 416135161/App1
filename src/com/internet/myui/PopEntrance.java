package com.internet.myui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.internet.netget.R;
import com.internet.ui.Search;
import com.internet.ui.TravelAct;

public class PopEntrance {

	private static PopEntrance instance;

	private PopEntrance() {
		// TODO Auto-generated constructor stub
	}

	public static PopEntrance getInstance() {
		if (instance == null)
			instance = new PopEntrance();
		return instance;
	}

	private PopupWindow pop;
	private View layout;
	private ImageView image_title;
	private TextView text_title, text_1, text_2, text_3, text_4, text_5,
			text_6;
	private View view5, view6;
	private Drawable drawable;

	public void initPop_Tables(final Activity context, int index) {
		if (pop == null) {
			layout = context.getLayoutInflater().inflate(R.layout.entrance_pop,
					null);
			text_title = (TextView) layout.findViewById(R.id.text_title);
			image_title = (ImageView) layout.findViewById(R.id.image_title);
			text_1 = (TextView) layout.findViewById(R.id.text_1);
			text_2 = (TextView) layout.findViewById(R.id.text_2);
			text_3 = (TextView) layout.findViewById(R.id.text_3);
			text_4 = (TextView) layout.findViewById(R.id.text_4);
			text_5 = (TextView) layout.findViewById(R.id.text_5);
			text_6 = (TextView) layout.findViewById(R.id.text_6);
			view5 = layout.findViewById(R.id.view_5);
			view6 = layout.findViewById(R.id.view_6);
			pop = new PopupWindow(layout,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			pop.setOutsideTouchable(false);
		}
		switch (index) {
		case 2:
			image_title.setImageResource(R.drawable.ylt);
			text_title.setText("管理更轻松");

			text_1.setText("云端查岗");
			text_2.setText("平安盾甲");
			text_3.setText("校园卫士");
			text_4.setText("e护身符");
			text_5.setVisibility(View.GONE);
			text_6.setVisibility(View.GONE);
			view5.setVisibility(View.GONE);
			view6.setVisibility(View.GONE);
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closePop();
					Intent intent = new Intent();
					intent.setClass(context, Search.class);
					context.startActivity(intent);
				}
			});
			break;
		case 5:
			image_title.setImageResource(R.drawable.yltx);
			text_title.setVisibility(View.GONE);

			text_1.setText("景区门票");
			text_2.setText("酒店住宿");
			text_3.setText("交通票务");
			text_4.setText("用车泊车");
			text_5.setText("反季节度假");
			text_6.setText("目的地接送");
			text_5.setVisibility(View.VISIBLE);
			text_6.setVisibility(View.VISIBLE);
			view5.setVisibility(View.VISIBLE);
			view6.setVisibility(View.VISIBLE);
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closePop();
					Intent intent = new Intent();
					intent.setClass(context, TravelAct.class);
					context.startActivity(intent);
				}
			});
			break;
		}

	}

	public boolean isShow() {
		if (pop != null && pop.isShowing())
			return true;
		return false;

	}

	public void show(View v) {
		if (pop != null && !pop.isShowing())
			pop.showAsDropDown(v, 20, -250);
		else
			pop.dismiss();
	}

	public void closePop() {
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
		}
	}
}
