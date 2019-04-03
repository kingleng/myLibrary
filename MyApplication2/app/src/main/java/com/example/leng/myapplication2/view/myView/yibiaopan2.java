package com.example.leng.myapplication2.view.myView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by leng on 2016/11/16.
 */

public class yibiaopan2 extends View {

    private float centerWidth;
    private float centerHeight;
    private float radius;

    private Paint mPaint;

    //比表盘外层环绕属性动画
    private float starAnim;

    public float getStarAnim() {
        return starAnim;
    }

    public void setStarAnim(float starAnim) {
        if(starAnim<0.7){
            this.starAnim = starAnim;
            invalidate();
        }

    }

    public yibiaopan2(Context context) {
        super(context);
        init();
    }

    public yibiaopan2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public yibiaopan2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#585857"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        MyAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initView();
    }

    void initView(){
        centerWidth = getWidth()/2.0f;
        centerHeight = getHeight()/2.0f;
        radius = centerWidth>centerHeight?centerHeight:centerWidth;
        radius = radius/6*5;

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if((360*starAnim)<240){
            RectF rect = new RectF(centerWidth-radius,centerHeight-radius,centerWidth+radius,centerHeight+radius);
            canvas.drawArc(rect,150+360*starAnim,15,false,mPaint);
        }

    }

    ObjectAnimator starAnimator;
    private void MyAnimator(){

        starAnimator = ObjectAnimator.ofFloat(this,"starAnim",0f,1f);
        starAnimator.setRepeatCount(ValueAnimator.INFINITE);
        starAnimator.setDuration(2*1000);
        starAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startAnim(){
        if(starAnimator!=null){
            starAnimator.start();
        }
    }


}
