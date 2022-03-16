package com.example.leng.myapplication.base.plugIn;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.leng.myapplication.R;

/**
 * Created by leng on 2020/3/9.
 */
public class TargetActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("TargetActivity","onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TargetActivity","onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TargetActivity","onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TargetActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TargetActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TargetActivity","onDestroy");
    }
}
