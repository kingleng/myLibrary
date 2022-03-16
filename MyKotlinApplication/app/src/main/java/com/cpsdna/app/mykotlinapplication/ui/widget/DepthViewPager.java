package com.cpsdna.app.mykotlinapplication.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.cpsdna.app.mykotlinapplication.R;
import com.cpsdna.app.mykotlinapplication.util.ShapeBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j on 2019/4/3.
 */
public class DepthViewPager extends ViewPager {

    public boolean isScroll=true;
    private long mDelayMillis = 1000*10;

    OnItemChangeListener listener;

    LinearLayout dotLayout;
    List<ImageView> dotLists;

    public void setDotLayout(LinearLayout dotLayout) {
        if(dotLayout == null){
            return;
        }
        this.dotLayout = dotLayout;
        dotLayout.removeAllViews();
        dotLists = new ArrayList<>();
        int length = getAdapter().getCount()>1?getAdapter().getCount()-2:getAdapter().getCount();
        for(int i=0;i<length;i++){
            ImageView imageView = new ImageView(dotLayout.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.px24),(int)getResources().getDimension(R.dimen.px24));
            lp.leftMargin = (int)getResources().getDimension(R.dimen.px12);
            lp.rightMargin = (int)getResources().getDimension(R.dimen.px12);
            imageView.setLayoutParams(lp);
            if(i==0){
                imageView.setImageDrawable(ShapeBuilder.create().Radius((int)getResources().getDimension(R.dimen.px24)).Solid(Color.parseColor("#00fafb")).build());
            }else{
                imageView.setImageDrawable(ShapeBuilder.create().Radius((int)getResources().getDimension(R.dimen.px24)).Stroke(2, Color.WHITE).build());
            }
            dotLayout.addView(imageView);
            dotLists.add(imageView);
        }

        setListener(new OnItemChangeListener() {
            @Override
            public void onItemChang(int postion) {
                for(int i=0;i<dotLists.size();i++){
                    if(postion == i){
                        dotLists.get(i).setImageDrawable(ShapeBuilder.create().Radius((int)getResources().getDimension(R.dimen.px24)).Solid(Color.parseColor("#00fafb")).build());
                    }else{
                        dotLists.get(i).setImageDrawable(ShapeBuilder.create().Radius((int)getResources().getDimension(R.dimen.px24)).Stroke(2, Color.WHITE).build());
                    }
                }
            }
        });
    }

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
//        setPageTransformer(true, new DepthPageTransformer());
        setViewPagerScrollSpeed();
    }

    private void setViewPagerScrollSpeed( ){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext( ),new LinearInterpolator());
            mScroller.set(this,scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        setOffscreenPageLimit(15);
        super.setAdapter(adapter);

        setCurrentItem(1);
        startScroll();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                mHandler.removeMessages(1);
                break;
            case MotionEvent.ACTION_MOVE:
                isTouch = true;
                mHandler.removeMessages(1);
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                mHandler.sendEmptyMessageDelayed(1, mDelayMillis);
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                mHandler.sendEmptyMessageDelayed(1, mDelayMillis);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void scrollToNext() {
        stopScroll();
        isTouch = true;
        int currentItem = getCurrentItem();
        currentItem++;
        setCurrentItem(currentItem,true);
        startScroll();
        isTouch = false;
    }
    public void scrollToPrevious() {
        stopScroll();
        isTouch = true;
        int currentItem = getCurrentItem();
        currentItem--;
        setCurrentItem(currentItem,true);
        startScroll();
        isTouch = false;
    }

    public void scrollToItemByPosition(int position) {
        stopScroll();
        isTouch = true;
        setCurrentItem(position,true);
        startScroll();
        isTouch = false;
    }

    /**
     * 停止无限轮播
     */
    public void stopScroll() {
        mHandler.removeMessages(1);
        isScroll=false;
    }

    public void startScroll(){
        stopScroll();
        mHandler.sendEmptyMessageDelayed(1, mDelayMillis);
    }

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

            if(getChildCount()<=1){
                return;
            }

            if(!isTouch){
                int newItem = getCurrentItem()+1;
                int childCount = getAdapter().getCount();
                if(newItem < childCount){
                    setCurrentItem(newItem);
//                    if(listener!=null){
//                        listener.onItemChang(newItem);
//                    }
                }

                mHandler.sendEmptyMessageDelayed(1, mDelayMillis);
            }


        }
    };


//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public class DepthPageTransformer implements PageTransformer {
//
//        public static final float MAX_SCALE = 1.0f;
//        public static final float MIN_SCALE = 0.85f;
//
//        public DepthPageTransformer() {
//        }
//
//        @Override
//        public void transformPage(View page, float position) {
//            if (position < -1) {
//                position = -1;
//            } else if (position > 1) {
//                position = 1;
//            }
//            float tempScale = position < 0 ? 1 + position : 1 - position;
//            float slope = (MAX_SCALE - MIN_SCALE) / 1;
//            float scaleValue = MIN_SCALE + tempScale * slope;
//
//            page.setScaleX(scaleValue);
//            page.setScaleY(scaleValue);
//        }
//    }

    public interface OnItemChangeListener{
        void onItemChang(int postion);
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {

            super.startScroll(startX, startY, dx, dy, mDuration);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}