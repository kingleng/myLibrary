package com.example.leng.myapplication2.ui.myView.largeimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LargeImage2View extends View{
    public BitmapRegionDecoder mDecoder;

    private Bitmap cache;
    private Bitmap nextCache;
    private Bitmap preCache;

    private volatile Rect mRect = new Rect();
    private volatile Rect mNextRect = new Rect();
    private volatile Rect mPreRect = new Rect();

    Matrix matrix = new Matrix();
    Matrix nextMatrix = new Matrix();
    Matrix preMatrix = new Matrix();
    Matrix scaleMatrix = new Matrix();
    Matrix translateMatrix = new Matrix();

    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;

    private MoveGestureDetector mDetector;


    ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 3,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;

    }

    Handler mH = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    requestLayout();
                    invalidate();
                    break;
                case 1:
                    isReset = true;
                    invalidate();
                    break;
            }

        }
    };

    public void setInputStream(final InputStream is) {

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long tt1 = System.currentTimeMillis();

                    mDecoder = BitmapRegionDecoder.newInstance(is, false);
                    mImageWidth = mDecoder.getWidth();
                    mImageHeight = mDecoder.getHeight();

                    long tt2 = System.currentTimeMillis();
                    Log.e("setInputStream:",tt2-tt1+"");


                    mH.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        });
    }

    private void checkWidth() {


        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();

//            matrix.postTranslate(imageWidth-rect.right,0);
        }

        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
//            matrix.postTranslate(rect.left,0);
        }
    }


    private void checkHeight() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        int rectH = rect.bottom - rect.top;

        if (rect.bottom > imageHeight) {

            int dy = rect.bottom - imageHeight;

            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();

            mRect.top = imageHeight-rectH;
            mRect.bottom = imageHeight;

            translateMatrix.postTranslate(0,dy/s);
        }

        if (rect.top < 0) {
            int dy = rect.top;
            rect.top = 0;
            rect.bottom = rectH;
//            matrix.postTranslate(0,rect.top);
            translateMatrix.postTranslate(0,dy/s);
        }
    }


    public LargeImage2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public void onMove(float distanceX, float distanceY) {

                float[] matrix = new float[9];
                scaleMatrix.getValues(matrix);

                int moveX = (int) (distanceX);
                int moveY = (int) (distanceY);

//                if (mImageWidth > getWidth()) {
//                    mRect.offset(-moveX, 0);
//                    checkWidth();
//                    invalidate();
//                }
                if (mImageHeight > getHeight()) {
                    translateMatrix.postTranslate(0,moveY);
                    mRect.offset(0, -(int)(moveY*s));
                    checkHeight();
                    invalidate();
                }

            }

            @Override
            public void onScale(float scale, float centerX, float centerY) {

            }

            @Override
            public void onFinish() {
                getBitmapcache();
            }
        });
    }

    /******************       异步获取图片缓存  ********************/
    private void getBitmapcache(){

        mNextRect.left = 0;
        mNextRect.right = mImageWidth;
        int ntop = (mRect.bottom+1);
        mNextRect.top = ntop>mImageHeight?mImageHeight:ntop;
        int nbottom = (mRect.bottom-mRect.top)+mRect.bottom+1;
        mNextRect.bottom = nbottom>mImageHeight?mImageHeight:nbottom;

        mPreRect.left = 0;
        mPreRect.right = mImageWidth;
        int ptop = mRect.top-1-(mRect.bottom-mRect.top);
        mPreRect.top = (ptop<0)?0:ptop;
        int pbottom = mRect.top-1;
        mPreRect.bottom = (pbottom<0)?0:pbottom;

        pool.submit(new Runnable() {
            @Override
            public void run() {
                long tt1 = System.currentTimeMillis();
                cache = mDecoder.decodeRegion(mRect, options);

                if(mNextRect.bottom-mNextRect.top > 0){
                    nextCache = mDecoder.decodeRegion(mNextRect, options);
                }
                if(mPreRect.bottom-mPreRect.top > 0){
                    preCache = mDecoder.decodeRegion(mPreRect, options);
                }

                long tt2 = System.currentTimeMillis();
                Log.e("time:",tt2-tt1+"");

                mH.sendEmptyMessage(1);
            }
        });
    }

    int width=0;
    int height = 0;
    boolean isReset = false;
    float s;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        mRect.left = 0;
        mRect.top = 0;
        mRect.right = imageWidth;
        mRect.bottom = (int)(imageWidth*1f/width*height);

        /******************       获取图片取样宽高  ********************/

        s = imageWidth*1f/width;

        /******************       获取图片缩放比例 s  ********************/

        int pow = 1;
        for(;;){
            if(pow<s){
                pow = pow*2;
            }else{
                break;
            }
        }
        options.inSampleSize = pow;

        /******************       获取图片取样率 pow  ********************/

        float ss = 1f;
        ss = ss*pow/s;

        /******************       获取图片最终缩放比例  ********************/

        scaleMatrix.setScale(ss,ss);

        getBitmapcache();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDecoder != null) {

            if(cache == null){
                return;
            }

            if(isReset){
                translateMatrix.reset();
                isReset = false;
            }

            matrix.reset();
            matrix.postConcat(scaleMatrix);
            matrix.postConcat(translateMatrix);

            canvas.drawBitmap(cache,matrix,null);


            if(nextCache!=null){
                nextMatrix.reset();
                nextMatrix.postConcat(scaleMatrix);
                nextMatrix.postConcat(translateMatrix);

                nextMatrix.postTranslate(0,height-1);

                float[] values = new float[9];
                nextMatrix.getValues(values);

                canvas.drawBitmap(nextCache,nextMatrix,null);
            }


            if(preCache!=null){
                preMatrix.reset();
                preMatrix.postConcat(scaleMatrix);
                preMatrix.postConcat(translateMatrix);
                preMatrix.postTranslate(0,-height+1);
                canvas.drawBitmap(preCache,preMatrix,null);
            }


        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

}