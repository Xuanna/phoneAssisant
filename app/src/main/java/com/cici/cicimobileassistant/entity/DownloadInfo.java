package com.cici.cicimobileassistant.entity;

public class DownloadInfo {

    private int threadId;
    private String url;
    private long start;
    private long end;
    private long progress;
    private long contentLen;

    public DownloadInfo() {
    }

    public DownloadInfo(int threadId, String url, long start, long end, long progress, long contentLen) {
        this.threadId = threadId;
        this.url = url;
        this.start = start;
        this.end = end;
        this.progress = progress;
        this.contentLen = contentLen;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(long contentLen) {
        this.contentLen = contentLen;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
}
