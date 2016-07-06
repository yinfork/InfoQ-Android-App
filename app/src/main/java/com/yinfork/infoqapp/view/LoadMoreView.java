package com.yinfork.infoqapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinfork.infoqapp.R;

/**
 * Created by yinjianhua on 16/6/20.
 */
public class LoadMoreView extends LinearLayout {
    private static final String TAG = "LoadMoreView";

    public static final int INVISIBILY_MODE = 99;
    public static final int LOADING_MODE = 100;
    public static final int ERROR_MODE = 101;


    private View mProcessBar;
    private TextView mLoadingText;
    private View.OnClickListener mListener;

    public LoadMoreView(Context context) {
        super(context);
        initLayout(context);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context){
        Log.d(TAG,"initLayout");
        LayoutInflater.from(context).inflate(R.layout.view_loadmore, this);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        mProcessBar = findViewById(R.id.item_news_progressbar);
        mLoadingText = (TextView) findViewById(R.id.item_news_message);
        setLoadingMode(INVISIBILY_MODE);
    }


    public void setLoadingMode(int mode){
        switch (mode){
            case LOADING_MODE:
                mProcessBar.setVisibility(VISIBLE);
                mLoadingText.setVisibility(VISIBLE);
                mLoadingText.setText(getResources().getString(R.string.foot_view_loading));
                setOnClickListener(null);
                break;
            case ERROR_MODE:
                mProcessBar.setVisibility(GONE);
                mLoadingText.setVisibility(VISIBLE);
                mLoadingText.setText(getResources().getString(R.string.foot_view_error));
                setOnClickListener(mListener);
                break;

            case INVISIBILY_MODE:
                mProcessBar.setVisibility(GONE);
                mLoadingText.setVisibility(GONE);
                setOnClickListener(null);
                break;
        }
    }

    public void setOnErrorClickListener(View.OnClickListener listener){
        mListener = listener;
    }
}
