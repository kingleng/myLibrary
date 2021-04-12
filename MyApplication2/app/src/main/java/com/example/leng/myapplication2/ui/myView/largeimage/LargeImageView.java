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
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
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
    public BitmapRegionDecoder mDecoderCopy;

//    List<ItemParams> cache = new ArrayList<>();

    private LruCache<String, ItemParams> mMemoryCache;

    public void addBitmapToMemoryCache(String key, ItemParams bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    public ItemParams getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 缓存图片的宽高比
     */
    float ratio = 0.3f;
    int preCacheNum = 3;

    Matrix matrix = new Matrix();

    Matrix scaleMatrix = new Matrix();
    float totalTranslateX = 0;
    float totalTranslateY = 0;
    float itemHeight = 0;

    boolean isInit = false;

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
                    isInit = true;
                    requestLayout();
                    invalidate();
                    break;
                case 1:
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

        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, ItemParams>(cacheSize) {
            @Override
            protected int sizeOf(String key, ItemParams bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.mBitmap.getByteCount() / 1024;
            }
        };
    }

    private void checkWidth() {

        float[] values = new float[9];
        scaleMatrix.getValues(values);

        if (totalTranslateX < -(mImageWidth/s*values[0]/ss-width)) {
            totalTranslateX = -(mImageWidth/s*values[0]/ss-width);
        }

        if (totalTranslateX > 0) {
            totalTranslateX = 0;
        }
    }


    private void checkHeight() {
        float[] values = new float[9];
        scaleMatrix.getValues(values);

        if (totalTranslateY < -(mImageHeight/s*values[0]/ss-height)) {
            totalTranslateY = -(mImageHeight/s*values[0]/ss-height);
        }

        if (totalTranslateY > 0) {
            totalTranslateY = 0;
        }
    }

    private void checkScale() {

        float[] values = new float[9];
        scaleMatrix.getValues(values);

        if(values[0]<ss){
            scaleMatrix.setScale(ss,ss);
        }

        if(values[0]>(ss*s)){
            scaleMatrix.setScale(ss*s,ss*s);
        }


//        if (totalTranslateY < -(mImageHeight/s-height)) {
//            totalTranslateY = -(mImageHeight/s-height);
//        }
//
//        if (totalTranslateY > 0) {
//            totalTranslateY = 0;
//        }
    }


    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public void onMove(float distanceX, float distanceY) {

//                float[] matrix = new float[9];
//                scaleMatrix.getValues(matrix);

                int moveX = (int) (distanceX);
                int moveY = (int) (distanceY);

                if (mImageWidth > getWidth()) {
                    totalTranslateX += moveX;
                    checkWidth();
                    invalidate();
                }
                if (mImageHeight > getHeight()) {
                    totalTranslateY += moveY;
                    checkHeight();
                    invalidate();
                }

                getBitmap();


            }

            @Override
            public void onScale(float scale, float centerX, float centerY) {
                scaleMatrix.postScale(scale,scale);
                
                totalTranslateY = totalTranslateY*scale - (centerY*scale-centerY);
                totalTranslateX = totalTranslateX*scale - (centerX*scale-centerX);
                checkScale();
                checkWidth();
                checkHeight();
                invalidate();
            }

            @Override
            public void onFinish() {
//                getBitmapcache();

            }
        });
    }

    Map<Integer,String> getBitmap = new HashMap();
    /******************       异步获取图片缓存  ********************/
    private void getBitmapcache(int position){

        if(getBitmap.get(position) !=null){
            return;
        }
        getBitmap.put(position,"1");

        Rect rect = new Rect();
        rect.left = 0;
        rect.top = (int)(mImageWidth*ratio*position);
        rect.right = mImageWidth;
        rect.bottom = (int)(mImageWidth*ratio*(position+1)+1);

        pool.submit(new Runnable() {
            @Override
            public void run() {
                long tt1 = System.currentTimeMillis();
                ItemParams params = new ItemParams();
                params.mBitmap = mDecoder.decodeRegion(rect, options);
                params.setRect(rect);
                params.position = position;

                addBitmapToMemoryCache(position+"",params);
                synchronized (getBitmap){
                    getBitmap.remove(position);
                }

                long tt2 = System.currentTimeMillis();
                Log.e("time:","position = "+position +"::"+(tt2-tt1)+"");

                mH.sendEmptyMessage(1);
            }
        });
    }

    int width=0;
    int height = 0;
    float s;
    float ss = 1f;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(!isInit){
            return;
        }

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

        ss = ss*pow/s;

        scaleMatrix.setScale(ss,ss);

        itemHeight = imageWidth/s;

        getBitmap();

    }

    private void getDecodeCopy(float sacle){
//        mDecoderCopy = mDecoder.
    }


    public void getBitmap(){
        if(mDetector==null){
            return;
        }
        List<Integer> positons = showBitmap();
        for(int i=0;i<positons.size();i++){
            if(getBitmapFromMemCache(positons.get(i)+"") == null){
                getBitmapcache(positons.get(i));
            }
        }
    }

    public List<Integer> showBitmap(){

        float[] values = new float[9];        
        scaleMatrix.getValues(values);        
        int firstPosition = (int)(-totalTranslateY/(width*values[0]/ss*ratio));
        int endPosition = (int)((-totalTranslateY+height)/(width*values[0]/ss*ratio));
        if(((-(totalTranslateY+height))%(width*values[0]/ss*ratio))>0){
            endPosition++;
        }

        for(int i=1;i<=preCacheNum;i++){
            if((totalTranslateY+width*values[0]/ss*ratio*i)<0){
                firstPosition--;
            }

            if(Math.abs(totalTranslateY-width*values[0]/ss*ratio*i)*s<mImageHeight){
                endPosition++;
            }
        }

        List<Integer> positions = new ArrayList<>();
        for(int i = firstPosition;i<=endPosition;i++){
            positions.add(i);
        }

        return positions;
    }


    float[] values = new float[9];

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDecoder != null) {

            List<Integer> positons = showBitmap();
            for(int i=0;i<positons.size();i++){

                ItemParams param = getBitmapFromMemCache(positons.get(i)+"");
                if(param == null){
                    continue;
                }
                Log.e("onDraw","positons = "+positons.get(i));
                matrix.reset();
                matrix.postConcat(scaleMatrix);
                scaleMatrix.getValues(values);
                matrix.postTranslate(totalTranslateX,param.top/s*values[0]/ss+totalTranslateY);
                canvas.drawBitmap(param.mBitmap,matrix,null);

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