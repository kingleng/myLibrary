package com.example.leng.myapplication2.download;

//下载进度接口
public interface OnProgressListener {
 
    void updateProgress(long max, long progress);
}