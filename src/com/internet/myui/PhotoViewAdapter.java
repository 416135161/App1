package com.internet.myui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.internet.tools.ImageUtil;

public class PhotoViewAdapter extends BaseAdapter {
	/* 声明变量 */
	private Context context;
	private Activity activity;
	private List<String> list;
//	private int width;
//	private int height;

	/* ImageAdapter的构造符 */
	public PhotoViewAdapter(Activity activity, List<String> li, int w, int h) {
		this.activity = activity;
		list = li;
//		this.width = w;
//		this.height = h;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			ImageView imageView = new ImageView(activity);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					Gallery.LayoutParams.FILL_PARENT,
					Gallery.LayoutParams.FILL_PARENT));
			// 设置缩放样式：将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
//			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			convertView=imageView;	
			holder.image = (ImageView) convertView;
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

//		Bitmap bitmap = ImageUtil.getSmallImageBitmapFromSD(context,
//				list.get(position), 100000);
		Bitmap bitmap=ImageUtil.getImageBitmapByPath(context, list.get(position));
		holder.image.setImageBitmap(bitmap);
//		holder.image.setBackgroundResource(R.drawable.gallery_img_bg);

		return convertView;
	}

	class Holder {
		ImageView image;
	}
}
