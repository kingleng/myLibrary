package com.example.leng.myapplication2.ui.myView;

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

import java.io.IOException;
import java.io.InputStream;

public class LargeImageView extends View{
    public BitmapRegionDecoder mDecoder;
    private Bitmap cache;
    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;


    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
    }

    Handler mH = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            requestLayout();
            invalidate();
        }
    };

    public void setInputStream(InputStream is) {

        try {

            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;

            mDecoder = BitmapRegionDecoder.newInstance(is, false);

            mH.sendEmptyMessage(0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }

    public void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public void onMove(float distanceX, float distanceY) {
                int moveX = (int) distanceX;
                int moveY = (int) distanceY;

//                if (mImageWidth > getWidth()) {
//                    mRect.offset(-moveX, 0);
//                    checkWidth();
//                    invalidate();
//                }
//                if (mImageHeight > getHeight()) {
//                    mRect.offset(0, -moveY);
//                    checkHeight();
//                    invalidate();
//                }

                matrix.setTranslate(moveX,moveY);
                invalidate();
            }

            @Override
            public void onFinish() {

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
        }

        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }


    private void checkHeight() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }


    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;

    }

    Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDecoder != null) {
//            if (cache != null) {
//                options.inBitmap = cache;
//            }

            long tt = System.currentTimeMillis();

            if(cache == null){
                cache = mDecoder.decodeRegion(mRect, options);
            }


//            canvas.drawBitmap(cache, 0, 0, null);
            long tt2 = System.currentTimeMillis();
            Log.e("ondraw", (tt2 - tt) + "");

            canvas.drawBitmap(cache,matrix,null);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
//        mGestureDetector.onTouchEvent(event);
        return true;
    }


    static class MoveGestureDetector {
        Context context;
        SimpleMoveGestureDetector simpleMoveGestureDetector;

        public MoveGestureDetector(Context context, SimpleMoveGestureDetector simpleMoveGestureDetector) {
            this.context = context;
            this.simpleMoveGestureDetector = simpleMoveGestureDetector;
        }

        float lastX;
        float lastY;

        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    lastY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    float currentX = event.getX();
                    float currentY = event.getY();

                    simpleMoveGestureDetector.onMove(currentX - lastX, currentY - lastY);
                    lastX = currentX;
                    lastY = currentY;
                    break;
                case MotionEvent.ACTION_UP:
                    simpleMoveGestureDetector.onFinish();
                    break;
            }
        }

        public interface SimpleMoveGestureDetector {
            void onMove(float distanceX, float distanceY);
            void onFinish();
        }
    }


}