package com.example.leng.myapplication.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.leng.myapplication.view.tools.DensityUtil;
import com.example.leng.myapplication.view.tools.WHUtil;

/**
 * Created by leng on 2017/2/27.
 */

public class ViewPagerBehavior extends CoordinatorLayout.Behavior {

    int mCurrentChildY;

    int width;
    float zoomValue;


    public ViewPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(width == 0){
            width = WHUtil.getWidth(context)[0];
            zoomValue = DensityUtil.dip2px(context,60)/(float)width;
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if(dependency instanceof NestedScrollView){
            return true;
        }
        return false;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        int lastCurrentChildY = mCurrentChildY;

        float dh = dependency.getY();
        float ch = child.getHeight();


        mCurrentChildY = Math.max((int)(dependency.getY()),0);

        child.setY(mCurrentChildY);

        float alpha = Math.max(Math.min(1-(700-dh)/700f,1),0);
//        Log.e("onDependentViewChanged","alpha = "+alpha);
        child.setAlpha(alpha);

        ViewGroup.LayoutParams lp = child.getLayoutParams();
        lp.width = (int)((width*(1f-alpha*zoomValue)));
        child.setLayoutParams(lp);
        child.setX(width*zoomValue/2f*(alpha));
        if(alpha<0.1f){
            child.setVisibility(View.GONE);
        }else{
            child.setVisibility(View.VISIBLE);
        }

        return lastCurrentChildY == mCurrentChildY;
    }
}
