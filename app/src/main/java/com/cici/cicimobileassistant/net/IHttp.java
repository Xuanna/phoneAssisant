package com.cici.cicimobileassistant.net;

import java.util.Map;

public interface IHttp {

    void get(String url, Map<String, Object> params, HttpBaseCallback callback);

    void post(String url, Map<String, Object> params, HttpBaseCallback callback);
}
