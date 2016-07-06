package com.yinfork.infoqapp.detail;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yinfork.infoqapp.MyApplication;
import com.yinfork.infoqapp.R;
import com.yinfork.infoqapp.loader.DetailLoader;

public class NewsInfoActivity extends AppCompatActivity {
    private WebView mWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;
    private WebSettings mWebSettings;
    private TextView mTitleView;
    private View mBackView;
    private String mUrl;
    private String mId;
    private String mTitle;
    private DetailLoader mLoader;
    private static final String TAG = "NewsInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsinfo);
        initView();
        mLoader = new DetailLoader();
        getNewsDetial();
    }

    private void initView() {
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.newsinfo_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mTitleView = (TextView) findViewById(R.id.title);
        mBackView= findViewById(R.id.back);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //webview
        mWebView = (WebView) findViewById(R.id.newsinfo_webview);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newsinfo_refreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2, R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsDetial();
            }
        });
        // Runnable为了能够第一次进入页面的时候显示加载进度条
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        //url
        mUrl = getIntent().getStringExtra("url");
        // id
        mId = getIntent().getStringExtra("id");
        // title
        mTitle = getIntent().getStringExtra("title");
        mTitleView.setText(mTitle);
    }

    private void getNewsDetial() {
        if (mUrl == null) {
            return;
        }
        mLoader.load(mUrl,mId, new DetailLoader.LoaderListener() {
            @Override
            public void onSuccess(String data) {
                mWebView.loadData(data, "text/html; charset=UTF-8", null);//这种写法可以正确解码

            }

            @Override
            public void onError() {

            }
        });
    }

    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack(); // goBack()表示返回webView的上一页面

            return true;
        }
        return super.onKeyDown(keyCoder, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getmRequestQueue().cancelAll(TAG);
        if(null != mWebView) {
            mWebView.destroy();
        }
    }
}
