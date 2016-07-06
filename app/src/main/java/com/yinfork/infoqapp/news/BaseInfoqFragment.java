package com.yinfork.infoqapp.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.yinfork.infoqapp.NewsAdapter;
import com.yinfork.infoqapp.OnScrollBottomListener;
import com.yinfork.infoqapp.R;
import com.yinfork.infoqapp.beans.NewsBean;
import com.yinfork.infoqapp.database.DatabaseManager;
import com.yinfork.infoqapp.detail.NewsInfoActivity;
import com.yinfork.infoqapp.http.request.BaseRequest;
import com.yinfork.infoqapp.util.MD5Util;

import java.util.List;

/**
 * Created by yinjianhua on 16/5/13.
 */
public abstract class BaseInfoqFragment<T> extends Fragment {
    public static final String TAG = "InfoqNewsFragment";

    private View mFootView;
    protected ListView mListView;
    protected NewsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DatabaseManager mDbManager;

    private int mPage = 0;
    private boolean mIsFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_news, container, false);

        mListView = (ListView) root.findViewById(R.id.news_list);
        mAdapter = new NewsAdapter(getActivity());

        mFootView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_foot, null);
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.news_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews(true);
            }
        });

        mListView.setOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(AbsListView absListView, int scrollState) {
                Log.d(TAG, "scrollBottomItem");
                getNews(false);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (null != mAdapter.getDatas() && mAdapter.getDatas().size() > position) {
                    NewsBean bean = mAdapter.getDatas().get(position);

                    Intent intent = new Intent(getActivity(), NewsInfoActivity.class);
                    intent.putExtra("url", bean.getLink());
                    intent.putExtra("id", bean.getId());
                    intent.putExtra("title", bean.getTitle());
                    startActivity(intent);
                }
            }
        });

        mDbManager = new DatabaseManager();
        getNews(true);

        return root;
    }

    public void getNews(final boolean isRefresh) {
        String url = getUrl();

        if (isRefresh) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    // Runnable为了能够第一次进入页面的时候显示加载进度条
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mPage += 15;
            url = url + mPage;
        }


        BaseRequest request = getRequest();
        if(null != request) {
            request.run(url, new BaseRequest.ResponseListener<List<NewsBean>>() {
                @Override
                public void onError(VolleyError error) {
                    if (isRefresh) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    if(null != error){
                        Log.d("TAG","Volley error: "+error.getMessage());
                    }
                }

                @Override
                public void onSuccess(List<NewsBean> result) {
                    if (null != result && result.size() > 0) {
                        if (isRefresh) {
                            mAdapter.onRefresh(result);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            mAdapter.onLoadMore(result);
                        }

                        if (mIsFirstLoad && mListView != null) {
                            mIsFirstLoad = false;
                            mListView.addFooterView(mFootView);
                        }

                        for(NewsBean bean:result){

                            bean.setType(getType());
                            String id = MD5Util.MD5Encode(bean.getLink()+bean.getType());
                            bean.setId(id);
                            mDbManager.insertNews(bean);
                        }
                    }
                }
            });
        }
    }

    abstract protected String getType();
    abstract protected String getUrl();
    abstract protected BaseRequest getRequest();

}
