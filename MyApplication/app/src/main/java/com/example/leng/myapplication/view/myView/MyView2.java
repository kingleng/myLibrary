package com.example.leng.myapplication.view.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by leng on 2016/10/11.
 */

public class MyView2 extends View {

    Paint mPaint1;
    Paint mPaint2;
    Paint mPaint3;
    public float progress;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public MyView2(Context context) {
        super(context);
        init();
    }

    public MyView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint1 = new Paint();
        mPaint1.setColor(Color.parseColor("#818181"));
        mPaint1.setAntiAlias(true);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeWidth(10);

        mPaint2 = new Paint();
        mPaint2.setStrokeWidth(0);
        mPaint2.setColor(Color.parseColor("#232a24"));
        mPaint2.setTextSize(40);
        mPaint2.setTypeface(Typeface.DEFAULT_BOLD); //设置字体

        mPaint3 = new Paint();
        mPaint3.setAntiAlias(true);
        mPaint3.setStrokeWidth(10); //设置圆环的宽度
        mPaint3.setColor(Color.parseColor("#27F518"));  //设置进度的颜色
        mPaint3.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/4,mPaint1);

        String text = (int)(progress*100)+"%";
        float textWidth = mPaint2.measureText(text);
        canvas.drawText(text , getWidth()/2 - textWidth / 2, getHeight()/2 + 20, mPaint2);


        RectF oval = new RectF(getWidth()/4, getHeight()/4, getWidth()/4*3, getHeight()/4*3);  //用于定义的圆弧的形状和大小的界限
        canvas.drawArc(oval, -90, 360 * progress , false, mPaint3);  //根据进度画圆弧


    }
}
