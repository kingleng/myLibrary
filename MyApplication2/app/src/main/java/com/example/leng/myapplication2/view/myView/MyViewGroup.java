package com.example.leng.myapplication2.view.myView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * Created by leng on 2016/11/17.
 */

public class MyViewGroup extends ViewGroup {

    public MyViewGroup(Context context) {
        super(context);
        init();
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        yibiaopan yibiaopan = new yibiaopan(getContext());
        yibiaopan.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        this.addView(yibiaopan);

        yibiaopan2 yibiaopan2 = new yibiaopan2(getContext());
        yibiaopan2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        this.addView(yibiaopan2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChild(getChildAt(0),widthMeasureSpec,heightMeasureSpec);
        measureChild(getChildAt(1),widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getChildAt(0).layout(l,t,r,b);
        getChildAt(1).layout(l,t,r,b);
    }

    ObjectAnimator animator;
    ObjectAnimator setValueAnimator;
    ObjectAnimator starAnimator;

    private void MyAnimator(){
        animator = ObjectAnimator.ofFloat(getChildAt(0),"showTime",0f,1f);
        animator.setDuration(2*1000);
        animator.setInterpolator(new LinearInterpolator());

        setValueAnimator = ObjectAnimator.ofFloat(getChildAt(0),"valueAnim",0f,1f);
        setValueAnimator.setDuration(1*1000);
        setValueAnimator.setInterpolator(new LinearInterpolator());

        starAnimator = ObjectAnimator.ofFloat(getChildAt(1),"starAnim",0f,1f);
        starAnimator.setRepeatCount(ValueAnimator.INFINITE);
        starAnimator.setDuration(2*1000);
        starAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startAnim(){
        if(animator!=null){
            animator.start();
        }
        if(setValueAnimator!=null){
            setValueAnimator.start();
        }
        if(starAnimator!=null){
            starAnimator.start();
        }

    }

    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        super.dispatchWindowVisibilityChanged(visibility);
        if(starAnimator!=null){
            starAnimator.end();
        }
    }
}
