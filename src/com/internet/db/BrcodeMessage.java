package com.internet.db;

import android.graphics.Bitmap;

public class BrcodeMessage {
	private String content;
	private Bitmap barcode;
	public BrcodeMessage() {
		// TODO Auto-generated constructor stub
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Bitmap getBarcode() {
		return barcode;
	}
	public void setBarcode(Bitmap barcode) {
		this.barcode = barcode;
	}
	
	
}
