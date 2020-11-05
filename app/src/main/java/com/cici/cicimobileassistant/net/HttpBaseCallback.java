package com.cici.cicimobileassistant.net;

import android.content.Context;

import java.util.Map;

public interface HttpBaseCallback {

    void onSuccess(String response);

    void fail(Exception e);

    void onPreExecute(Context context, Map<String,Object> map);


}
