package com.example.leng.myapplication.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.myView.yibiaopan;
import com.example.leng.myapplication.view.myView.yibiaopan2;

public class DemoActivity extends AppCompatActivity {

    private yibiaopan ybp;
    private yibiaopan2 ybp2;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ybp = (yibiaopan)findViewById(R.id.yibiaopan);
        ybp2 = (yibiaopan2)findViewById(R.id.yibiaopan2);
        start = (Button)findViewById(R.id.demo_btn);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ybp.startSetValueAnim();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        ybp.startAnim();
        ybp2.startAnim();
    }
}
