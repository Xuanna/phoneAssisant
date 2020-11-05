package com.cici.cicimobileassistant.download;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.cici.cicimobileassistant.net.CommonParamsCallBack;
import com.cici.cicimobileassistant.net.HttpCallback;
import com.cici.cicimobileassistant.net.HttpUtils;
import com.cici.cicimobileassistant.net.OkHttpManager;
import com.cici.cicimobileassistant.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadManager {

    private static final int MAX_TASK = 3;

    private static final DownloadManager sDispatcher = new DownloadManager();

    private final Deque<DownloadTask> readyTasks = new ArrayDeque<>();

    private final Deque<DownloadTask> runningTasks = new ArrayDeque<>();

    private final Deque<DownloadTask> stopTasks = new ArrayDeque<>();
    private Context context;

    private DownloadManager() {

    }

    public static DownloadManager getDispatcher() {
        return sDispatcher;
    }
    private String fileName;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startDownload(Context context, String fileName, String url, DownloadCallback callback) {
        this.context = context;
        this.fileName=fileName;
        if (runningTasks.size() > MAX_TASK) {
//            callback.overMax("最多只能同时下载"+MAX_TASK+"个任务哦");

            return;
        }

//        HttpUtils.with(context).execute(new CommonParamsCallBack() {
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//
//            @Override
//            public void fail(Exception e) {
//
//            }
//        });
        Call call = OkHttpManager.asyncCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                long contentLength = response.body().contentLength();
                if (contentLength <= 0) {
                    return;
                }
                File file = FileUtils.init(context).createSDPackageFile("downloadApk", fileName);
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(contentLength);

                DownloadTask downloadTask = null;
                try {
                    downloadTask = new DownloadTask(context,fileName,url, contentLength, callback);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                downloadTask.start();

                if (runningTasks.size() <= MAX_TASK) {
                    runningTasks.add(downloadTask);
                }

            }
        });

    }

    /**
     * 如果暂停下载的是正在下载的任务就去暂停
     *
     * @param url
     */
    public void stopDownload(String url) {
        for (DownloadTask downloadTask : runningTasks) {
            if (downloadTask.getUrl().equals(url)) {
                downloadTask.stop();

                Log.e("TAG","  找到任务，暂停所有线程下载");
            }

        }
    }

}
