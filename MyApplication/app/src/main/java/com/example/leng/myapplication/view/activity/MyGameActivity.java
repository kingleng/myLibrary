package com.example.leng.myapplication.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.myView.MyGameView;

public class MyGameActivity extends AppCompatActivity {

    MyGameView myGameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game);
        myGameView = (MyGameView)findViewById(R.id.mygameview);
        myGameView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myGameView.stop();
    }
}
