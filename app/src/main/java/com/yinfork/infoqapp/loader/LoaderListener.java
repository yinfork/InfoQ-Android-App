package com.yinfork.infoqapp.loader;

public interface LoaderListener<T> {
    public void onSuccess(T data);

    public void onError();
}
