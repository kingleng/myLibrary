package com.example.leng.myapplication2.ui.myView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by leng on 2019/7/12.
 */
public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    float lastX;
//    float lastY;
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastX = ev.getX();
//                lastY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(Math.abs(lastX-ev.getX())>10 || Math.abs(lastY-ev.getY())>10){
//                    return true;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
