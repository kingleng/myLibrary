package com.example.leng.myapplication2.ui.myView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

public class DragView extends ImageView {

    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
    private long mDownTimeMillion;
    private int mWidth;
    private int mHeight;
    private int mScreenWidth;
    private int mDisplayHeight;
    private OnClickListener mClickListener;

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragView(Context context) {
        super(context);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(null);
        mClickListener = l;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        int mScreenHeight = dm.heightPixels;
        Rect frame = new Rect();
        ((Activity) getContext()).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        mDisplayHeight = (int) (mScreenHeight - frame.top - dm.density * 48);
        mWidth = w;
        mHeight = h;
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        // 备用代码：params.leftMargin = mScreenWidth - mWidth;
        // 初始位置
        params.leftMargin = mScreenWidth - mWidth;
        params.topMargin = (int) ((mDisplayHeight - mHeight) * 0.6);
        setLayoutParams(params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = rawX;
                mLastY = rawY;
                mDownX = (int) event.getRawX();
                mDownY = (int) event.getRawY();
                mDownTimeMillion = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = rawX - mLastX;
                int offsetY = rawY - mLastY;
                int l = getLeft() + offsetX;
                int t = getTop() + offsetY;
                int r = getRight() + offsetX;
                int b = getBottom() + offsetY;
                if (l < 0) {
                    l = 0;
                    r = mWidth;
                }
                if (r > mScreenWidth) {
                    r = mScreenWidth;
                    l = mScreenWidth - mWidth;
                }
                if (t < 0) {
                    t = 0;
                    b = mHeight;
                }
                if (b > mDisplayHeight) {
                    b = mDisplayHeight;
                    t = mDisplayHeight - mHeight;
                }
                MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
                // 备用代码：params.leftMargin = l;
                params.topMargin = t;
                params.leftMargin = l;

                setLayoutParams(params);
                mLastX = rawX;
                mLastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                int mUpX = (int) event.getRawX();
                int mUpY = (int) event.getRawY();
                long mUpTimeMillion = System.currentTimeMillis();
                if (mUpTimeMillion - mDownTimeMillion < 1000
                        && Math.abs(mDownX - mUpX) < 3
                        && Math.abs(mDownY - mUpY) < 3) {
                    if (mClickListener != null) {
                        mClickListener.onClick(this);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

}
