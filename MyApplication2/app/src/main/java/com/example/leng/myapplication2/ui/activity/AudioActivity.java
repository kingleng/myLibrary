package com.example.leng.myapplication2.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.leng.myapplication2.R;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    public static final String LOG_TAG = AudioActivity.class.getSimpleName();

    public Button start;
    public Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);


        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = stopRecord();
                Intent intent = new Intent(AudioActivity.this,MusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",key);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }

    private MediaRecorder recorder;
    private String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordertest.wav";

    public void startRecord() {

        recorder = new MediaRecorder();
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }catch (IllegalStateException e){
            Log.i(LOG_TAG, "设置录音源失败");
            e.printStackTrace();
        }

        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        }catch (IOException e){
            Log.e(LOG_TAG, "准备失败");
            e.printStackTrace();
        }
        recorder.start();

        Log.i(LOG_TAG, "开始录音...");
    }

    public String stopRecord() {

        if(recorder == null){
            return fileName;
        }

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;

        Log.e(LOG_TAG, "停止录音"+fileName.toString());

        return fileName;
    }
}
