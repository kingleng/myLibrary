package com.example.leng.myapplication2.ui.myView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by j on 2019/4/3.
 */
public class DepthViewPager extends ViewPager {

    OnItemChangeListener listener;

    public OnItemChangeListener getListener() {
        return listener;
    }

    public void setListener(OnItemChangeListener listener) {
        this.listener = listener;
    }

    public DepthViewPager(Context context) {
        super(context,null);
    }

    public DepthViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true, new DepthPageTransformer());

    }


    @Override
    public void setAdapter(PagerAdapter adapter) {
        setOffscreenPageLimit(5);
        super.setAdapter(adapter);
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int position = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                this.position = position;

                int currentItem = position -1;
                if(currentItem == -1){
                    currentItem = adapter.getCount()-1;
                }else if(currentItem == adapter.getCount()){
                    currentItem = 0;
                }
                if(listener!=null){
                    listener.onItemChang(currentItem);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==0){
                    if(position == getAdapter().getCount()-1){
                        setCurrentItem(1,false);
                    }

                    if(position == 0){
                        setCurrentItem(getAdapter().getCount()-2,false);
                    }
                }

            }
        });
        setCurrentItem(1);
        startSrcoll();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                mHandler.removeCallbacks(mHandlerRunnable);
                break;
            case MotionEvent.ACTION_MOVE:
                isTouch = true;
                mHandler.removeCallbacks(mHandlerRunnable);
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                mHandler.postDelayed(mHandlerRunnable,3000);
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                mHandler.postDelayed(mHandlerRunnable,3000);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void startSrcoll(){
        mHandler.postDelayed(mHandlerRunnable,3000);
    }

    Runnable mHandlerRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.e("asd","run");
            if(getChildCount()<=1){
                return;
            }
            mHandler.sendEmptyMessage(0);
        }
    };

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        super.setPageTransformer(reverseDrawingOrder, transformer);
//        super.setPageTransformer(true, new DepthPageTransformer());
    }

    private boolean isTouch = false;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!isTouch){
                int newItem = getCurrentItem()+1;
                int childCount = getAdapter().getCount();
                if(newItem < childCount){
                    setCurrentItem(newItem);
//                    if(listener!=null){
//                        listener.onItemChang(newItem);
//                    }
                }
                mHandler.postDelayed(mHandlerRunnable,3000);
            }


        }
    };


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class DepthPageTransformer implements ViewPager.PageTransformer {

        public static final float MAX_SCALE = 1.0f;
        public static final float MIN_SCALE = 0.85f;

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

    public interface OnItemChangeListener{
        void onItemChang(int postion);
    }
}