package com.cici.cicimobileassistant.download;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.cici.cicimobileassistant.db.DaoSupport;
import com.cici.cicimobileassistant.db.DbUtils;
import com.cici.cicimobileassistant.entity.DownloadInfo;
import com.cici.cicimobileassistant.net.OkHttpManager;
import com.cici.cicimobileassistant.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * 下载线程
 */
public class DownloadRunnable implements Runnable {

    private static final int STATUS_DOWNLOADING = 1;
    private static final int STATUS_STOP = 2;

    private String url;
    private long start;
    private long end;
    private long contentLen;
    private long progress;
    private int threadId;
    private String fileName;
    private int status = STATUS_DOWNLOADING;
    private DownloadCallback downloadCallback;
    private Context context;

    DbUtils dbUtils;
    DaoSupport<DownloadInfo> support;

    public DownloadRunnable(Context context, int threadId, String fileName, String url, long start, long end, long progress, long contentLen, DownloadCallback downloadCallback) {
        this.url = url;
        this.context = context;
        this.threadId = threadId;
        this.start = start + progress;
        this.end = end;
        this.progress = progress;
        this.contentLen = contentLen;
        this.downloadCallback = downloadCallback;
        this.fileName = fileName;
        dbUtils = DbUtils.getDbUtils();
        dbUtils.init(context);//创建/获得线程数据库
        support = (DaoSupport<DownloadInfo>) dbUtils.getDao(DownloadInfo.class);


        downloadInfo = new DownloadInfo(threadId, url, start, end, progress, contentLen);
        support.insert(downloadInfo);


    }

    DownloadInfo downloadInfo;
    long downProgress;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

        InputStream is = null;
        RandomAccessFile raf = null;
        try {
            Response response = OkHttpManager.asyncResponse(url, start, end);

            is = response.body().byteStream();

            File file = FileUtils.init(context).createSDPackageFile("downloadApk", fileName);
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(start);

            int len;
            byte[] bytes = new byte[1024 * 10];
            while ((len = is.read(bytes)) != -1) {


                if (status == STATUS_STOP) {

                    Log.e("TAGG",Thread.currentThread()+"--暂停");
                    break;
                }
                downProgress += len;
                raf.write(bytes, 0, len);

                downloadCallback.onDownloading(len, contentLen);

            }
            //下载完成，删除数据库线程信息（可不删除）
            downloadCallback.onSuccess(file);

        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onFailure(e);
        } finally {
            //下载数据保存至数据库
            downloadInfo.setProgress(progress + downProgress);

            support.update(downloadInfo, "threadId=? and url=?", new String[]{threadId + "", url});


            Log.e("TAG", "Finally");
            try {

                if (raf != null) {
                    raf.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void stop() {
        status = STATUS_STOP;
    }
}
