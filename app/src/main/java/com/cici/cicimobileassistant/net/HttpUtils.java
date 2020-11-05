package com.cici.cicimobileassistant.net;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络框架，链式调用
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class HttpUtils {

    private static IHttp iHttp =  OkHttpManager.getManager();
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0022;
    private String url;
    private Map<String, Object> params;
    private int method = GET_TYPE;

    private Context context;

    public HttpUtils(Context context) {
        this.context = context;
        params = new HashMap<>();

    }

    public static HttpUtils with(Context context) {

        return new HttpUtils(context);
    }


    public HttpUtils url(String url) {
        this.url = url;
        return this;
    }

    public HttpUtils setParams(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public HttpUtils setParams(Map<String, Object> params) {
        params.putAll(params);
        return this;
    }

    public void execute(HttpCallback callback) {

        callback.onPreExecute(context, params);

        if (method == GET_TYPE) {
            get(url, params, callback);
        } else if (method == POST_TYPE) {
            post(url, params, callback);
        }

    }

    public static void init(IHttp httpManager) {
        iHttp = httpManager;

    }

    public static void changeEngine(IHttp httpManager) {
        iHttp = httpManager;

    }

    private void post(String url, Map<String, Object> params, HttpBaseCallback httpCallback) {
        iHttp.post(url, params, httpCallback);

    }

    private void get(String url, Map<String, Object> params, HttpBaseCallback httpCallback) {
        iHttp.get(url, params, httpCallback);
    }


    public static Class<?> analysicClazzInfo(Object object) {

        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];

    }
}
