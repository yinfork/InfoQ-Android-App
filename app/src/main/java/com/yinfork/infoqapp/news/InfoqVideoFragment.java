package com.yinfork.infoqapp.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.yinfork.infoqapp.beans.NewsBean;
import com.yinfork.infoqapp.detail.VideoInfoActivity;
import com.yinfork.infoqapp.http.request.BaseRequest;
import com.yinfork.infoqapp.http.request.VediosRequest;

/**
 * Created by yinjianhua on 16/5/13.
 */
public class InfoqVideoFragment extends BaseInfoqFragment {

    private String mUrl = "http://www.infoq.com/cn/presentations/";

    public InfoqVideoFragment(String url){
        if(null != url){
            mUrl = url;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (null != mAdapter.getDatas() && mAdapter.getDatas().size() > position) {
                    NewsBean bean = mAdapter.getDatas().get(position);

                    Intent intent = new Intent(getActivity(), VideoInfoActivity.class);
                    intent.putExtra("url", bean.getLink());
                    intent.putExtra("id", bean.getId());
                    intent.putExtra("title", bean.getTitle());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected String getType() {
        return "video";
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    @Override
    protected BaseRequest getRequest() {
        return new VediosRequest();
    }


}
