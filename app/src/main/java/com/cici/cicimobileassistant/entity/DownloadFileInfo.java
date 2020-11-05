package com.cici.cicimobileassistant.entity;

public class DownloadFileInfo {

    private String url;
    private long progress;
    private long contentLen;
    private int status;


    public DownloadFileInfo(String url, int status, long progress, long contentLen) {
        this.url = url;
        this.status = status;
        this.progress = progress;
        this.contentLen = contentLen;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(long contentLen) {
        this.contentLen = contentLen;
    }
}
