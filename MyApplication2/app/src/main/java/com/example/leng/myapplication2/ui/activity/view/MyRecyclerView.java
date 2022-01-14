package com.example.leng.myapplication2.ui.activity.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.QuickAdapter;

public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    float lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Log.e("asd", "onInterceptTouchEvent");
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //找到被点击位置的item的rootView
                View view = findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
//                    通过rootView找到对应的ViewHolder
                    QuickAdapter.VH holder = (QuickAdapter.VH) getChildViewHolder(view);
                    //由ViewHolder决定要不要请求不拦截,如果不拦截的话event就回一路传到rootView中.否则被rv消费.
                    ScrollView scrollview_ll = holder.getView(R.id.scrollview_ll);
                    if (lastY != 0) {
                        float dY = e.getY() - lastY;
                        if (dY > 0) {
                            Log.e("asd","下拉");
                            //下拉
                            boolean isTop;
                            if (scrollview_ll.canScrollVertically(-1)) {
                                Log.e("asd","下拉：到顶");
                                isTop = false;
                            }else{
                                Log.e("asd","下拉：未到顶");
                                isTop = true;
                            }
                            return isTop;
                        } else {
                            //上滑
                            Log.e("asd","上滑");
                            boolean isBottom;
                            if (scrollview_ll.canScrollVertically(1)){
                                Log.e("asd","上滑：未到底");
                                isBottom = false;
                            }else{
                                Log.e("asd","上滑：到底");
                                isBottom = true;
                            }

                            return isBottom;
                        }
                    }
                }
                lastY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
                lastY = 0;
                break;
        }
        return false;
    }
}