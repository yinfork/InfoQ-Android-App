package com.yinfork.infoqapp.volley;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2015/10/22.
 */
public abstract class VolleyResponse {
    private Response.Listener<String> mListener;
    private Response.ErrorListener mErrorListener;

    public abstract void onSuccess(String result);

    public abstract void onError(VolleyError error);

    public Response.Listener<String> loadLinstener() {
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onSuccess(response);
            }
        };
        return mListener;
    }

    public Response.ErrorListener errorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };
        return mErrorListener;
    }
}
