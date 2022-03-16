package com.example.leng.myapplication.view.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leng on 2016/10/17.
 */

public class MyView4 extends View {

    private Paint mPaint;
    private Matrix mShaderMatrix;

    private float myValue;

    public float getMyValue() {
        return myValue;
    }

    public void setMyValue(float myValue) {
        this.myValue = myValue;
        invalidate();
    }

    public MyView4(Context context) {
        super(context);
        init();
    }

    public MyView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        mPaint = new Paint();
//        mPaint.setColor(Color.parseColor("#ffffff"));
//        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mShaderMatrix = new Matrix();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawBackground();
    }

    private BitmapShader mWaveShader;

    private void drawBackground(){
        double mDefaultAngularFrequency = 2.0f * Math.PI / 1.0f / getWidth();
        float mDefaultAmplitude = 0.25f * getWidth();


        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint1 = new Paint();
        paint1.setStyle(Paint.Style.FILL);
        paint1.setAntiAlias(true);
        paint1.setColor(Color.parseColor("#ffffff"));

        RectF oval = new RectF(0,0,getWidth(),getHeight());
        canvas.drawArc(oval,90,180,true,paint1);
        paint1.setColor(Color.parseColor("#000000"));
        canvas.drawArc(oval,-90,180,true,paint1);

        paint1.setColor(Color.parseColor("#ffffff"));
        RectF oval2 = new RectF(getWidth()/4.0f,0,getWidth()/ 4.0f * 3.0f,getHeight()/2.0f);
        canvas.drawArc(oval2,-91,182,true,paint1);
        paint1.setColor(Color.parseColor("#000000"));
        RectF oval3 = new RectF(getWidth()/4.0f,getHeight()/2.0f,getWidth()/ 4.0f * 3.0f,getHeight());
        canvas.drawArc(oval3,89,182,true,paint1);

        paint1.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth()/2.0f,getHeight() / 4.0f,getWidth() / 8.0f,paint1);

        paint1.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(getWidth()/2.0f,getHeight() / 4.0f * 3.0f,getWidth() / 8.0f,paint1);


        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPaint.getShader() == null){
            mPaint.setShader(mWaveShader);
        }

        mShaderMatrix.setScale(
                1.0f / 1.0f,
                0.05f / 0.05f,
                0,
                0);

//        mShaderMatrix.postTranslate(
//                myValue * getWidth(),
//                (0.0f) * getHeight());
        mShaderMatrix.postRotate(-myValue*360,getWidth() / 2f, getHeight() / 2f);

        mWaveShader.setLocalMatrix(mShaderMatrix);

        float radius = getWidth() / 2f;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mPaint);

    }
}
