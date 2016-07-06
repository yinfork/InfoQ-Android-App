package com.yinfork.infoqapp.loader;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yinfork.infoqapp.MyApplication;
import com.yinfork.infoqapp.beans.NewsBean;
import com.yinfork.infoqapp.database.DatabaseManager;
import com.yinfork.infoqapp.util.InfoqNewsParse;
import com.yinfork.infoqapp.util.MD5Util;
import com.yinfork.infoqapp.util.NetWorkUtil;
import com.yinfork.infoqapp.volley.PcStringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsLoader {
    public static final String TAG = "NewsLoader";

    // Loader 当前状态
    private boolean mIsRefreshing = false;
    private boolean mIsLoadmoreing = false;


    private String mType;
    private LoaderListener mListener;
    private DatabaseManager mDBManager;

    private final static int EXPERT_TIME = 1 * 24 * 60 * 60;


    public NewsLoader(String type) {
        if (null != type) {
            mType = type;
        }

        mDBManager = new DatabaseManager();
    }


    public void loadLocalData(final LoaderListener listener) {
        Log.d(TAG, "loadLocalData type: " + mType);

        List<NewsBean> list = mDBManager.queryNews(mType, 0, 15);
        if (null != listener) {
            if (null != list && list.size() > 0) {
                listener.onSuccess(list);
            } else {
                listener.onError();
            }
        }
    }

    public void refresh(String url, final LoaderListener listener) {
        if(!mIsRefreshing) {
            mIsRefreshing = true;
            if (NetWorkUtil.isConnected()) {
                StringRequest request = new PcStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mIsRefreshing = false;
                        if (null != listener) {
                            List<NewsBean> list = parseData(response);
                            saveData(list);
                            listener.onSuccess(list);
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
                List<NewsBean> list = mDBManager.queryNews(mType, 0, 15);
                mIsRefreshing = false;
                if (null != listener) {
                    if (null != list && list.size() > 0) {
                        listener.onSuccess(list);
                    } else {
                        listener.onError();
                    }
                }
            }
        }
    }


    public void loadMore(String url, int curCount, int queryCount, final LoaderListener listener) {
        if(!mIsLoadmoreing) {
            mIsLoadmoreing = true;
            if (NetWorkUtil.isConnected()) {
                StringRequest request = new PcStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mIsLoadmoreing = false;
                        if (null != listener) {
                            List<NewsBean> list = parseData(response);
                            saveData(list);
                            listener.onSuccess(list);
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
                List<NewsBean> list = mDBManager.queryNews(mType, curCount, queryCount);
                mIsLoadmoreing = false;
                if (null != listener) {
                    if (null != list && list.size() > 0) {
                        listener.onSuccess(list);
                    } else {
                        listener.onError();
                    }
                }
            }
        }
    }


    private List<NewsBean> parseData(String data) {
        return InfoqNewsParse.getmInstance().getNewsList(data);
    }

    private void saveData(List<NewsBean> list) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date());

        Log.d("yinjianhua", "now time: " + nowTime);

        for (NewsBean bean : list) {
            bean.setType(mType);
            String id = MD5Util.MD5Encode(bean.getLink() + bean.getType());
            bean.setId(id);
            bean.setModify(nowTime);
            mDBManager.insertNews(bean);
        }
    }


    public interface LoaderListener {
        public void onSuccess(List<NewsBean> data);

        public void onError();
    }


}
