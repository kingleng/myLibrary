package com.example.leng.myapplication2.ui.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.app.MyApplication;

public class VideoViewActivity extends Activity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        videoView = (VideoView) findViewById(R.id.videoview);

        videoView.setMediaController(new MediaController(this));

//        String url = "http://newspaper-management.aegis-info.com/file/download?name=admin_6672149472645786031.MP4";
//        String url = "https://media.w3.org/2010/05/sintel/trailer.mp4";
//        String url = "http://newspaper-management.aegis-info.com/file/download?name=admin_213421.mp4";
//        String url = "http://v.videoincloud.com/sgy/20190614/oQMpsG/oQMpsG.m3u8";
//        HttpProxyCacheServer proxy = MyApplication.getProxy(this);
//        String proxyUrl = proxy.getProxyUrl(url);
//        videoView.setVideoURI(Uri.parse(url));//播放网络视频
//        videoView.setVideoPath(proxyUrl);//播放网络视频
//        videoView.setVideoPath(url);//播放网络视频


//        File file = new File(Environment.getExternalStorageDirectory(),"aegis/videoDownload/2c6cd48441a89cc5eb00732868e6f77e240d88b6.mp4");
////        File file = new File("/storage/emulated/0/aegis/videoDownload/d5b7ff0de37dd291cd44f34f2e5d8356f3f40f30.mp4");
//        String filePath = file.getPath();
//        videoView.setVideoPath(filePath); // 指定视频文件的路径

        String url = MyApplication.getVideoProxyServer().getProxyUrl("http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4");
        videoView.setVideoPath(url);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i("tag", "准备完毕,可以播放了");
                videoView.setVisibility(View.VISIBLE);
            }
        });

        videoView.start(); // 开始播放
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

}
