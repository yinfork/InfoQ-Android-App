package com.yinfork.infoqapp.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.yinfork.infoqapp.MyApplication;

import java.util.Map;

/**
 * Created by Administrator on 2015/10/22.
 */
public class VolleyClient {

    private static StringRequest mStringRequest;

    public static void RequestGet(String url, String tag, VolleyResponse vf) {
        MyApplication.getmRequestQueue().cancelAll(tag);
        mStringRequest = new StringRequest(Request.Method.GET, url, vf.loadLinstener(), vf.errorListener()) {

        };
        MyApplication.getmRequestQueue().add(mStringRequest);
    }

    public static void PcRequestGet(String url, String tag, VolleyResponse vf) {
        MyApplication.getmRequestQueue().cancelAll(tag);
        mStringRequest = new PcStringRequest(Request.Method.GET, url, vf.loadLinstener(), vf.errorListener()) {

        };

        MyApplication.getmRequestQueue().add(mStringRequest);
    }

    public static void RequestPost(Context context, String url, String tag, final Map<String, String> params, VolleyResponse vf) {
        MyApplication.getmRequestQueue().cancelAll(tag);
        mStringRequest = new StringRequest(Request.Method.POST, url, vf.loadLinstener(), vf.errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        MyApplication.getmRequestQueue().add(mStringRequest);

    }
}
