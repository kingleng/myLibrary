package com.example.leng.myapplication.view.myView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.leng.myapplication.R;

/**
 * Created by leng on 2016/11/16.
 */

public class yibiaopan extends View {

    private float centerWidth;
    private float centerHeight;
    private float radius;

    private Paint mPaint;
    private Paint mPaint2;
    private Paint mTextPaint;

    //比表盘出现属性动画
    private float showTime = 1.0f;
    private int myValue = 80; // 0-100
    private float valueAnim;
    private float starAnim;

    public float getShowTime() {
        return showTime;
    }

    public void setShowTime(float showTime) {
        this.showTime = showTime;
        invalidate();
    }

    public int getMyValue() {
        return myValue;
    }

    public void setMyValue(int myValue) {
        this.myValue = myValue;
        startSetValueAnim();
    }

    public float getValueAnim() {
        return valueAnim;
    }

    public void setValueAnim(float valueAnim) {
        this.valueAnim = valueAnim;
        invalidate();
    }

    public float getStarAnim() {
        return starAnim;
    }

    public void setStarAnim(float starAnim) {
        if(starAnim<0.7){
            this.starAnim = starAnim;
            invalidate();
        }

    }

    public yibiaopan(Context context) {
        super(context);
        init();
    }

    public yibiaopan(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.yibiaopan);
        init();
    }

    public yibiaopan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.yibiaopan);
        init();
    }

    void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#585857"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(8);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#ffffff"));

        MyAnimator();

        isStart = false;
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

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAlpha((int)(255*showTime));

        canvas.rotate(240,centerWidth,centerHeight);
        for(int i = 0;i<41;i++){
            if(i < (int)(myValue*valueAnim/100f*40)){
                mPaint2.setColor(Color.argb(255,(int)4*i+20,(int)i+30,(int)3*i+100));
                Path path2 = new Path();
                path2.moveTo(centerWidth,centerHeight-radius*showTime/5*4);
                path2.lineTo(centerWidth,centerHeight-radius*showTime);
                path2.close();
                canvas.drawPath(path2,mPaint2);
                canvas.rotate(6,centerWidth,centerHeight);
            }else{
                Path path = new Path();
                if(showTime>0.9f){
                    path.moveTo(centerWidth,centerHeight-radius*(showTime*(11/8f-(showTime-0.9f)*10*3/8)*0.5f + 0.5f)/5*4);
                    path.lineTo(centerWidth,centerHeight-radius*(showTime*(11/8f-(showTime-0.9f)*10*3/8)*0.5f + 0.5f ));
                }else{
                    path.moveTo(centerWidth,centerHeight-radius*(showTime*11/8*0.5f + 0.5f)/5*4);
                    path.lineTo(centerWidth,centerHeight-radius*(showTime*11/8*0.5f + 0.5f));
                }

                path.close();
                canvas.drawPath(path,mPaint);
                canvas.rotate(6,centerWidth,centerHeight);
            }
        }

        canvas.rotate(234,centerWidth,centerHeight);


        mTextPaint.setTextSize(150*showTime);
        mTextPaint.setAlpha((int)(255*showTime));
        String text = Integer.toString((int)(myValue*valueAnim));
        float textWidth = mTextPaint.measureText(text);
        canvas.drawText(text,centerWidth-textWidth/2,centerHeight+textWidth/4,mTextPaint);

//        if(isStart){
//            if((360*starAnim)<240){
//                RectF rect = new RectF(centerWidth-radius,centerHeight-radius,centerWidth+radius,centerHeight+radius);
//                canvas.drawArc(rect,150+360*starAnim,15,false,mPaint);
//            }
//
//        }

    }

    ObjectAnimator animator;
    ObjectAnimator setValueAnimator;
    ObjectAnimator starAnimator;
    Boolean isStart = false;
    private void MyAnimator(){
        animator = ObjectAnimator.ofFloat(this,"showTime",0f,1f);
        animator.setDuration(2*1000);
        animator.setInterpolator(new LinearInterpolator());

        setValueAnimator = ObjectAnimator.ofFloat(this,"valueAnim",0f,1f);
        setValueAnimator.setDuration(1*1000);
        setValueAnimator.setInterpolator(new LinearInterpolator());

//        starAnimator = ObjectAnimator.ofFloat(this,"starAnim",0f,1f);
//        starAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        starAnimator.setDuration(2*1000);
//        starAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startAnim(){
        if(animator!=null){
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startSetValueAnim();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    public void startSetValueAnim(){
        isStart = true;
        if(setValueAnimator!=null){
            setValueAnimator.start();
        }
//        if(starAnimator!=null){
//            starAnimator.start();
//        }
    }


}
