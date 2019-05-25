package com.example.leng.myapplication2.view.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leng.myapplication2.R;

public class FrameAnimationActivity extends AppCompatActivity {

    private ImageView imageView0;
    private ImageView imageView;
    private Button start;
    private Button stop;

    private AnimationDrawable frameAnim0;
    private AnimationDrawable frameAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageView0 = (ImageView) findViewById(R.id.imageView0);
        imageView = (ImageView) findViewById(R.id.imageView);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        // 通过逐帧动画的资源文件获得AnimationDrawable示例
        frameAnim0 = (AnimationDrawable) getResources().getDrawable(R.drawable.voice_anim3);
        frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.voice_anim4);
        // 把AnimationDrawable设置为ImageView的背景
        imageView0.setBackgroundDrawable(frameAnim0);
        imageView.setBackgroundDrawable(frameAnim);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

    }

    /**
     * 开始播放
     */
    protected void start() {
        if (frameAnim0 != null && !frameAnim0.isRunning()) {
            frameAnim0.start();
            Toast.makeText(FrameAnimationActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
            Log.i("main", "index 为5的帧持续时间为：" + frameAnim.getDuration(5) + "毫秒");
            Log.i("main", "当前AnimationDrawable一共有" + frameAnim.getNumberOfFrames() + "帧");
        }
        if (frameAnim != null && !frameAnim.isRunning()) {
            frameAnim.start();
            Toast.makeText(FrameAnimationActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
            Log.i("main", "index 为5的帧持续时间为：" + frameAnim.getDuration(5) + "毫秒");
            Log.i("main", "当前AnimationDrawable一共有" + frameAnim.getNumberOfFrames() + "帧");
        }
    }

    /**
     * 68      * 停止播放
     * 69
     */
    protected void stop() {
        if (frameAnim0 != null && frameAnim0.isRunning()) {
            frameAnim0.stop();
            Toast.makeText(FrameAnimationActivity.this, "停止播放", Toast.LENGTH_SHORT).show();
        }
        if (frameAnim != null && frameAnim.isRunning()) {
            frameAnim.stop();
            Toast.makeText(FrameAnimationActivity.this, "停止播放", Toast.LENGTH_SHORT).show();
        }
    }

}
