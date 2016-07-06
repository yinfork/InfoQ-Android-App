package com.yinfork.infoqapp.http.request;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.VolleyError;
import com.yinfork.infoqapp.volley.VolleyClient;
import com.yinfork.infoqapp.volley.VolleyResponse;

/**
 * Created by yinjianhua on 16/5/15.
 */
public abstract class BaseRequest<T> {

    private ResponseListener<T> mListener = null;

    private Handler mUiHadler;

    public BaseRequest(){
        mUiHadler = new Handler(Looper.getMainLooper());
    }

    public void run(String url,ResponseListener listener){
        mListener = listener;
        VolleyClient.PcRequestGet(url, "q", new VolleyResponse() {
            @Override
            public void onSuccess(String response) {
                T result = null;
                if (null != response) {
                    result = parseData(response);
                }

                if (null != mListener) {
                    final T finalResult = result;
                    mUiHadler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onSuccess(finalResult);
                        }
                    });
                }
            }

            @Override
            public void onError(final VolleyError error) {
                if (null != mListener) {
                    mUiHadler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onError(error);
                        }
                    });
                }
            }

        });
    }

    public abstract T parseData(String data);


    // FIXME: Listener容易造成内存泄露,可以考虑使用统一管理所有请求,然后使用注册和反注册的形式获取响应
    public interface ResponseListener<T> {
        void onError(VolleyError error);

        void onSuccess(T result);
    }

}
