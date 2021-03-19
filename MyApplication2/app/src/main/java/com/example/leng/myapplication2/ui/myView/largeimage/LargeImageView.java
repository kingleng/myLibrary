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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LargeImageView extends View{
    public BitmapRegionDecoder mDecoder;

    List<ItemParams> cache = new ArrayList<>();

    Map<Integer,Integer> isDecode = new HashMap<>();

    Matrix matrix = new Matrix();

    Matrix scaleMatrix = new Matrix();
    float totalTranslateY = 0;
    float itemHeight = 0;

    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;

    private MoveGestureDetector mDetector;


    ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
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

//    private void checkWidth() {
//
//
//        Rect rect = mRect;
//        int imageWidth = mImageWidth;
//        int imageHeight = mImageHeight;
//
//        if (rect.right > imageWidth) {
//            rect.right = imageWidth;
//            rect.left = imageWidth - getWidth();
//
////            matrix.postTranslate(imageWidth-rect.right,0);
//        }
//
//        if (rect.left < 0) {
//            rect.left = 0;
//            rect.right = getWidth();
////            matrix.postTranslate(rect.left,0);
//        }
//    }


    private void checkHeight() {

        if (totalTranslateY < -(mImageHeight/s-height)) {
            totalTranslateY = -(mImageHeight/s-height);
        }

        if (totalTranslateY > 0) {
            totalTranslateY = 0;
        }
    }


    public LargeImageView(Context context, AttributeSet attrs) {
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
                    totalTranslateY += moveY;
                    checkHeight();
                    invalidate();
                }

            }

            @Override
            public void onScale(float scaleX, float scaleY) {
//                scaleMatrix.postScale(scaleX,scaleY);
//                invalidate();
            }

            @Override
            public void onFinish() {
//                getBitmapcache();
                getBitmap();
            }
        });
    }

    /******************       异步获取图片缓存  ********************/
    private void getBitmapcache(int position){

//        if(isDecode.containsKey(position)){
//            return;
//        }
        isDecode.put(position,0);

        Rect rect = new Rect();
        rect.left = 0;
        rect.top = mImageWidth*position;
        rect.right = mImageWidth;
        rect.bottom = mImageWidth*(position+1);

        pool.submit(new Runnable() {
            @Override
            public void run() {
                long tt1 = System.currentTimeMillis();
                ItemParams params = new ItemParams();
                params.mBitmap = mDecoder.decodeRegion(rect, options);
                params.setRect(rect);
                params.position = position;
                if(cache.size() == 0){
                    cache.add(params);
                }else{
                    for(int i=0;i<cache.size();i++){
                        if(position<cache.get(i).position){
                            cache.add(i,params);
                        }
                    }

                    if(position>=cache.get(cache.size()-1).position){
                        cache.add(params);
                    }
                }

                isDecode.remove(position);

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



        /******************       获取图片缩放比例 s  ********************/
        s = imageWidth*1f/width;

        /******************       获取图片取样率 pow  ********************/

        int pow = 1;
        for(;;){
            if(pow<s){
                pow = pow*2;
            }else{
                break;
            }
        }
        options.inSampleSize = pow;

        /******************       获取图片最终缩放比例  ********************/
        float ss = 1f;
        ss = ss*pow/s;

        scaleMatrix.setScale(ss,ss);

        itemHeight = imageWidth/s;

        getBitmap();
    }


    public void getBitmap(){
        if(mDetector==null){
            return;
        }

        List<Integer> positons = showBitmap();

        for(int i=0;i<positons.size();i++){
            boolean has = false;
            for(ItemParams params :cache){
                if(params.position == positons.get(i)){
                    has = true;
                }
            }
            if(!has){
                getBitmapcache(positons.get(i));
            }

        }

//        for(ItemParams params :cache){
//            boolean has = false;
//            if(params.position < positons.get(0)-1){
//                cache.remove(params);
//                continue;
//            }
//
//            if(params.position > positons.get(positons.size()-1)+1){
//                cache.remove(params);
//                continue;
//            }
//
//        }




    }

    public List<Integer> showBitmap(){
        int firstPosition = (int)(-totalTranslateY/width);
        int endPosition = (int)((-totalTranslateY+height)/width);
        if(((-(totalTranslateY+height))%width)>0){
            endPosition++;
        }

        List<Integer> positions = new ArrayList<>();
        for(int i = firstPosition;i<=endPosition;i++){
            positions.add(i);
        }

        return positions;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDecoder != null) {

            for(int i=0;i<cache.size();i++){
//                if(i!=0){
//                    break;
//                }
                matrix.reset();
                matrix.postConcat(scaleMatrix);
                matrix.postTranslate(0,cache.get(i).top/s+totalTranslateY);
                canvas.drawBitmap(cache.get(i).mBitmap,matrix,null);
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    public static class ItemParams{
        //缓存图片
        private Bitmap mBitmap;
        //布局图片参数（位置）
        private int top;
        private int bottom;
        private int left;
        private int right;

        private int position;

        private boolean isStartCache = false;

        private float getHeight(){
            return bottom - top;
        }

        private Rect getRect(){
            return new Rect(left,top,right,bottom);
        }

        public void setRect(Rect rect){
            top = rect.top;
            bottom = rect.bottom;
            left = rect.left;
            right = rect.right;
        }


    }

}