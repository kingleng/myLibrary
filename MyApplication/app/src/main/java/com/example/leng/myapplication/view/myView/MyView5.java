package com.example.leng.myapplication.view.myView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.leng.myapplication.R;

/**
 * Created by leng on 2016/10/20.
 */

public class MyView5 extends ListView {

    ImageView mHeadView;
    int mHeadViewMeasuredHeight;
    int mHeadViewMeasuredHeight2;

    private ObjectAnimator Anim;

    float animaValue;

    public float getAnimaValue() {
        return animaValue;
    }

    public void setAnimaValue(float animaValue) {
        this.animaValue = animaValue;

        Log.e("asd","animaValue::"+animaValue);
        // 测量头部加载更多控件的高度
        mHeadView.measure(0, 0);
        // 头部刷新控件的高度
        mHeadViewMeasuredHeight2 = mHeadView.getMeasuredHeight();

        mHeadView.setPadding(0,(int)((mHeadViewMeasuredHeight2-mHeadViewMeasuredHeight)*(1.0f-animaValue)),0,0);

        if(animaValue == 1.0f){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHeadView.setPadding(0,-mHeadViewMeasuredHeight,0,0);
                }
            },2000);
        }

    }

    public MyView5(Context context) {
        super(context);
        initView();
    }

    public MyView5(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyView5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){

        initHead();
        Animator();
    }

    private void Animator(){
        Anim = ObjectAnimator.ofFloat(
                this, "animaValue", 0f, 1f);
        Anim.setDuration(2000);
        Anim.setInterpolator(new LinearInterpolator());
    }



    private void initHead(){
        mHeadView = new ImageView(this.getContext());
        mHeadView.setImageResource(R.mipmap.ic_launcher);
        // 测量头部加载更多控件的高度
        mHeadView.measure(0, 0);
        // 头部刷新控件的高度
        mHeadViewMeasuredHeight = mHeadView.getMeasuredHeight();
        // 设置Padding值，使头部下拉控件默认不可见
        mHeadView.setPadding(0, -mHeadViewMeasuredHeight, 0, 0);
        addHeaderView(mHeadView);
    }

    float lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("asd","asdsdaad"+event.getAction());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if(Anim != null){
                    Anim.end();
                }
                float currntY = event.getY();
                float dx = currntY - lastY;
                // 设置顶部控件Padding的参数
                float scrollYDistance = -mHeadViewMeasuredHeight + dx;

                Log.e("asd","dx::"+dx + "::mHeadViewMeasuredHeight::" + mHeadViewMeasuredHeight + "::scrollYDistance::"+scrollYDistance);

                mHeadView.setPadding(0,(int)scrollYDistance,0,0);

                break;
            case MotionEvent.ACTION_UP:
//                mHeadView.setPadding(0,0,0,0);

                if(Anim != null){
                    Anim.start();
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    int mMaxOverDistance = 100;

//    @Override
//    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
//                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverDistance, isTouchEvent);
//    }
}
