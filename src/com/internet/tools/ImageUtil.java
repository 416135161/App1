package com.internet.tools;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

/**
 * @author sea 图片相关操作工具类
 */
public class ImageUtil {

	// 根据图片路径获得图片 Bitmap
	public static Bitmap getImageBitmapByPath(Context context, String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}

	// 获得SD卡下图片的缩略图
	public static Bitmap getSmallBitmapFromSD(Context context, String path,
			int maxNumOfPix) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int insampal = computeSampleSize(opts, -1, maxNumOfPix);
		opts.inSampleSize = insampal;
		opts.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(path, opts);
		return bmp;
	}

	public static Bitmap getSmallBitmap(byte[] data, int maxNumOfPix) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int insampal = computeSampleSize(opts, -1, maxNumOfPix);
		opts.inSampleSize = insampal;
		opts.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		return bmp;
	}

	public static Bitmap getSmallBitmap(String path, int width, int heigh) {

		Options op = new Options();

		op.inJustDecodeBounds = true;

		Bitmap bt = BitmapFactory.decodeFile(path, op);

		int xScale = op.outWidth / width;

		int yScale = op.outHeight / heigh;

		op.inSampleSize = xScale > yScale ? xScale : yScale;

		op.inJustDecodeBounds = false;

		bt = BitmapFactory.decodeFile(path, op);

		return bt;

	}

	// 回收图片的方法
	public static void distoryBitmap(Bitmap bitmap) {
		if (null != bitmap && !bitmap.isRecycled())
			bitmap.recycle();
	}

	// 动态计算inSampleSize
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// 把bitmap保存在SD卡指定目录下
	public static void saveBitmapToSDcard(String imgPath, Bitmap mBitmap,
			int quality) {
		File f = null;
		FileOutputStream fOut = null;
		try {
			f = FileUtil.createAbsFile(imgPath);
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
			File file = new File(imgPath);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 顺时针旋转图片
	 * @param rotate角度
	 * @param bitmap
	 * @return
	 */
	public static Bitmap setBitmapRotate(int rotate, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.setRotate(rotate);// 顺时针旋转度
		Bitmap newbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		distoryBitmap(bitmap);
		bitmap = null;
		return newbitmap;
	}
}