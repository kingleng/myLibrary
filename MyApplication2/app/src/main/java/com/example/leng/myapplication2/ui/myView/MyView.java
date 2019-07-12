package com.example.leng.myapplication2.ui.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leng on 2016/10/10.
 */

public class MyView extends View {

    private Matrix mShaderMatrix;

    private Paint mViewPaint;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }

    private float myValue;


    public float getMyValue() {
        return myValue;
    }

    public void setMyValue(float myValue) {
        this.myValue = myValue;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    private double mDefaultAngularFrequency;
    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;

    private BitmapShader mWaveShader;

    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / 1.0f / getWidth();
        mDefaultAmplitude = getHeight() * 0.05f;
        mDefaultWaterLevel = getHeight() * 0.5f;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(Color.parseColor("#28FFFFFF"));
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(Color.parseColor("#3CFFFFFF"));
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (mViewPaint.getShader() == null) {
            mViewPaint.setShader(mWaveShader);
        }

        mShaderMatrix.setScale(
                1.0f / 1.0f,
                0.05f / 0.05f,
                0,
                mDefaultWaterLevel);

        mShaderMatrix.postTranslate(
                myValue * getWidth(),
                (0.0f) * getHeight());

        // assign matrix to invalidate the shader
        mWaveShader.setLocalMatrix(mShaderMatrix);

        float radius = getWidth() / 2f;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);

    }


}
