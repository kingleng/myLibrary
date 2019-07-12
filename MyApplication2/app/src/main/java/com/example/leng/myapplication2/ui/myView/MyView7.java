package com.example.leng.myapplication2.ui.myView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by leng on 2016/10/31.
 */

public class MyView7 extends View {

    private float value;
    private int animTime = 10;
    private Paint mPaint;

    private ObjectAnimator myviewAnim;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    public void setAnimTime(int animTime) {
        this.animTime = animTime;
    }

    public MyView7(Context context) {
        super(context);
        init();
    }

    public MyView7(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView7(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#341923"));
        mPaint.setTextSize(55);
    }

    public void startAnim(){
        myviewAnim = ObjectAnimator.ofFloat(
                this, "value", 0f, 1f);
        myviewAnim.setDuration(animTime * 1000);
        myviewAnim.setInterpolator(new LinearInterpolator());
        myviewAnim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAlpha((int)(255-255/(float)((int)(value * 10 * animTime) % 10)));
        int num = 10 - (int)(value * animTime);
        String text = num+"";
        float textWidth = mPaint.measureText(text);
        canvas.drawText(text,getWidth()/2-textWidth/2,getHeight()/2,mPaint);
    }
}
