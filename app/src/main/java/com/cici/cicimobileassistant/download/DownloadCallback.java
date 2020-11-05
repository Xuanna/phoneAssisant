package com.cici.cicimobileassistant.download;

import java.io.File;
import java.io.IOException;

/**
 * 下载回调
 */
public interface DownloadCallback {

    void onFailure(IOException e);
    void onSuccess(File file);
    void onDownloading(long progress,long conLen);

}
