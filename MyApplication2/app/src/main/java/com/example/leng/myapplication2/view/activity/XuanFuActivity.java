package com.example.leng.myapplication2.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.tools.AndroidUtil;
import com.example.leng.myapplication2.view.tools.DensityUtil;
import com.example.leng.myapplication2.view.tools.SlideUtil;

public class XuanFuActivity extends Activity {

    View shareLayout;

    SlideUtil mSlideUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_fu);
        shareLayout = findViewById(R.id.share_layout);

        mSlideUtil = new SlideUtil.Builder(shareLayout)
//                .withDistance(DensityUtil.dip2px(this, 100))
                .withDistance(300)
                .build();

        mSlideUtil.hide(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSlideUtil.show(true);
            }
        },2000);



        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSlideUtil.isShow()){
                    mSlideUtil.hide(true);
                }else{
                    mSlideUtil.show(true);
                }

            }
        });
    }
}
