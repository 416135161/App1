package com.internet.tools;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpUtil {

//	public static final String SERVER_ADDRESS = "http://182.140.244.38:8088/";
	public static final String SERVER_ADDRESS1 = "http://www.elanton.mobi:8088/";
//	public static final String SERVER_ADDRESS = "http://192.168.90.198:8080/";

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
		request.setRetryPolicy(new DefaultRetryPolicy(
                10000, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mQueue.add(request);
	}
	
	public String getHost(Context context){
		String host= "http://" + UserSession.getIp(context) + ":" + UserSession.getPort(context) + "/";
		return host;	
	}


}
