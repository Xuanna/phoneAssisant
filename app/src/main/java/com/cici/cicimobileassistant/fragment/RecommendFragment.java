package com.cici.cicimobileassistant.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.cici.cicimobileassistant.R;
import com.cici.cicimobileassistant.db.DbUtils;
import com.cici.cicimobileassistant.db.QuerySupport;
import com.cici.cicimobileassistant.download.DownloadCallback;
import com.cici.cicimobileassistant.download.DownloadManager;
import com.cici.cicimobileassistant.download.DownloadTask;
import com.cici.cicimobileassistant.entity.DownloadInfo;
import com.cici.cicimobileassistant.utils.FileUtils;
import com.cici.cicimobileassistant.views.DownloadButton;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class RecommendFragment extends BaseFragment {


    View contentView;
    //    @BindView(R.id.btnDown)
    DownloadButton btnDown;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_recommend, container, false);


        btnDown = contentView.findViewById(R.id.btnDown);
        btnDown.setOnClickListener(onClickListener);
//
        DbUtils dbUtils = DbUtils.getDbUtils();
        dbUtils.init(getActivity());
//
//
//        DownloadInfo downloadInfo=new DownloadInfo();
//        downloadInfo.setProgress(590);
//
//        long a=dbUtils.getDao(DownloadInfo.class).update(downloadInfo, "threadId=? and url=?", new String[]{0 + "", "http://downmobiles.kugou.com/Android/KugouPlayer/10309/KugouPlayer_219_V10.3.0.apk"});
//
//        Log.e("TAG","更新条数:"+a);

        List<DownloadInfo> mList = dbUtils.getDao(DownloadInfo.class).query().queryAll();
        Log.e("data", "查询："+new Gson().toJson(mList));
//
//        HttpUtils.with(getContext()).url("https://www.baidu.com")
//                .execute(new HttpBaseCallback() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Log.e("response", response);
//                    }
//
//                    @Override
//                    public void fail(Exception e) {
//                        Log.e("fail", e.toString());
//
//                    }
//                });
        return contentView;
    }

    DownloadManager manager = DownloadManager.getDispatcher();
    private long downProgress;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            Log.e("TAG","onclick="+isPause);
            if (isPause) {
                isPause = false;
                btnDown.setStatus(DownloadButton.PAUSE);
//                manager.stopDownload("http://downmobiles.kugou.com/Android/KugouPlayer/10309/KugouPlayer_219_V10.3.0.apk");
            } else {
                isPause = true;
                btnDown.setStatus(DownloadButton.DOWNLOADING);
                btnDown.setProgress(0.5f);
//                manager.startDownload(getActivity(), "youku.apk", "http://downmobiles.kugou.com/Android/KugouPlayer/10309/KugouPlayer_219_V10.3.0.apk", new DownloadCallback() {
//                    @Override
//                    public void onFailure(IOException e) {
//                        Log.e("onFailure", e.toString());
//
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//                        Log.e("onSuccess", "onSuccess");
//                        btnDown.setStatus(DownloadButton.FINISH);
//                    }
//
//                    @Override
//                    public void onDownloading(long progress, long contentLen) {
//
//
//
//                        BigDecimal bigDecimal = BigDecimal.valueOf(progress);
//                        BigDecimal bigDecimal2 = BigDecimal.valueOf(contentLen);
//                        float f = bigDecimal.divide(bigDecimal2,2,BigDecimal.ROUND_FLOOR).floatValue();
//
//                        btnDown.setProgress(f);
////                        Log.e("onDownloading", "进度:" + f);
////
////                        Log.e("onDownloading", "progress=" + progress + "---contentLen=" + contentLen);
//
//
//                    }
//
//                });


            }
        }
    };

    boolean isPause = false;
}