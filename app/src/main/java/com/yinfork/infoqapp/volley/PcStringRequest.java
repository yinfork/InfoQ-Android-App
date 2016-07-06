package com.yinfork.infoqapp.volley;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinjianhua on 16/5/4.
 */
public class PcStringRequest extends StringRequest {
    public PcStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public PcStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("User-Agent", "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11");




        return headerMap;
    }


}
