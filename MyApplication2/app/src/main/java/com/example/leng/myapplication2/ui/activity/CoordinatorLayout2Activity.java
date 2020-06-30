package com.example.leng.myapplication2.ui.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.MyPagerAdapter;
import com.example.leng.myapplication2.event.FinishEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorLayout2Activity extends AppCompatActivity {

    ViewPager viewPager;
    List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout2);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        views = new ArrayList<>();
        for(int i=0;i<5;i++){
            ImageView view = new ImageView(this);
            view.setFitsSystemWindows(true);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageResource(R.drawable.defult_bg);
            views.add(view);
        }
        viewPager.setAdapter(new MyPagerAdapter(views));


//        View bottomSheet = findViewById(R.id.bottom_sheet);
//        MyBottomSheetBehavior behavior = MyBottomSheetBehavior.from(bottomSheet);
//        behavior.setCallBack(new MyBottomSheetBehavior.CallBack() {
//            @Override
//            public void finish() {
//                finish();
//            }
//        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishEvent event){
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }



    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
