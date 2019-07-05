package com.example.leng.myapplication2.view.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.download.DownLoadTask;
import com.example.leng.myapplication2.view.download.FileInfo;
import com.example.leng.myapplication2.view.download.OnProgressListener;
import com.example.leng.myapplication2.view.rxhttp.DownloadInfo;
import com.example.leng.myapplication2.view.rxhttp.OnError;
import com.example.leng.myapplication2.view.tools.MD5;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import rxhttp.wrapper.entity.Progress;

public class RxHttpActivity extends AppCompatActivity {

    String mPath = getSDPath() + "/aegis/videoDownload";

    DownloadInfo dataInfo;

    public TextView progress_tv;

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_http);

        progress_tv = (TextView)findViewById(R.id.progress_tv);

        dataInfo = new DownloadInfo("http://t-newspaper-management.aegis-info.com/file/download?deviceId=all_all&name=1559733119_baiduren.mp4");

    }

    public void onBtnClick(View view){

//        String sdVideoUrl = getVideoPath(dataInfo.getUrl());
//        if (!TextUtils.isEmpty(sdVideoUrl)) {
//            dataInfo.setProgress(100);
//        } else {
//            File pFile = new File(mPath);
//            if (!pFile.exists()) {
//                pFile.mkdirs();
//            }
////            dataInfo.setProgress(0);
//
//
//        }

        FileInfo info = new FileInfo();
        info.setUrl("http://t-newspaper-management.aegis-info.com/file/download?deviceId=all_all&name=1559700153_baiduren.mp4");
//        info.setUrl("https://o8g2z2sa4.qnssl.com/android/momo_8.18.5_c1.apk");
//        info.setFileName("111.apk");
        info.setFileName("baiduren.mp4");

        new DownLoadTask(info, null, new OnProgressListener() {
            @Override
            public void updateProgress(long max, long progress) {
                if(max <= 0){
                    return;
                }
                float pro = ((int)(progress*10000/max))/100f;
                DecimalFormat format2 = new DecimalFormat( "0.00 ");
                progress_tv.setText("已经下载"+format2.format(pro)+"%");
            }
        }).start();
    }

    //获取视频缓存地址
    private String getVideoPath(String fileUrl) {
        File pFile = new File(mPath);
        if (!pFile.exists()) {
            pFile.mkdirs();
            return "";
        }

        String fileName = fileUrl;
        try {
            fileName =  MD5.md5(fileUrl) + ".mp4";
        } catch (Exception e) {
            return "";
        }
        File file = new File(mPath, fileName);
        if (file.exists()) {
            return file.toString();
        }

        return "";

    }
}
