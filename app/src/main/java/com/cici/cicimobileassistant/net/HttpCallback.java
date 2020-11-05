package com.cici.cicimobileassistant.net;

import com.google.gson.Gson;

public abstract class HttpCallback<T> implements HttpBaseCallback {


    @Override
    public void onSuccess(String response) {

        Gson gson = new Gson();
        T result = (T) gson.fromJson(response, HttpUtils.analysicClazzInfo(this));

        onSuccess(result);
    }

    public abstract void onSuccess(T result);


}
