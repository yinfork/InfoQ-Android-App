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

public class NewsLoader extends BaseLoader<List<NewsBean>> {
    public static final String TAG = "NewsLoader";

    private String mType;
    private DatabaseManager mDBManager;

    public NewsLoader(String url,String type) {
        super(url);
        if (null != type) {
            mType = type;
        }

        mDBManager = new DatabaseManager();
    }

    @Override
    protected List<NewsBean> queryLocalData() {
        return mDBManager.queryNews(mType, 0, 15);
    }

    @Override
    protected List<NewsBean> parseData(String data) {
        return InfoqNewsParse.getmInstance().getNewsList(data);
    }

    @Override
    protected void saveData(List<NewsBean> list) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date());

        Log.d(TAG, "now time: " + nowTime);

        for (NewsBean bean : list) {
            bean.setType(mType);
            String id = MD5Util.MD5Encode(bean.getLink() + bean.getType());
            bean.setId(id);
            bean.setModify(nowTime);
            mDBManager.insertNews(bean);
        }
    }
}
