package com.example.leng.myapplication2.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DepthPageTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 1.0f;
    public static final float MIN_SCALE = 0.7f;

    public DepthPageTransformer() {
    }

    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        float tempScale = position < 0 ? 1 + position : 1 - position;
        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;

        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
    }
}