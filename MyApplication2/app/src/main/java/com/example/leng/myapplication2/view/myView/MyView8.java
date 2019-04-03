package com.example.leng.myapplication2.view.myView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by leng on 2016/11/11.
 */

public class MyView8 extends View {

    private Paint mPaint;
    private float centerWith;
    private float centerHeight;


    int i = 0;
    private float value = 0;
    private int duretion = 5;
    private ObjectAnimator myviewAnim;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;

        if(value<=0.25){
            i = 1;
        }else if(value<=0.5){
            i = 2;
        }else if(value<=0.75){
            i = 3;
        }else{
            i = 4;
        }
        invalidate();
    }

    public void setDuretion(int duretion) {
        this.duretion = duretion;
    }

    public MyView8(Context context) {
        super(context);
        init();
    }

    public MyView8(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView8(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.parseColor("#ffffff"));

        myviewAnim = ObjectAnimator.ofFloat(
                this, "value", 0f, 1f);
        myviewAnim.setRepeatCount(ValueAnimator.INFINITE);
        myviewAnim.setDuration(duretion * 1000);
        myviewAnim.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initWith();
    }

    private void initWith(){
        centerWith = getWidth()/2.0f;
        centerHeight = getHeight()/2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        switch (i){
            case 4:
                Path mPath4 = new Path();
                float dx4 = centerWith - centerWith * (value - 0.75f)*4;
                float dy4 = 2*centerHeight - centerHeight * (value - 0.75f)*4;
                mPath4.moveTo(centerWith, 2*centerHeight);
                mPath4.lineTo(dx4,dy4);
                mPath4.close();
                canvas.drawPath(mPath4,mPaint);
            case 3:
                Path mPath3 = new Path();
                float dx3 = 2*centerWith - centerWith * (value - 0.5f)*4;
                float dy3 = centerHeight + centerHeight * (value - 0.5f)*4;
                mPath3.moveTo(2*centerWith, centerHeight);
                mPath3.lineTo(dx3,dy3);
                mPath3.close();
                canvas.drawPath(mPath3,mPaint);
            case 2:
                Path mPath2 = new Path();
                float dx2 = centerWith+ centerWith * (value - 0.25f)*4;
                float dy2 = centerHeight * (value - 0.25f)*4;
                mPath2.moveTo(centerWith, 0.0f);
                mPath2.lineTo(dx2,dy2);
                mPath2.close();
                canvas.drawPath(mPath2,mPaint);
            case 1:
                Path mPath5 = new Path();
                float dx1 = 0.0f+ centerWith * value*4;
                float dy1 = centerHeight - centerHeight * value*4;
                mPath5.moveTo(0.0f, centerHeight);
                mPath5.lineTo(dx1,dy1);
                mPath5.close();
                canvas.drawPath(mPath5,mPaint);
        }

    }

    public void startAnim(){
        if(myviewAnim!=null){
            myviewAnim.start();
        }

    }

    public void stopAnim(){
        if(myviewAnim!=null){
            myviewAnim.end();
        }
    }
}
