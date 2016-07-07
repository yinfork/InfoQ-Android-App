package com.android.volley.helper;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.cache.LruCache;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2015/10/22.
 */
public class VolleyImageHelper implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mLruCache;
    private final int mMaxSize = 10 * 1024 * 1024;

    public VolleyImageHelper() {
        mLruCache = new LruCache<String, Bitmap>(mMaxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.i("clj", "getBitmap=" + url);
        return mLruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
        Log.i("clj", "putBitmap=" + url);
    }

}
