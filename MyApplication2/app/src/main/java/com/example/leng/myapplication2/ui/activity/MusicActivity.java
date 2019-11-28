package com.example.leng.myapplication2.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.SnArVideoTab;
import com.example.leng.myapplication2.ui.server.MediaServer;

import java.util.Random;

public class MusicActivity extends AppCompatActivity {

//    String url = "http://fdfs.xmcdn.com/group4/M02/28/FA/wKgDtFM052_jBsKhAAvPQEMti4w713.mp3";
    String url = "http://sndsp.pptvyun.com/merge/866e3/OSKtkId-zyAVpReDtHoIY6zRhrQ/eyJkbCI6MTUzNjg4NzExMywiZXMiOjI1OTIwMDAsImlkIjoiMGEyZG42YWVvYUNrbmFhTDRLMmZvYXVlcGV5aW02dWJvcU9oIiwidiI6IjEuMCJ9/0a2dn6aeoaCknaaL4K2foauepeyim6uboqOh.mp3";


    private SnArVideoTab videoTab;
    private Button btnStart;
    private Button btnStop;

    MediaServer mediaServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        String aa = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(aa)){
            url = aa;
        }


        ImageView imageview = (ImageView) findViewById(R.id.imageview);
        ImageButton ccc_ib = (ImageButton) findViewById(R.id.ccc_ib);

        ccc_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MusicActivity.this,"透明imageButton点击事件",Toast.LENGTH_SHORT).show();
            }
        });

//        ObjectAnimator icon_anim = ObjectAnimator.ofFloat(imageview, "rotation", 0.0F, 359.0F);
//        icon_anim.setRepeatCount(-1);
//        icon_anim.setDuration(500);
//        LinearInterpolator interpolator = new LinearInterpolator();
//        icon_anim.setInterpolator(interpolator); //设置匀速旋转，不卡顿
//        icon_anim.start();

        videoTab = (SnArVideoTab) findViewById(R.id.video_tab);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);

        mediaServer = new MediaServer(this);

        mediaServer.setMediaPlayerProgress(new MediaServer.OnMediaPlayerProgress() {
            @Override
            public void onProgressChange(int progress) {

            }

            @Override
            public void onCreate(int totalProgress) {
                Toast.makeText(MusicActivity.this,"totalProgress = "+totalProgress ,Toast.LENGTH_SHORT).show();
                Log.e("asd","totalProgress = "+totalProgress);
            }

            @Override
            public void onEnd() {

            }
        });

        mediaServer.setSource(url);

        videoTab.setListener(new SnArVideoTab.OnChangeListener() {
            @Override
            public void onItemChange(int position) {
                Toast.makeText(MusicActivity.this,"item "+position ,Toast.LENGTH_SHORT).show();
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaServer.startMusic(15000);

//                videoTab.setCurrentItem(new Random().nextInt(3));
//                videoTab.setCurrentItem(0);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaServer.stopMusic();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        videoTab.setCurrentItem(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaServer.stopMusic();
    }

    @Override
    protected void onDestroy() {
        mediaServer.release();
        super.onDestroy();
    }
}
