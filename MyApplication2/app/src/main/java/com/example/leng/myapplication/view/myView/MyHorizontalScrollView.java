package com.example.leng.myapplication.view.myView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

public class MyHorizontalScrollView extends HorizontalScrollView {

    private View inner;

    RecyclerView recyclerView;

    public MyHorizontalScrollView(Context context) {
        this(context,null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //取消滑动到最前和最后是出现的蓝色颜色阴影块
        setOverScrollMode(View.OVER_SCROLL_NEVER);

        recyclerView = new RecyclerView(context, attrs);
//        recyclerView = findViewById(R.id.recyclerView);

        this.addView(recyclerView);
    }

    public RecyclerView getContentView(){
        return recyclerView;
    }

    private float lastX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                boolean canMoveDown = false;
                if(recyclerView!=null){
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    // 获取最后一个完全显示的itemPosition
                    int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                    int itemCount = manager.getItemCount();

                    // 判断是否滑动到了最后一个item，并且是向左滑动
                    if (lastItemPosition == (itemCount - 1)) {
                        float dx = Math.abs(ev.getX() - lastX);
                        if(dx<=0){
//                            canMoveDown = recyclerView.canScrollVertically(1);
                            canMoveDown = !isSlideToBottom(recyclerView);
                        }else{
                            canMoveDown = true;
                        }
                    }else{
                        canMoveDown = true;
                    }
                }
                if(canMoveDown){
                    return false;
                }else{
                    return super.onInterceptTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()>0)
        inner=getChildAt(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(inner!=null){
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private float downX;
    private static final int DEFAULT_DEVIDE=4;
    private Rect normal = new Rect();
    private void commOnTouchEvent(MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX=ev.getX();
                int deltX=(int)(downX-moveX)/DEFAULT_DEVIDE;
                downX=moveX;

                //不能滚动就直接移动布局
                if(isNeedMove()){
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                        return;
                    }
                    inner.layout(inner.getLeft()-deltX,inner.getTop(),inner.getRight()-deltX,inner.getBottom());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    animation();
                }
                break;
        }
    }

    private boolean isNeedMove(){
        int offset=inner.getMeasuredWidth()-getWidth();
        int scrollX=getScrollX();
        Log.d("zbv","offset="+offset+";scrollX="+scrollX);
        //头和尾
//        if(scrollX==0||scrollX==offset){
//            return true;
//        }
        if(scrollX==offset){
            return true;
        }

        return false;
    }

    // 开启动画移动

    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(getLeft(), normal.left, 0,
                0);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }
}