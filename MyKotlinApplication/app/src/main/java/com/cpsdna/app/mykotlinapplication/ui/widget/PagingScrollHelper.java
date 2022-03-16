package com.cpsdna.app.mykotlinapplication.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

public class PagingScrollHelper {

    RecyclerView mRecyclerView = null;

    private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();

    private MyOnFlingListener mOnFlingListener = new MyOnFlingListener();
    private int offsetY = 0;
    private int offsetX = 0;

    int startY = 0;
    int startX = 0;


    enum ORIENTATION {
        HORIZONTAL, VERTICAL, NULL
    }

    ORIENTATION mOrientation = ORIENTATION.HORIZONTAL;

    public void setUpRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
//        //处理滑动
//        recycleView.setOnFlingListener(mOnFlingListener);
//        //设置滚动监听，记录滚动的状态，和总的偏移量
//        recycleView.setOnScrollListener(mOnScrollListener);
//        //记录滚动开始的位置
//        recycleView.setOnTouchListener(mOnTouchListener);
        //获取滚动的方向
        stopScroll();
        updateLayoutManger();

        startScroll();
    }

    int endPoint0 = 0;
    int startPoint0 = 0;
    ValueAnimator mAutoAnimator = null;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mRecyclerView!=null){
                int currentPosition = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                //获取开始滚动时所在页面的index
                int p = currentPosition;
                currentPosition++;
                if(currentPosition>=mRecyclerView.getAdapter().getItemCount()){
                    currentPosition = 0;
                }
                startPoint0 = p * mRecyclerView.getHeight();
                endPoint0 = (currentPosition) * mRecyclerView.getHeight();
                if (endPoint0 < 0) {
                    endPoint0 = 0;
                }

                //使用动画处理滚动
                if (mAutoAnimator == null) {
                    mAutoAnimator = new ValueAnimator().ofInt(startPoint0, endPoint0);
                    if(currentPosition == 0){
                        mAutoAnimator.setDuration(10);
                    }else{
                        mAutoAnimator.setDuration(1000);
                    }
                    mAutoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int nowPoint = (int) animation.getAnimatedValue();
                            int dy = nowPoint - startPoint0;
                            mRecyclerView.scrollBy(0, dy);
                            startPoint0 = startPoint0+ dy;
                        }
                    });
                    mAutoAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //回调监听
                            if (null != mOnPageChangeListener) {
                                mOnPageChangeListener.onPageChange(endPoint0 / mRecyclerView.getHeight());
                            }
                        }
                    });

                } else {
                    mAutoAnimator.cancel();
                    if(currentPosition == 0){
                        mAutoAnimator.setDuration(10);
                    }else{
                        mAutoAnimator.setDuration(1000);
                    }
                    mAutoAnimator.setIntValues(startPoint0, endPoint0);
                }
                mAutoAnimator.start();


            }
            handler.sendEmptyMessageDelayed(1, 3000);
        }
    };

    /**
     * 开始无限轮播
     */
    public void startScroll() {
        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    /**
     * 停止无限轮播
     */
    public void stopScroll() {
        handler.removeMessages(1);
    }

    public void updateLayoutManger() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION.VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION.HORIZONTAL;
            } else {
                mOrientation = ORIENTATION.NULL;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            startX = 0;
            startY = 0;
            offsetX = 0;
            offsetY = 0;

        }

    }

    ValueAnimator mAnimator = null;

    public class MyOnFlingListener extends RecyclerView.OnFlingListener {

        @Override
        public boolean onFling(int velocityX, int velocityY) {
            if (mOrientation == ORIENTATION.NULL) {
                return false;
            }

            //获取开始滚动时所在页面的index
            int p = getStartPageIndex();

            //记录滚动开始和结束的位置
            int endPoint = 0;
            int startPoint = 0;

            //如果是垂直方向
            if (mOrientation == ORIENTATION.VERTICAL) {
                startPoint = offsetY;

                if (velocityY < 0) {
                    p--;
                } else if (velocityY > 0) {
                    p++;
                }
                //更具不同的速度判断需要滚动的方向
                //注意，此处有一个技巧，就是当速度为0的时候就滚动会开始的页面，即实现页面复位
                endPoint = p * mRecyclerView.getHeight();

            } else {
                startPoint = offsetX;
                if (velocityX < 0) {
                    p--;
                } else if (velocityX > 0) {
                    p++;
                }
                endPoint = p * mRecyclerView.getWidth();

            }
            if (endPoint < 0) {
                endPoint = 0;
            }

            //使用动画处理滚动
            if (mAnimator == null) {
                mAnimator = new ValueAnimator().ofInt(startPoint, endPoint);

                mAnimator.setDuration(300);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int nowPoint = (int) animation.getAnimatedValue();

                        if (mOrientation == ORIENTATION.VERTICAL) {
                            int dy = nowPoint - offsetY;
                            //这里通过RecyclerView的scrollBy方法实现滚动。
                            mRecyclerView.scrollBy(0, dy);
                        } else {
                            int dx = nowPoint - offsetX;
                            mRecyclerView.scrollBy(dx, 0);
                        }
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //回调监听
                        if (null != mOnPageChangeListener) {
                            mOnPageChangeListener.onPageChange(getPageIndex());
                        }
                    }
                });
            } else {
                mAnimator.cancel();
                mAnimator.setIntValues(startPoint, endPoint);
            }

            mAnimator.start();

            return true;
        }
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //newState==0表示滚动停止，此时需要处理回滚
            if (newState == 0 && mOrientation != ORIENTATION.NULL) {
                boolean move;
                int vX = 0, vY = 0;
                if (mOrientation == ORIENTATION.VERTICAL) {
                    int absY = Math.abs(offsetY - startY);
                    //如果滑动的距离超过屏幕的一半表示需要滑动到下一页
                    move = absY > recyclerView.getHeight() / 2;
                    vY = 0;

                    if (move) {
                        vY = offsetY - startY < 0 ? -1000 : 1000;
                    }

                } else {
                    int absX = Math.abs(offsetX - startX);
                    move = absX > recyclerView.getWidth() / 2;
                    if (move) {
                        vX = offsetX - startX < 0 ? -1000 : 1000;
                    }

                }

                mOnFlingListener.onFling(vX, vY);

            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //滚动结束记录滚动的偏移量
            offsetY += dy;
            offsetX += dx;
        }
    }

    private MyOnTouchListener mOnTouchListener = new MyOnTouchListener();


    public class MyOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //手指按下的时候记录开始滚动的坐标
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startY = offsetY;
                startX = offsetX;
            }
            return false;
        }

    }

    private int getPageIndex() {
        int p = 0;
        if (mOrientation == ORIENTATION.VERTICAL) {
            p = offsetY / mRecyclerView.getHeight();
        } else {
            p = offsetX / mRecyclerView.getWidth();
        }
        return p;
    }

    private int getStartPageIndex() {
        int p = 0;
        if (mOrientation == ORIENTATION.VERTICAL) {
            p = startY / mRecyclerView.getHeight();
        } else {
            p = startX / mRecyclerView.getWidth();
        }
        return p;
    }

    onPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int index);
    }

}