package com.cici.cicimobileassistant.net;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class OkHttpManager implements IHttp {


    private static OkHttpClient okHttpClient;

    public static OkHttpManager okHttpManager = new OkHttpManager();

    @RequiresApi(api = Build.VERSION_CODES.O)
    private OkHttpManager() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(5000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(5000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(5000, TimeUnit.MILLISECONDS);

        okHttpClient = builder.build();

    }


    public static OkHttpManager getManager() {
        return okHttpManager;
    }

    public static Call asyncCall(String url) {

        Request request = new Request.Builder()
                .url(url).build();

        return okHttpClient.newCall(request);
    }

    public static Response asyncResponse(String url, long start, long end) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();

        return okHttpClient.newCall(request).execute();

    }

    @Override
    public void get(String url, Map<String, Object> params, HttpBaseCallback callback) {

        Request request = new Request.Builder()
                .url(joinParams(url, params))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                callback.fail(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.onSuccess(response.body().string());
            }
        });

    }

    @Override
    public void post(String url, Map<String, Object> params, HttpBaseCallback callback) {

        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            builder.add(key, (String) value);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                callback.fail(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.onSuccess(response.body().string());
            }
        });
    }


    /**
     * url拼接
     *
     * @param params
     */
    public String joinParams(String url, Map<String, Object> params) {


        if (params == null || params.size() <= 0) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {//没有？
            sb.append("?");
        } else {//有？
            if (!url.endsWith("?")) {//是否以？结尾，不是？就在后面加&
                sb.append("&");
            }
        }


        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append("=").append(value).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();

    }
}
