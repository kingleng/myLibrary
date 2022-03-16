package com.example.leng.myapplication2.ui.myView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by 17092234 on 2018/8/16.
 */

public class SnArVideoTab extends LinearLayout {

    private int mScreenWidth;
    private Scroller mScroller;
    private int mLastX;

    int TouchSlop;

    private OnChangeListener listener;

    public void setListener(OnChangeListener listener) {
        this.listener = listener;
    }


    public SnArVideoTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SnArVideoTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    void initView(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

        TouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

    }

    private int mTotalLength;

    private int childViewWidth1;
    private int childViewWidth2;
//    private int childViewWidth3;

    private int keyLeft;
    private int keyRight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int count = getChildCount();
        for(int i=0;i<count;i++){

            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

//            if(count == 3){
//                if(i==0){
//                    mTotalLength += childWidth + lp.leftMargin + lp.rightMargin;
//                    childViewWidth1 = childWidth + lp.leftMargin + lp.rightMargin;
//                }else if(i==1){
//                    mTotalLength += childWidth/2 + lp.leftMargin;
//                    childViewWidth2 = childWidth + lp.leftMargin + lp.rightMargin;
//                }else{
//                    childViewWidth3 = childWidth + lp.leftMargin + lp.rightMargin;
//                }
//            }else if(count == 2){
//                if(i==0){
//                    mTotalLength += childWidth/2 + lp.leftMargin;
//                    childViewWidth1 = childWidth + lp.leftMargin + lp.rightMargin;
//                }else if(i==1){
//                    childViewWidth2 = childWidth + lp.leftMargin + lp.rightMargin;
//                }
//            }

            if(i==0){
                mTotalLength += childWidth/2 + lp.leftMargin;
                childViewWidth1 = childWidth + lp.leftMargin + lp.rightMargin;
            }else if(i==1){
                childViewWidth2 = childWidth + lp.leftMargin + lp.rightMargin;
            }

        }

        scrollBy(-(mScreenWidth/2-mTotalLength), 0);

        keyLeft = -getScrollX() - childViewWidth2 - childViewWidth1/2;
        keyRight = -getScrollX() + childViewWidth1/2;

    }

    int touchX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                touchX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int dx = mLastX - x;

                scrollBy(dx, 0);
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                int count = getChildCount();
                if(Math.abs(touchX - x) <= TouchSlop){
                    for(int i=0;i<count;i++){
                        View child = getChildAt(i);
                        LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        float startX = child.getLeft();
                        float cdX = child.getMeasuredWidth()+lp.rightMargin;
                        float cpX = -getScrollX();

                        if((startX - lp.leftMargin + cpX) <= event.getX() && (startX + cdX + cpX) > event.getX()){
                            mScroller.startScroll(getScrollX(), 0,(int)((startX + child.getMeasuredWidth()/2 +cpX) -mScreenWidth/2), 0);
                            if(listener!=null){
                                listener.onItemChange(i);
                            }
                            break;
                        }
                    }

                    break;
                }


                for(int i=0;i<count;i++){
                    View child = getChildAt(i);
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    float startX = child.getLeft();
                    float cdX = child.getMeasuredWidth()+lp.rightMargin;
                    float cpX = -getScrollX();

                    if((startX - lp.leftMargin + cpX) <= mScreenWidth/2 && (startX + cdX + cpX) > mScreenWidth/2){
                        mScroller.startScroll(getScrollX(), 0,(int)((startX + cdX/2 +cpX) -mScreenWidth/2), 0);
                        if(listener!=null){
                            listener.onItemChange(i);
                        }
                        break;
                    }
                }

                if(-getScrollX()<keyLeft){
                    mScroller.startScroll(getScrollX(), 0, -(keyLeft + getScrollX()+childViewWidth2/2), 0);
                    if(listener!=null){
                        listener.onItemChange(2);
                    }

                }

                if(-getScrollX() > keyRight){
                    mScroller.startScroll(getScrollX(), 0, (-getScrollX()-(keyRight-childViewWidth1/2)), 0);
                    if(listener!=null){
                        listener.onItemChange(0);
                    }
                }

                break;
        }
        postInvalidate();
        return true;
    }

    public void setCurrentItem(int position){
        int count = getChildCount();
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            float startX = child.getLeft();
            float cdX = child.getMeasuredWidth()+lp.rightMargin;
            float cpX = -getScrollX();

            if(position == i){
                mScroller.startScroll(getScrollX(), 0,(int)((startX + cdX/2 +cpX) -mScreenWidth/2), 0);
                if(listener!=null){
                    listener.onItemChange(i);
                }
                break;
            }
        }
        postInvalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public interface OnChangeListener{
        void onItemChange(int position);
    }


}
