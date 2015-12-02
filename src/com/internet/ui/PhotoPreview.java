package com.internet.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.internet.intrface.TopBarClickListener;
import com.internet.myui.MyGallery;
import com.internet.myui.PhotoViewAdapter;
import com.internet.myui.TopBar;
import com.internet.netget.R;
import com.internet.tools.FileUtil;
import com.internet.tools.GetSDImage;
import com.internet.tools.NormalUtil;

public class PhotoPreview extends Activity implements OnClickListener {

	private boolean keepOne = false;
	private int countSetAdapter = 0;;
	private int height = 0;
	private int height2 = 0;
	private int countDraw = 0;
	private String path = null;
	private List<String> list = null;

	private ImageView leftButton = null;
	private ImageView rightButton = null;
	private ImageView deleteButton = null;
	private Button cancleButton = null;
	private MyGallery gallery = null;
	private RelativeLayout bottom = null;
	private PhotoViewAdapter galleryAdapter = null;
	private TextView name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photoview);
		setTopBar();
		name = (TextView) findViewById(R.id.name);
		leftButton = (ImageView) findViewById(R.id.photoview_leftbtn);
		rightButton = (ImageView) findViewById(R.id.photoview_rightbtn);
		deleteButton = (ImageView) findViewById(R.id.photoview_delete);
		cancleButton = (Button) findViewById(R.id.photoview_cancelbtn);
		gallery = (MyGallery) findViewById(R.id.photoview_gallery);
		bottom = (RelativeLayout) findViewById(R.id.photoview_bottom);

		// 图片点击事件监听
		gallery.setOnItemSelectedListener(new GalleryItemChangedListener());
		Intent intent = getIntent();
		if (!intent.getBooleanExtra("delete", true)) {
			deleteButton.setVisibility(View.INVISIBLE);
		}

		// DisplayMetrics dm=NormalUtil.getDM(PhotoPreview.this);
		// LayoutParams params=gallery.getLayoutParams();
		// if(dm.heightPixels>=800){
		// params.height=dm.heightPixels/2;
		// }
		// else{
		// params.height=dm.heightPixels/3;
		// }
		// gallery.setLayoutParams(params);

		ViewTreeObserver observer = gallery.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				int tempheight = gallery.getMeasuredHeight();
				if (height < tempheight) {
					height = tempheight;
				}
				countDraw++;
				if (height > 0 && countDraw == 1) {
					// DisplayMetrics dm=NormalUtil.getDM(PhotoPreview.this);
					// int totalHeight=height+height2;
					// if(totalHeight<dm.heightPixels-100){
					// }
					reSetHeight();
				}
				return true;
			}
		});
		ViewTreeObserver observer2 = bottom.getViewTreeObserver();
		observer2.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				height2 = bottom.getMeasuredHeight();
				return true;
			}
		});

		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		path = getIntent().getStringExtra("path");
		keepOne = getIntent().getBooleanExtra("keepone", false);

		setGalleyAdapter();
		cancleButton.setText(list.size() + "");

	}
	

	private void setGallerySelection(int pos) {
		if (list.size() > 0) {
			if (list.size() > pos) {
				gallery.setSelection(pos);
			} else {
				pos--;
				setGallerySelection(pos);
			}
		}
	}

	private void reSetHeight() {
		int totalHeight = height + height2;
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.photoview);
		LayoutParams params = layout.getLayoutParams();
		DisplayMetrics dm = NormalUtil.getDM(this);

		if (totalHeight < dm.heightPixels - 100) {
			totalHeight += 60;
		}
		params.height = totalHeight;
		layout.setLayoutParams(params);
	}

	// 为拖动图片控件设置适配器
	private void setGalleyAdapter() {

		DisplayMetrics dm = NormalUtil.getDM(this);
		list = GetSDImage.getImgPathList(path);

		if (list.size() == 0) {
			// deleteButton.setEnabled(false);
			finish();
		} else if (list.size() == 1) {
			leftButton.setEnabled(false);
			rightButton.setEnabled(false);
			leftButton.setVisibility(View.INVISIBLE);
			rightButton.setVisibility(View.INVISIBLE);
		}
		galleryAdapter = new PhotoViewAdapter(this, list, dm.widthPixels,
				dm.heightPixels);
		gallery.setAdapter(galleryAdapter);
		if (countSetAdapter == 0) {
			gallery.setSelection(getIntent().getIntExtra("selection", 0));

		}
		countSetAdapter++;
	}

	class GalleryItemChangedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			if (list.size() != 1 && list.size() != 0) {
				if (position == 0) {
					leftButton.setEnabled(false);
					rightButton.setEnabled(true);
					leftButton.setVisibility(View.INVISIBLE);
					rightButton.setVisibility(View.VISIBLE);
				} else if (position == list.size() - 1) {
					rightButton.setEnabled(false);
					leftButton.setEnabled(true);
					rightButton.setVisibility(View.INVISIBLE);
					leftButton.setVisibility(View.VISIBLE);
				} else {
					leftButton.setVisibility(View.VISIBLE);
					rightButton.setVisibility(View.VISIBLE);
					leftButton.setEnabled(true);
					rightButton.setEnabled(true);
				}
			}
			String photoPath = list.get(position);
			name.setText(photoPath.substring(photoPath.lastIndexOf("/") + 1));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.photoview_leftbtn:
			rightButton.setEnabled(true);
			int currentPosition = gallery.getSelectedItemPosition();
			if (currentPosition == 1) {
				gallery.setSelection(currentPosition - 1);
				leftButton.setEnabled(false);
			} else {
				gallery.setSelection(currentPosition - 1);
			}
			break;
		case R.id.photoview_rightbtn:
			leftButton.setEnabled(true);
			int currentPosition2 = gallery.getSelectedItemPosition();
			if (currentPosition2 == list.size() - 2) {
				gallery.setSelection(currentPosition2 + 1);
				rightButton.setEnabled(false);
			} else {
				gallery.setSelection(currentPosition2 + 1);
			}
			break;
		case R.id.photoview_cancelbtn:
			finish();
			break;
		case R.id.photoview_delete:
			if (keepOne && list.size() == 1) {
				NormalUtil.displayMessage(getApplicationContext(),
						"该项至少需要一张图片！");
				return;
			}
			int pos = gallery.getSelectedItemPosition();
			boolean delSuc = FileUtil.deleteFile(list.get(pos));
			if (delSuc) {
				setGalleyAdapter();
				setGallerySelection(pos);
				NormalUtil.displayMessage(getApplicationContext(), "删除成功！");
			}

			break;
		}
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
