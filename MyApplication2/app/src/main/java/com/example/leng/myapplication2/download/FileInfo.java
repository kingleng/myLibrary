package com.example.leng.myapplication2.download;

//保存下载任务信息
public class FileInfo {
    private String fileName;//文件名
    private String url;//下载地址
    private long length;//文件大小
    private long finished;//下载以已完成进度
    private boolean isStop = false;//是否暂停下载
    private boolean isDownLoading = false;//是否正在下载
    //......
    //剩下的都是对应的get and set 方法就不贴出来了，自动生成就好了


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public boolean isDownLoading() {
        return isDownLoading;
    }

    public void setDownLoading(boolean downLoading) {
        isDownLoading = downLoading;
    }
}