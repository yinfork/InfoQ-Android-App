package com.yinfork.infoqapp.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.yinfork.infoqapp.NewsAdapter;
import com.yinfork.infoqapp.OnScrollBottomListener;
import com.yinfork.infoqapp.R;
import com.yinfork.infoqapp.beans.NewsBean;
import com.yinfork.infoqapp.detail.NewsInfoActivity;
import com.yinfork.infoqapp.http.request.BaseRequest;
import com.yinfork.infoqapp.http.request.NewsRequest;
import com.yinfork.infoqapp.loader.NewsLoader;
import com.yinfork.infoqapp.view.LoadMoreView;

import java.util.List;

/**
 * Created by yinjianhua on 16/5/13.
 */
public class InfoqNewsFragment extends Fragment{
    public static final String TAG = "InfoqNewsFragment";
    private LoadMoreView mFootView;
    protected ListView mListView;
    protected NewsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsLoader mLoader;
    private int mPage = 0;
    private Handler mHandler = new Handler();
    private Runnable refreshRunnable;

    private final static String DEFAULT_URL = "http://www.infoq.com/cn/news/";
    private final static String DEFAULT_TYPE = "NONE";

    private String mUrl = DEFAULT_URL;
    private String mType = DEFAULT_TYPE;

    public static InfoqNewsFragment newInstance(String url, String type) {
        InfoqNewsFragment newFragment = new InfoqNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString("url");
            String type = args.getString("url");

            if(null != url){
                mUrl = url;
            }
            if(null != type){
                mType = type;
            }
        }
        mLoader = new NewsLoader(mType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_news, container, false);

        mListView = (ListView) root.findViewById(R.id.news_list);
        mAdapter = new NewsAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        mFootView = new LoadMoreView(getActivity());
        mFootView.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFootView.setLoadingMode(LoadMoreView.LOADING_MODE);
                loadMore();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.news_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mListView.setOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(AbsListView absListView, int scrollState) {
                Log.d(TAG, "scrollBottomItem");

                if(null != mFootView){
                    mFootView.setLoadingMode(LoadMoreView.LOADING_MODE);
                }
                loadMore();
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

        return root;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, mType + " onViewCreated");
        loadLocalData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(null != mListView && null != mFootView) {
            mListView.removeFooterView(mFootView);
        }

        if(null != mHandler){
            mHandler.removeCallbacks(null);
        }
    }

    private void loadLocalData(){
        Log.d(TAG, mType + " loadLocalData");

        mLoader.loadLocalData(new NewsLoader.LoaderListener() {
            @Override
            public void onSuccess(List<NewsBean> data) {
                Log.d(TAG, mType + " loadLocalData Success");

                if (null != data && data.size() > 0) {
                    mPage += data.size();
                    mAdapter.onRefresh(data);

                    int footViewCount = mListView.getFooterViewsCount();
                    if (footViewCount >= 0 && mListView != null) {
                        mFootView.setLoadingMode(LoadMoreView.INVISIBILY_MODE);
                        mListView.addFooterView(mFootView);
                    }

                    mHandler.removeCallbacks(refreshRunnable);
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, mType + " setRefreshing false");
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, mType + " loadLocalData onError");

                refreshRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, mType + " setRefreshing true");
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                };
                mHandler.post(refreshRunnable);

                // 如果本地没有数据,则进行网络请求
                refresh();
            }
        });
    }

    private void refresh(){
        String url = getUrl();

        mLoader.refresh(url, new NewsLoader.LoaderListener() {
            @Override
            public void onSuccess(List<NewsBean> data) {
                Log.d(TAG, mType + " refresh onSuccess");

                mHandler.removeCallbacks(refreshRunnable);
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, mType + " setRefreshing false");
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }

                if (null != data && data.size() > 0) {
                    mAdapter.onRefresh(data);

                    int footViewCount = mListView.getFooterViewsCount();
                    if (footViewCount <= 0 && mListView != null) {
                        mFootView.setLoadingMode(LoadMoreView.INVISIBILY_MODE);
                        mListView.addFooterView(mFootView);
                    }
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, mType + " refresh onError");
                mHandler.removeCallbacks(refreshRunnable);
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, mType + " setRefreshing false");
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }


    private void loadMore(){
        String url = getUrl();
        mPage += 15;
        url = url + mPage;

        mLoader.loadMore(url, mPage - 15, 15, new NewsLoader.LoaderListener() {
            @Override
            public void onSuccess(List<NewsBean> data) {
                Log.d(TAG, mType + " loadMore onSuccess");
                if (null != data && data.size() > 0) {
                    mAdapter.onLoadMore(data);
                }
            }

            @Override
            public void onError() {
                Log.d(TAG,mType + " loadMore onError");
                mFootView.setLoadingMode(LoadMoreView.ERROR_MODE);
            }
        });
    }

    protected String getUrl() {
        return mUrl;
    }

    protected BaseRequest getRequest() {
        return new NewsRequest();
    }

}
