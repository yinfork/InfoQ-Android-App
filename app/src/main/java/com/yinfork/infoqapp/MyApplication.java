package com.yinfork.infoqapp;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yinfork.infoqapp.database.DbOpenHelper;
import com.yinfork.infoqapp.util.NetWorkUtil;

public class MyApplication extends Application {
    private static Context mContext;
    private static RequestQueue mRequestQueue;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        DbOpenHelper.initContext(getApplicationContext());
        NetWorkUtil.initContext(getApplicationContext());
    }

    public static RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }
}
