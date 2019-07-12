package com.example.leng.myapplication2.ui.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 17092234 on 2018/8/8.
 */

public class SnArMusicSeekBar extends View {

    private float mWidth;
    private float mHeight;

    private Paint mPaint;
    private Paint mPaint2;

    private int myValue = -1;
    private int myEnd = 15;
    private int mTotal = 100;

    private OnSeekBarChangeListener listener;

    public int getMyValue() {
        return myValue;
    }

    public void setMyValue(int myValue) {
        this.myValue = myValue;
        invalidate();
    }

    public int getMyEnd() {
        return myEnd;
    }

    public void setMyEnd(int myEnd) {
        this.myEnd = myEnd;
    }

    public int getmTotal() {
        return mTotal;
    }

    public void setmTotal(int mTotal) {
        this.mTotal = mTotal;
    }

    public void setListener(OnSeekBarChangeListener listener) {
        this.listener = listener;
    }

    public SnArMusicSeekBar(Context context) {
        super(context);
        init();
    }

    public SnArMusicSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnArMusicSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.parseColor("#80FFAAFF"));

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
//        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setStrokeWidth(8);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initView();
    }

    void initView(){
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int layerID = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint2, Canvas.ALL_SAVE_FLAG);

        for(int i = 0;i<40;i++){
            if(mWidth/40f*i > myValue*1f/mTotal*mWidth){
                mPaint2.setColor(Color.parseColor("#80ffffff"));
            }else{
//                mPaint2.setColor(Color.parseColor("#ffffff"));
                mPaint2.setColor(Color.parseColor("#80ffffff"));
//                mPaint2.setColor(Color.argb(255,255,255,255));
//                if(myValue == 0){
//                    mPaint2.setColor(Color.parseColor("#80ffffff"));
//                }
            }

            float currentHeight = 0;
            int currentNum = i%8;
            switch (currentNum){
                case 0:
                case 7:
                    currentHeight = mHeight*0.3f;
                    break;
                case 1:
                case 6:
                    currentHeight = mHeight*0.44f;
                    break;
                case 2:
                case 5:
                    currentHeight = mHeight*0.61f;
                    break;
                case 3:
                case 4:
                    currentHeight = mHeight*0.77f;
                    break;
            }

            RectF r2 = new RectF();
            r2.left = mWidth/40*i+mWidth/40/2;
            r2.right = mWidth/40*i+mWidth/40/2+8;
            r2.top = mHeight/2f-currentHeight/2f ;
            r2.bottom = mHeight/2f+currentHeight/2f;

            canvas.drawRoundRect(r2, 10, 10, mPaint2);
        }

        if(myValue  == -1){
            return;
        }
        mPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mPaint2.setColor(Color.parseColor("#fde801"));

        RectF r2 = new RectF();
        r2.left = myValue*1f/mTotal*mWidth;
        r2.right = (myValue+myEnd)*1f/mTotal*mWidth;
        r2.top = 0 ;
        r2.bottom = mHeight;
        canvas.drawRoundRect(r2, 10, 10, mPaint2);

        mPaint2.setXfermode(null);

        canvas.restoreToCount(layerID);
    }

    float touchX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(myValue  == -1){
            return true;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                myValue = (int)(touchX/mWidth*mTotal);
                if(myValue+myEnd>mTotal){
                    myValue = mTotal - myEnd;
                }

                if(myValue<0){
                    myValue = 0;
                }

                if(listener!=null){
                    listener.onSeekBarChange(myValue);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    public interface OnSeekBarChangeListener{
        void onSeekBarChange(int value);
        void onSeekBarEnd();
    }
}