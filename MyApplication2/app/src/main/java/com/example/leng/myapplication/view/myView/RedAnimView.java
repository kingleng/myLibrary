package com.example.leng.myapplication.view.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.leng.myapplication.R;

/**
 * Created by 17092234 on 2018/4/24.
 */

public class RedAnimView extends View implements Runnable {

    public Context context;

    private boolean isRepert = true;

    Thread mThread;

    private Paint mPaint;

    int zxcv = 1;

    int width = 0;
    int height = 0;


    public RedAnimView(Context context) {
        this(context,null);
    }

    public RedAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RedAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        init();
        startRoll();
    }

    private void init() {
        mPaint = new TextPaint();
    }

    private void startRoll() {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }

        mThread = new Thread(this);
        mThread.start();
    }

    Matrix matrix = new Matrix();

    Bitmap bitmap=null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(bitmap==null){
            bitmap = changeBitmapSize(BitmapFactory.decodeResource(getResources(), R.drawable.defult_bg));
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            matrix.postTranslate(getWidth()/2f-width/2f,getHeight()/2f-height/2);
        }
//        matrix.postRotate(0f,bitmap.getWidth()/2,bitmap.getHeight()/2);

        canvas.drawBitmap(bitmap,matrix, mPaint);
    }

    private Bitmap changeBitmapSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置想要的大小
        int newWidth = getScreenWidth(context)/5*4;

        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleWidth);

        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();
        return bitmap;
    }

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();

        return width;

    }

    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

//        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return height;

    }


    @Override
    public void run() {
        while (isRepert) {
            try {
                Thread.sleep(5);
                if(zxcv<700){
                    zxcv++;
                    if(zxcv>600){
                        if(bitmap!=null){
                            matrix.postScale(0.990f,0.990f,getScreenWidth(context),getScreenHeight(context)/5*2);
                        }
                    }

                }else{
                    if (mThread != null) {
                        mThread.interrupt();
                        mThread = null;
                        return;
                    }
                }

                postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
