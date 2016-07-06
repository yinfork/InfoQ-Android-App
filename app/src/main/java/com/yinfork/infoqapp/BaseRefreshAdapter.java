package com.yinfork.infoqapp;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinjianhua on 16/5/5.
 */
public abstract class BaseRefreshAdapter<T> extends BaseAdapter {

    protected List<T> mDatas = new ArrayList<>();

    public void onRefresh(List<T> sourceDatas) {
        if (sourceDatas != null && sourceDatas.size() != 0) {

            List<T> newDatas = new ArrayList<>();
            if (mDatas != null && mDatas.size() > 0) {
                T firstBean = mDatas.get(0);
                for (T bean : sourceDatas) {
                    // 去掉重复数据
                    if (firstBean.equals(bean)) break;

                    newDatas.add(bean);
                }
            } else {
                newDatas = sourceDatas;
            }

            if (null != newDatas && newDatas.size() > 0) {
                ArrayList<T> oldData = new ArrayList(mDatas);
                mDatas = newDatas;
                mDatas.addAll(oldData);
                notifyDataSetChanged();
            }
        }
    }

    public void onLoadMore(List<T> newDatas) {
        if (newDatas != null && newDatas.size() != 0) {
            mDatas.addAll(newDatas);
            notifyDataSetChanged();
        }
    }

}
