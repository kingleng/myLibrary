package com.example.leng.myapplication2.ui.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leng on 2019/12/26.
 */
public class ImageMapView extends AppCompatImageView {


    private int widthSize;
    private int heightSize;
    private float scale = 1f;

    Paint mPaint;

    private List<ItemType> items = new ArrayList<>();

    public ImageMapView(Context context) {
        super(context);
    }

    public ImageMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItems(List<ItemType> items) {
        this.items = items;
    }


    /**
     * 图片展示大小与坐标系大小的缩放系数
     * @param scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();

    }



    private void init() {

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#55ff5500"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(touchItem == null){
            return;
        }

        if (TextUtils.equals(touchItem.shape, "circ")) {
            int[] values = touchItem.values;
            int cx = (int)(values[0] * scale);
            int cy = (int)(values[1] * scale);
            int radius = (int)(values[2] * scale);
            canvas.drawCircle(cx,cy,radius,mPaint);
        }else if (TextUtils.equals(touchItem.shape, "rect")) {
            int[] values = touchItem.values;
            int left = (int)(values[0] * scale);
            int top = (int)(values[1] * scale);
            int right = (int)(values[2] * scale);
            int bottom = (int)(values[3] * scale);
            canvas.drawRect(left,top,right,bottom,mPaint);
        } else {
            int[] values = touchItem.values;
            Path mPath = new Path();
            for(int i=0;i<values.length;i=i+2){
                if(i==0){
                    if(i+1<values.length){
                        mPath.moveTo(values[i]*scale, values[i+1]*scale);
                    }
                }else{
                    if(i+1<values.length){
                        mPath.lineTo(values[i]*scale, values[i+1]*scale);
                    }
                }
            }
            mPath.close();
            canvas.drawPath(mPath,mPaint);

        }

        touchItem = null;

        new Handler().postDelayed(() -> invalidate(),300);

    }

    private ItemType touchItem;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (int i = 0; i < items.size(); i++) {
                    ItemType itemType = items.get(i);
                    int[] values = itemType.values;
                    if (TextUtils.equals(itemType.shape, "circ")) {
                        int cx = (int)(values[0]*scale);
                        int cy = (int)(values[1]*scale);
                        int radius = (int)(values[2]*scale);

                        int dx = Math.abs(cx-x);
                        int dy = Math.abs(cy-y);
                        int dd = (int)Math.sqrt(dx*dx + dy*dy);
                        if(dd<radius){
                            itemType.listener.onClick();
                            touchItem = itemType;
                            invalidate();
                            return true;
                        }
                    }else if (TextUtils.equals(itemType.shape, "rect")) {
                        int left = (int)(values[0]*scale);
                        int top = (int)(values[1]*scale);
                        int right = (int)(values[2]*scale);
                        int bottom = (int)(values[3]*scale);

                        if(x>left && x<right && y>top && y<bottom){
                            itemType.listener.onClick();
                            touchItem = itemType;
                            invalidate();
                            return true;
                        }
                    } else {
                        int[] valueXs = new int[values.length/2];
                        int[] valueYs = new int[values.length/2];
                        for(int j=0;j<values.length;j+=2){
                            valueXs[j/2] = (int)(values[j]*scale);
                            valueYs[j/2] = (int)(values[j+1]*scale);
                        }
                        if (pnpoly(values.length / 2, valueXs, valueYs, x, y)) {
                            itemType.listener.onClick();
                            touchItem = itemType;
                            invalidate();
                            return true;
                        }
                    }
                }
                return false;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断一个坐标点是否在不规则多边形内部
     *
     * @param nvert :不规则区域的全部坐标(x,y)点个数,只算x或y一个坐标数组的长度即可
     * @param vertx :不规则区域X坐标数组
     * @param verty :单个分区Y坐标数组
     * @param testx :点击屏幕获取的x坐标
     * @param testy :点击屏幕获取的y坐标
     * @return :如果为true 表示在区域内,如果为false不在
     */
    private boolean pnpoly(int nvert, int[] vertx, int[] verty, int testx, int testy) {
        int i, j;
        boolean c = false;
        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            if (((verty[i] > testy) != (verty[j] > testy)) &&
                    (testx < (vertx[j] - vertx[i]) * (testy - verty[i]) / (verty[j] - verty[i]) + vertx[i]))
                c = !c;
        }
        return c;
    }

    public static class ItemType {

        /**
         * 圆形（circ 或 circle）
         * 多边形（poly 或 polygon）
         * 矩形（rect 或 rectangle）
         **/
        public String shape;
        public int[] values;
        public ItemListener listener;

        public ItemType(String shape, int[] values, ItemListener listener) {
            this.shape = shape;
            this.values = values;
            this.listener = listener;
        }
    }

    public interface ItemListener {
        void onClick();
    }
}
