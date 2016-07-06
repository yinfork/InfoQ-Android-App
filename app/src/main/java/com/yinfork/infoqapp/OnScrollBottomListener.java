package com.yinfork.infoqapp;

import android.widget.AbsListView;

/**
 * Created by yinjianhua on 16/5/5.
 */
public abstract class OnScrollBottomListener implements AbsListView.OnScrollListener {

    public void onScrollStateChanged(AbsListView view, int scrollState){
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
                onScrollBottom(view, scrollState);
            }
        }
    }

    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    // 滑动进入到底部Item
    abstract public void onScrollBottom(AbsListView absListView, int scrollState);
}
