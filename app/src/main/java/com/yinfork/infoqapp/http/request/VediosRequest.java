package com.yinfork.infoqapp.http.request;

import com.yinfork.infoqapp.beans.NewsBean;
import com.yinfork.infoqapp.util.InfoqNewsParse;

import java.util.List;

/**
 * Created by yinjianhua on 16/5/15.
 */
public class VediosRequest extends BaseRequest<List<NewsBean>> {

    @Override
    public List<NewsBean> parseData(String data) {
        return InfoqNewsParse.getmInstance().getVideosList(data);
    }
}
