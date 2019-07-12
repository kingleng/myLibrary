package com.example.leng.myapplication2.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.SnArMusicSeekBar2;
import com.example.leng.myapplication2.ui.myView.yibiaopan;
import com.example.leng.myapplication2.ui.myView.yibiaopan2;

import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    private yibiaopan ybp;
    private yibiaopan2 ybp2;
    private Button start;
    private SnArMusicSeekBar2 sn_ar_music_seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ybp = (yibiaopan)findViewById(R.id.yibiaopan);
        ybp2 = (yibiaopan2)findViewById(R.id.yibiaopan2);
        start = (Button)findViewById(R.id.demo_btn);
        sn_ar_music_seekbar = (SnArMusicSeekBar2)findViewById(R.id.sn_ar_music_seekbar);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ybp.startSetValueAnim();
            }
        });


        sn_ar_music_seekbar.setmTotal(30*1000);
        sn_ar_music_seekbar.setMyStart(0);
        sn_ar_music_seekbar.setMyEnd(15*1000);
        sn_ar_music_seekbar.setMyValue(0);

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);

            }
        };

        timer.schedule(task,0,10);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sn_ar_music_seekbar.setMyValue(sn_ar_music_seekbar.getMyValue()+10);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        ybp.startAnim();
        ybp2.startAnim();
    }
}
