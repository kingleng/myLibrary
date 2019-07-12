package com.example.leng.myapplication2.ui.myView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by leng on 2017/5/5.
 *
 * 一个小圆随波逐流的动画
 */

public class MyGameView extends View {

    //波浪底
    Paint mPaint1;
    //小圆
    Paint mPaint2;

    //动画参数
    private float starAnim = 0;

    public MyGameView(Context context) {
        super(context);
        init();
    }

    public MyGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        MyAnimator();

        mPaint1 = new Paint();
        mPaint1.setColor(Color.parseColor("#44ef44"));
        mPaint1.setAntiAlias(true);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setStrokeWidth(2);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.parseColor("#ff2b52"));
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.FILL);


    }

    public void setStarAnim(float starAnim) {
        this.starAnim = starAnim;
        invalidate();
    }

    private double mDefaultAngularFrequency;
    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaterStartX;

    int endX;
    int endY;

    float lastBeginY = 0;

    int count = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDefaultAngularFrequency = 2.0f * Math.PI / 1.0f / getWidth();
        mDefaultAmplitude = getHeight() * 0.05f;
        mDefaultWaterLevel = getHeight() * 0.5f;

        mDefaultWaterStartX = getWidth() * starAnim;

        endX = getWidth() + 1;
        endY = getHeight() + 1;


        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = (beginX + mDefaultWaterStartX) * mDefaultAngularFrequency;
            float beginY = (float) ( mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            if(lastBeginY!=0 && count>0){
                canvas.drawLine(beginX, lastBeginY, beginX, endY, mPaint1);
                count--;
            }else{
//                count = (new Random().nextInt(10) == 3)?100:0;
                canvas.drawLine(beginX, beginY, beginX, endY, mPaint1);
                lastBeginY = beginY;
            }

        }


        double wx = (getWidth()/2.0f + mDefaultWaterStartX) * mDefaultAngularFrequency;
        float beginY = (float) ( mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
        if(touchParameter>0){
            if(isStartTouch){
                isStartTouch = false;
                touchParameter = (int)beginY - touchParameter;
                currentTouchParameter = (int)beginY;
            }
            if(currentTouchParameter>touchParameter && !isTop){
                currentTouchParameter -= 10;
            }else {
                isTop = true;
                if(currentTouchParameter<beginY){
                    currentTouchParameter += 10;
                }else{
                    currentTouchParameter = (int)beginY;
                    touchParameter = 0;
                }
            }
            Log.e("MyGameView","onDraw0000 :" + currentTouchParameter);
        }else{
            currentTouchParameter = (int)beginY;
            Log.e("MyGameView","onDraw11111 :" + currentTouchParameter);
        }


//        Log.e("MyGameView","onDraw :" + currentTouchParameter);
//        canvas.drawCircle(getWidth()/2.0f-15,beginY-15 - currentTouchParameter,30,mPaint2);
        canvas.drawCircle(getWidth()/2.0f-15,currentTouchParameter,30,mPaint2);

    }

    public void start(){
        if(starAnimator!=null){
            starAnimator.start();
        }
    }

    public void stop(){
        if(starAnimator!=null){
            starAnimator.cancel();
            starAnimator.clone();
        }
    }

    ObjectAnimator starAnimator;
    private void MyAnimator(){
        starAnimator = ObjectAnimator.ofFloat(this,"starAnim",0f,1f);
        starAnimator.setRepeatCount(ValueAnimator.INFINITE);
        starAnimator.setDuration(2*1000);
        starAnimator.setInterpolator(new LinearInterpolator());
    }

    int touchParameter = 0;
    int currentTouchParameter = 0;
    boolean isTop = false;
    boolean isStartTouch = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("MyGameView","onTouchEvent");

        touchParameter = 200;
//        currentTouchParameter = 0;
        isTop = false;
        isStartTouch = true;

        return super.onTouchEvent(event);
    }
}
