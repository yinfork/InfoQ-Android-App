package com.yinfork.infoqapp.loader;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yinfork.infoqapp.MyApplication;
import com.yinfork.infoqapp.util.NetWorkUtil;
import com.yinfork.infoqapp.volley.PcStringRequest;

import java.util.List;

public abstract class BaseLoader<T> {

    // Loader 当前状态
    private boolean mIsRefreshing = false;
    private boolean mIsLoadmoreing = false;

    protected String mUrl;

    protected abstract T queryLocalData();
    protected abstract T parseData(String data);
    protected abstract void saveData(T data);

    public BaseLoader(String url){
        mUrl = url;
    }

    public void loadLocalData(final LoaderListener<T> listener) {
        T result = queryLocalData();

        if (null != listener) {
            if (null != result) {
                // 如果是列表,还要判断列表里是否有数据
                if(result instanceof List && ((List)result).size() == 0){
                    listener.onError();
                }else {
                    listener.onSuccess(result);
                }
            } else {
                listener.onError();
            }
        }
    }

    public void refresh(final LoaderListener<T> listener) {
        if(!mIsRefreshing) {
            mIsRefreshing = true;
            if (NetWorkUtil.isConnected()) {

                // TODO: 要加入接口来选择Request的方式
                StringRequest request = new PcStringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mIsRefreshing = false;
                        if (null != listener) {
                            T data = parseData(response);
                            saveData(data);
                            listener.onSuccess(data);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mIsRefreshing = false;
                        if (null != listener) {
                            listener.onError();
                        }
                    }
                });

                MyApplication.getmRequestQueue().add(request);
            } else {
                T result = queryLocalData();
                mIsRefreshing = false;
                if (null != listener) {
                    if (null != result) {
                        listener.onSuccess(result);
                    } else {
                        listener.onError();
                    }
                }
            }
        }
    }


    public void loadMore(int curCount, int queryCount, final LoaderListener<T> listener) {
        if(!mIsLoadmoreing) {
            mIsLoadmoreing = true;
            if (NetWorkUtil.isConnected()) {
                StringRequest request = new PcStringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mIsLoadmoreing = false;
                        if (null != listener) {
                            T data= parseData(response);
                            saveData(data);
                            listener.onSuccess(data);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mIsLoadmoreing = false;
                        if (null != listener) {
                            listener.onError();
                        }
                    }
                });

                MyApplication.getmRequestQueue().add(request);
            } else {
                T result = queryLocalData();
                mIsLoadmoreing = false;
                if (null != listener) {
                    if (null != result) {
                        listener.onSuccess(result);
                    } else {
                        listener.onError();
                    }
                }
            }
        }
    }

}
