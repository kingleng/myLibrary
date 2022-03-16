package com.example.leng.myapplication2.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.MarqueeView;

public class MarQueeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_quee);

        MarqueeView view1 =  findViewById(R.id.view1);
        view1.setTexts("今天的天气非常好啊，是不是");
    }
}
