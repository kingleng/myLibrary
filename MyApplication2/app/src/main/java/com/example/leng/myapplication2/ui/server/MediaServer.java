package com.example.leng.myapplication2.ui.server;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 17092234 on 2018/8/7.
 */

public class MediaServer {

    MediaPlayer mediaPlayer;
    Activity mActivity;
    Uri uri;

    OnMediaPlayerProgress mediaPlayerProgress;

    int progress;

    Timer mTimer;
    TimerTask timerTask;

    public MediaServer(Activity activity) {
        this.mActivity = activity;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer = MediaPlayer.create(activity, R.raw.welcome_to_planet_urf);

    }

    public void setSource(String url){

        try {
            mediaPlayer.reset();
//            Uri uri = Uri.parse("http://fdfs.xmcdn.com/group4/M02/28/FA/wKgDtFM052_jBsKhAAvPQEMti4w713.mp3");
            uri = Uri.parse(url);
            mediaPlayer.setDataSource(mActivity,uri);
            //播放前准备一下
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(mediaPlayerProgress!=null){
                    mediaPlayerProgress.onCreate(mediaPlayer.getDuration());
                }
            }
        });


    }

    public void setMediaPlayerProgress(OnMediaPlayerProgress mediaPlayerProgress) {
        this.mediaPlayerProgress = mediaPlayerProgress;
    }

    public void startMusic(){

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mTimer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if(mediaPlayerProgress!=null){
                            mediaPlayerProgress.onProgressChange(mediaPlayer.getCurrentPosition());
                        }
                    }
                };
                mTimer.schedule(timerTask, 0, 10);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(mediaPlayerProgress!=null){
                            mediaPlayerProgress.onEnd();
                        }

                    }
                });
            }
        });



        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });

    }

    public void startMusic(final int duartion){


        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mActivity,uri);
            //播放前准备一下
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mTimer = new Timer();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if(progress+duartion<mediaPlayer.getCurrentPosition()){
                                mediaPlayer.seekTo(progress);
                            }
                            if(mediaPlayerProgress!=null){
                                mediaPlayerProgress.onProgressChange(mediaPlayer.getCurrentPosition());
                            }
                        }
                    };
                    mTimer.schedule(timerTask, 0, 10);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayerProgress.onEnd();
            }
        });

    }

    public void setProgress(int progress){

        this.progress = progress;

        if(mediaPlayer!=null){
            mediaPlayer.seekTo(progress);
        }

    }

    public void stopMusic(){

        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }

        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }

    }

    public void release(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }

        if(timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }

    }

    public interface OnMediaPlayerProgress{
        void onProgressChange(int progress);
        void onCreate(int totalProgress);
        void onEnd();
    }

}
