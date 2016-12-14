package com.internet.tools;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpUtil {

	public static final String SERVER_ADDRESS = "http://125.67.237.234:8080/";

	RequestQueue mQueue;

	private static HttpUtil instance;

	private HttpUtil() {

	}

	public static HttpUtil getInstance() {
		if (instance == null) {
			instance = new HttpUtil();
		}
		return instance;
	}

	public void addRequest(StringRequest request, Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
		}
		mQueue.add(request);
	}


}
