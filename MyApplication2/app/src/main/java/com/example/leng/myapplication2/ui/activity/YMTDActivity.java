package com.example.leng.myapplication2.ui.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.leng.myapplication2.R;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class YMTDActivity extends AppCompatActivity {

    TextView setting;
    TextView text;
    TextView text2;
    Button start;
    Button tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_y_m_t_d);

        initView();


    }

    private void initView(){
        setting = findViewById(R.id.setting);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);
        start = findViewById(R.id.start);
        tag = findViewById(R.id.tag);

        setting.setOnClickListener(view -> {

        });

        start.setOnClickListener(view -> {
            MyTimeer.schedule(mTask,0,1000);
        });

        tag.setOnClickListener(view -> {
            tagNum++;
            text2.setText("胎动次数："+tagNum);
        });
    }

    Timer MyTimeer = new Timer();

    Long time = 1*60*60L;
    int tagNum = 0;

    TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            mH.sendEmptyMessage(0);
        }
    };

    MyHandler mH = new MyHandler(this);

    class MyHandler extends Handler{

        WeakReference<Activity> weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Activity mA = weakReference.get();
            if(mA!=null){
                time--;
                text.setText("倒计时："+dateFormat(time));
            }
        }
    };

    private String dateFormat(Long date){

        if(date>0){
            long second = date%(60);
            long minute = (date/60)%60;
            long hour = (date/(60*60))%24;

            String mH = hour>9?hour+"":"0"+hour;
            String mM = minute>9?minute+"":"0"+minute;
            String mS = second>9?second+"":"0"+second;

            return mH+":"+mM+":"+mS;
        }else{
            return "00:00:00";
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(MyTimeer!=null){
            MyTimeer.cancel();
            MyTimeer.purge();
            MyTimeer = null;
        }

        if(mTask!=null){
            mTask.cancel();
            mTask = null;
        }
    }
}
