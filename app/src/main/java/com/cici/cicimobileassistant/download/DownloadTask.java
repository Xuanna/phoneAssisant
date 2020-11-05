package com.cici.cicimobileassistant.download;


import android.content.ContentValues;
import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import com.cici.cicimobileassistant.db.DaoSupport;
import com.cici.cicimobileassistant.db.DbUtils;
import com.cici.cicimobileassistant.db.QuerySupport;
import com.cici.cicimobileassistant.entity.DownloadFileInfo;
import com.cici.cicimobileassistant.entity.DownloadInfo;
import com.cici.cicimobileassistant.views.DownloadButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 下载任务，所有的线程下载都在线程池中，放置不必要的资源开销
 */
public class DownloadTask {

    public static ExecutorService executorService;
    private List<DownloadRunnable> mRunnables;
    private long contentLen;
    private String url;
    private DownloadCallback downloadCallback;
    private volatile int mSucceedNumber;
    private Context context;
    private DownloadFileInfo fileInfo;
    private String fileName;
    DbUtils dbUtils;
    DaoSupport<DownloadFileInfo> support;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    //    public static double[] mProcess = new double[THREAD_SIZE];
    public static long mProcess;

    public String getUrl() {
        return url;
    }

    public DownloadTask(Context context, String fileName, String url, long length, DownloadCallback downloadCallback) throws IllegalAccessException {
        this.url = url;
        this.contentLen = length;
        this.fileName = fileName;
        mRunnables = new ArrayList<>();
        executorService();
        this.context = context;
        this.downloadCallback = downloadCallback;

        //数据库文件信息：下载进度，文件长度，状态：下载完成，暂停，下载中

        dbUtils = DbUtils.getDbUtils();
        dbUtils.init(context);

        support = (DaoSupport<DownloadFileInfo>) dbUtils.getDao(DownloadFileInfo.class);

        fileInfo = new DownloadFileInfo(url, 2, 0, contentLen);
        support.insert(fileInfo);
    }


    public ExecutorService executorService() {

        if (executorService == null) {
            executorService = new ThreadPoolExecutor(
                    0,
                    THREAD_SIZE,
                    30,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());

        }


        return executorService;

    }

    private long downProgress = 0;//文件下载长度


    public void start() {

        long eachLen = contentLen / THREAD_SIZE;
        for (int i = 0; i < THREAD_SIZE; i++) {

            long start = eachLen * i;
            long end = eachLen * (i + 1) - 1;

            if (i == THREAD_SIZE - 1) {
                end = contentLen - 1;
            }
            //查询线程数据库当前线程的下载了多少
            QuerySupport<DownloadInfo> downloadInfoQuerySupport = dbUtils.getDao(DownloadInfo.class).query();
            DownloadInfo downloadInfo = downloadInfoQuerySupport.query(null, "threadId=? and url=? ", new String[]{i + "", url}, null, null, null);


            long threadProgress = downloadInfo.getProgress();

            Log.e("tAgG", "当前线程的进度：" + Thread.currentThread() + "--start=" + start + "--progress=" + threadProgress + "--end=" + end);
            DownloadRunnable runnable = new DownloadRunnable(context, i, fileName, url, start, end, threadProgress, contentLen, new DownloadCallback() {
                @Override
                public void onFailure(IOException e) {
                    downloadCallback.onFailure(e);

                }

                @Override
                public void onDownloading(long down, long contentLen) {//当前线程下载了多少，保存文件信息,文件信息，下载中，暂停


                    synchronized (this) {
                        downProgress += down;
                        downloadCallback.onDownloading(downProgress, contentLen);
                    }

//

//
//                    fileInfo.setProgress(threadProgress+down);
//                    support.update(fileInfo, "url = ?", new String[]{url});

                    //更新文件长度

//                    Log.e("tAgG", "总共下载了" + downProgress + "长度");

                }

                @Override
                public void onSuccess(File file) {
                    synchronized (this) {
                        mSucceedNumber++;
                        if (mSucceedNumber == THREAD_SIZE) {
                            try {
                                downloadCallback.onSuccess(file);//文件下载完成，清除数据库信息
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            mRunnables.add(runnable);
            executorService.execute(runnable);
        }
    }


    public void stop() {
        fileInfo.setStatus(3);
        fileInfo.setProgress(downProgress);
        support.update(fileInfo, "url=?", new String[]{url});
        for (DownloadRunnable runnable : mRunnables) {
            runnable.stop();
        }
    }
}
