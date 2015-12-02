package com.internet.tools;

import java.io.File;
import java.util.ArrayList;

import android.util.Log;

public class GetSDImage {
	/**
	 * 获取传入的目录路径下所有图片路径List
	 * 
	 * @param path
	 * @return
	 */
	public static ArrayList<String> getImgPathList(String path) {
		ArrayList<String> it = new ArrayList<String>();
		try {
			File f = new File(path);
			File[] files = f.listFiles();
			/* 将所有文件存入List中 */
			if (files.length > 0) {
				Log.d("Image", path + "文件夹下" + files.length + "张图片。");
			}
			for (int i = files.length - 1; i >= 0; i--) {
				File file = files[i];
				// 如果文件为图片，添加到List中
				if (getImageFile(file.getPath()))
					it.add(file.getPath());
			}
		} catch (Exception e) {
			Log.d("getimglist", "查询文件夹不存在！" + path);
		}

		return it;
	}

	// 判断文件是否为图片
	private static boolean getImageFile(String fName) {
		boolean re;

		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 按扩展名的类型决定MimeType */
		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

}
