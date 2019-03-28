package com.example.leng.myapplication.view.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by leng on 2016/10/20.
 */

public class MyView6 extends View {

    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaint3;
    private Paint mPaint4;
    private Paint mPaintValue;

    private float mWith;
    private float centerWith;
    private float centerHeight;

    private int value1 = 25;
    private int value2 = 20;
    private int value3 = 25;
    private int value4 = 20;
    private int value5 = 25;
    private int value6 = 20;

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
        invalidate();
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
        invalidate();
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
        invalidate();
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
        invalidate();
    }

    public int getValue5() {
        return value5;
    }

    public void setValue5(int value5) {
        this.value5 = value5;
        invalidate();
    }

    public int getValue6() {
        return value6;
    }

    public void setValue6(int value6) {
        this.value6 = value6;
        invalidate();
    }

    public MyView6(Context context) {
        super(context);
        init();
    }

    public MyView6(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(2);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initWith();
    }

    private void initWith(){
        centerWith = getWidth()/2.0f;
        centerHeight = getHeight()/2.0f;
        mWith = getWidth()/3.0f;

        Log.e("mwith","mWith:"+mWith);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPaint.setColor(Color.parseColor("#E651BE"));
        Path mPath = new Path();
        float dx1 = centerWith - mWith * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy1 = centerHeight - mWith * (float)Math.cos(Math.PI / 360.0f * 60);
        Log.e("onDraw","点1:"+dx1 +"::"+ dy1);
        mPath.moveTo(dx1,dy1);
        float dx2 = centerWith + mWith * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy2 = centerHeight - mWith * (float)Math.cos(Math.PI / 360.0f * 60);
        Log.e("onDraw","点2:"+dx2 +"::"+ dy2);
        mPath.lineTo(dx2,dy2);
        float dx3 = centerWith + mWith;
        float dy3 = centerHeight;
        Log.e("onDraw","点3:"+dx3 +"::"+ dy3);
        mPath.lineTo(dx3,dy3);
        float dx4 = centerWith + mWith * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy4 = centerHeight + mWith * (float)Math.cos(Math.PI / 360.0f*60);
        Log.e("onDraw","点4:"+dx4 +"::"+ dy4);
        mPath.lineTo(dx4,dy4);
        float dx5 = centerWith - mWith * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy5 = centerHeight + mWith * (float)Math.cos(Math.PI / 360.0f * 60);
        Log.e("onDraw","点5:"+dx5 +"::"+ dy5);
        mPath.lineTo(dx5,dy5);
        float dx6 = centerWith - mWith;
        float dy6 = centerHeight;
        Log.e("onDraw","点6:"+dx6 +"::"+ dy6);
        mPath.lineTo(dx6 ,dy6);
        mPath.close();
        canvas.drawPath(mPath,mPaint);

        mPaint.setColor(Color.parseColor("#CC31A3"));
        Path mPath2 = new Path();
        float dx21 = centerWith - mWith/4.0f*3 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy21 = centerHeight - mWith/4.0f*3 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath2.moveTo(dx21,dy21);
        float dx22 = centerWith + mWith/4.0f*3 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy22 = centerHeight - mWith/4.0f*3 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath2.lineTo(dx22,dy22);
        float dx23 = centerWith + mWith/4.0f*3;
        float dy23 = centerHeight;
        mPath2.lineTo(dx23,dy23);
        float dx24 = centerWith + mWith/4.0f*3 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy24 = centerHeight + mWith/4.0f*3 * (float)Math.cos(Math.PI / 360.0f*60);
        mPath2.lineTo(dx24,dy24);
        float dx25 = centerWith - mWith/4.0f*3 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy25 = centerHeight + mWith/4.0f*3 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath2.lineTo(dx25,dy25);
        float dx26 = centerWith - mWith/4.0f*3;
        float dy26 = centerHeight;
        mPath2.lineTo(dx26 ,dy26);
        mPath2.close();
        canvas.drawPath(mPath2,mPaint);

        mPaint.setColor(Color.parseColor("#B71B8E"));
        Path mPath3 = new Path();
        float dx31 = centerWith - mWith/2.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy31 = centerHeight - mWith/2.0f * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath3.moveTo(dx31,dy31);
        float dx32 = centerWith + mWith/2.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy32 = centerHeight - mWith/2.0f * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath3.lineTo(dx32,dy32);
        float dx33 = centerWith + mWith/2.0f;
        float dy33 = centerHeight;
        mPath3.lineTo(dx33,dy33);
        float dx34 = centerWith + mWith/2.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy34 = centerHeight + mWith/2.0f * (float)Math.cos(Math.PI / 360.0f*60);
        mPath3.lineTo(dx34,dy34);
        float dx35 = centerWith - mWith/2.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy35 = centerHeight + mWith/2.0f * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath3.lineTo(dx35,dy35);
        float dx36 = centerWith - mWith/2.0f;
        float dy36 = centerHeight;
        mPath3.lineTo(dx36 ,dy36);
        mPath3.close();
        canvas.drawPath(mPath3,mPaint);

        mPaint.setColor(Color.parseColor("#960870"));
        Path mPath4 = new Path();
        float dx41 = centerWith - mWith/4.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy41 = centerHeight - mWith/4.0f * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath4.moveTo(dx41,dy41);
        float dx42 = centerWith + mWith/4.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy42 = centerHeight - mWith/4.0f * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath4.lineTo(dx42,dy42);
        float dx43 = centerWith + mWith/4.0f;
        float dy43 = centerHeight;
        mPath4.lineTo(dx43,dy43);
        float dx44 = centerWith + mWith/4.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy44 = centerHeight + mWith/4.0f * (float)Math.cos(Math.PI / 360.0f*60);
        mPath4.lineTo(dx44,dy44);
        float dx45 = centerWith - mWith/4.0f * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy45 = centerHeight + mWith/4.0f * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath4.lineTo(dx45,dy45);
        float dx46 = centerWith - mWith/4.0f;
        float dy46 = centerHeight;
        mPath4.lineTo(dx46 ,dy46);
        mPath4.close();
        canvas.drawPath(mPath4,mPaint);

        mPaint2.setColor(Color.parseColor("#8007BC"));
        Path mPath5 = new Path();
        mPath5.moveTo(centerWith,centerHeight);
        mPath5.lineTo(dx1,dy1);
        mPath5.moveTo(centerWith,centerHeight);
        mPath5.lineTo(dx2,dy2);
        mPath5.moveTo(centerWith,centerHeight);
        mPath5.lineTo(dx3,dy3);
        mPath5.moveTo(centerWith,centerHeight);
        mPath5.lineTo(dx4,dy4);
        mPath5.moveTo(centerWith,centerHeight);
        mPath5.lineTo(dx5,dy5);
        mPath5.moveTo(centerWith,centerHeight);
        mPath5.lineTo(dx6,dy6);
        mPath5.close();
        canvas.drawPath(mPath5,mPaint2);

        mPaint2.setColor(Color.parseColor("#32E476"));
        Path mPath6 = new Path();
        float dx61 = centerWith - mWith/100.0f*value1 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy61 = centerHeight - mWith/100.0f*value1 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath6.moveTo(dx61,dy61);
        float dx62 = centerWith + mWith/100.0f*value2 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy62 = centerHeight - mWith/100.0f*value2 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath6.lineTo(dx62,dy62);
        float dx63 = centerWith + mWith/100.0f*value3;
        float dy63 = centerHeight;
        mPath6.lineTo(dx63,dy63);
        float dx64 = centerWith + mWith/100.0f*value4 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy64 = centerHeight + mWith/100.0f*value4 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath6.lineTo(dx64,dy64);
        float dx65 = centerWith - mWith/100.0f*value5 * (float)Math.sin(Math.PI / 360.0f * 60);
        float dy65 = centerHeight + mWith/100.0f*value5 * (float)Math.cos(Math.PI / 360.0f * 60);
        mPath6.lineTo(dx65,dy65);
        float dx66 = centerWith - mWith/100.0f*value6;
        float dy66 = centerHeight;
        mPath6.lineTo(dx66,dy66);
        mPath6.close();
        canvas.drawPath(mPath6,mPaint2);

    }
}
