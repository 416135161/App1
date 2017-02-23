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
	private String photo;
	
	private String info; //扫描到的内容
	
	private String location;

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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
