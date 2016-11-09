package com.internet.db;

import android.text.TextUtils;

public class MessageItem implements java.io.Serializable

{

	private static final long serialVersionUID = 1L;
	private long id;
	private String body;
	private String phoneNo;
	private String date;
	private String photoPath;
	private String tag;

	public MessageItem() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getTag() {
		if (TextUtils.isEmpty(tag))
			tag = "-";
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
