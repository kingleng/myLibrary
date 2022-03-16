package com.example.leng.myapplication.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.leng.myapplication.view.event.FinishEvent;
import com.example.leng.myapplication.view.tools.DensityUtil;
import com.example.leng.myapplication.view.tools.WHUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by leng on 2017/2/27.
 */

public class MyBottomSheetBehavior extends CoordinatorLayout.Behavior {

    private static int MAX_VALUE = 1000;
    public static int DEFUALT_VALUE = 700;
    private static int HALF_VALUE = 450;
    private static int DOWN_DURATION = 250;

    int height;
    int width;
    float zoomValue;

    Context context;

    CallBack mCallBack;

    private Animation mFinishAnimation;



    public abstract static class CallBack{
        public abstract void finish();
    }

    public void setCallBack(CallBack mCallBack){
        this.mCallBack = mCallBack;
    }

    public MyBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onLayoutChild( CoordinatorLayout parent, View child, int layoutDirection ) {
//        Log.e("onLayoutChild","onLayoutChild");
        // First let the parent lay it out
        if (ViewCompat.getFitsSystemWindows(parent) &&
                !ViewCompat.getFitsSystemWindows(child)) {
            ViewCompat.setFitsSystemWindows(child, true);
        }
        parent.onLayoutChild(child, layoutDirection);

        /**
         * New behavior
         */
        ViewCompat.offsetTopAndBottom(child, DEFUALT_VALUE);


        if(width == 0){
            child.setAlpha(0);
//            height = DensityUtil.dip2px(child.getContext(),300);
            width = WHUtil.getWidth(child.getContext())[0];
            height = WHUtil.getWidth(child.getContext())[1];
            zoomValue = DensityUtil.dip2px(child.getContext(),60)/(float)width;
            finishAnimation(child);
        }

        return true;
    }


    float[] lastPoint = new float[2];
    boolean result = false;
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {

        int action = ev.getAction();
        result = false;
        if(MotionEvent.ACTION_DOWN == action){
            lastPoint[0] = ev.getX();
            lastPoint[1] = ev.getY();
            lastY = ev.getY();
            return false;
        }
        if(MotionEvent.ACTION_MOVE == action){
            if(Math.abs(ev.getX()-lastPoint[0])<Math.abs(ev.getY()-lastPoint[1])){
                result = true;
            }
        }
        if(MotionEvent.ACTION_UP == action){

        }

        return result;
    }

    float lastY = 0;
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = child.getY() + ev.getY() - lastY;
                if(newY>0){
                    child.setY(newY);
                    lastY = ev.getY();
                    float alpha = Math.max(Math.min((DEFUALT_VALUE-child.getY())/DEFUALT_VALUE,1),0);
                    child.setAlpha(alpha);
                    ViewGroup.LayoutParams lp = child.getLayoutParams();
                    lp.width = (int)((width*(alpha*zoomValue+(1f-zoomValue))));
                    child.setLayoutParams(lp);
                    child.setX(width*zoomValue/2f*(1f-alpha));
                    if(child.getY() == DEFUALT_VALUE){
                        child.setVisibility(View.GONE);
                    }else{
                        child.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                finishAnimation(child);
                break;
        }

        return true;
    }

    private void finishAnimation(final View view){

        final float valueY = view.getY();


        mFinishAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                if(valueY<HALF_VALUE){
                    view.setY(valueY*(1-interpolatedTime));
                }else if(valueY<DEFUALT_VALUE){
                    view.setY((DEFUALT_VALUE-valueY)*interpolatedTime+valueY);
                }else if(valueY<MAX_VALUE){
                    view.setY(valueY - (valueY-DEFUALT_VALUE)*interpolatedTime);
                }else{
                    view.setY(valueY + (height-valueY)*interpolatedTime);
                }

                float alpha = Math.max(Math.min((DEFUALT_VALUE-view.getY())/DEFUALT_VALUE,1),0);
                view.setAlpha(alpha);
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.width = (int)((width*(alpha*zoomValue+(1f-zoomValue))));
                view.setLayoutParams(lp);
                view.setX(width*zoomValue/2f*(1f-alpha));
                if(view.getY() == DEFUALT_VALUE){
                    view.setVisibility(View.GONE);
                }else{
                    view.setVisibility(View.VISIBLE);
                }

            }
        };
        mFinishAnimation.setDuration(DOWN_DURATION);
        mFinishAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(view.getY() >= (height-10)){
                    if(mCallBack!=null){
                        mCallBack.finish();
                    }
                    EventBus.getDefault().post(new FinishEvent());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.clearAnimation();
        view.startAnimation(mFinishAnimation);

    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    public static MyBottomSheetBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof MyBottomSheetBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with MyBottomSheetBehavior");
        }
        return (MyBottomSheetBehavior) behavior;
    }


}
