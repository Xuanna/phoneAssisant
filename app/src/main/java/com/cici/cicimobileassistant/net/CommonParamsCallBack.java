package com.cici.cicimobileassistant.net;

import android.content.Context;

import java.util.Map;

/**
 * 公共参数的callback
 */
public abstract class CommonParamsCallBack extends HttpCallback {


    @Override
    public void onPreExecute(Context context, Map<String, Object> map) {

        map.put("", "");

    }

}
