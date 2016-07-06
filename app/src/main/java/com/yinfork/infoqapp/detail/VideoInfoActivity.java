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

public class VideoInfoActivity extends AppCompatActivity {
    private WebView mWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;
    private WebSettings mWebSettings;
    private TextView mTitleView;
    private View mBackView;
    private String mUrl;
    private String mTitle;
    private final String mTag = "VideoInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsinfo);
        initView();
        getNewsDetial(200, 200);
    }

    private void initView() {
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.newsinfo_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
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

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
        mWebSettings.setJavaScriptEnabled(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newsinfo_refreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2, R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsDetial(200, 200);
            }
        });
        // Runnable为了能够第一次进入页面的时候显示加载进度条
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        //title
        mTitleView = (TextView) findViewById(R.id.title);
        //设置标题
        mTitle = getIntent().getStringExtra("title");
        if (mTitle != null) {
            mTitleView.setText(mTitle);
        }
        //url
        mUrl = getIntent().getStringExtra("url");
    }

    private void getNewsDetial(final int width, final int height) {
        if (mUrl == null) {
            return;
        }
        mWebView.loadUrl(mUrl);
    }

    /**
     * 格式化html
     *
     * @param frame 框架
     * @param title 标题
     * @param infor 作者时间
     * @param texts 内容
     * @return
     */
    private String formatHtml(String frame, String title, String infor, String texts) {
        return String.format(frame, title, infor, texts);

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
        MyApplication.getmRequestQueue().cancelAll(mTag);
        if(null != mWebView) {
            mWebView.destroy();
        }
    }
}
