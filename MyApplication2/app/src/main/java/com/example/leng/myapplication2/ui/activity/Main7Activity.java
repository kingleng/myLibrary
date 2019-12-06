package com.example.leng.myapplication2.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.PathView;
import com.example.leng.myapplication2.ui.tools.DensityUtil;
import com.example.leng.myapplication2.ui.tools.TranslucentBarUtil;

public class Main7Activity extends Activity {

    PathView pathView1;
    PathView pathView2;
    PathView pathView3;
    PathView pathView4;
    PathView pathView5;
    PathView pathView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TranslucentBarUtil.setTranslucentBar(this, true);
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = this.getWindow();
//            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        init();
    }

    private void init() {
        pathView1 = (PathView)findViewById(R.id.path_view1);
        pathView2 = (PathView)findViewById(R.id.path_view2);
        pathView3 = (PathView)findViewById(R.id.path_view3);
        pathView4 = (PathView)findViewById(R.id.path_view4);
        pathView5 = (PathView)findViewById(R.id.path_view5);
        pathView6 = (PathView)findViewById(R.id.path_view6);

        int[] datas = getWidth(this);
        int mWidth = datas[0];
        int mHeight = datas[1] - DensityUtil.dip2px(this,50);

        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        Path path4 = new Path();
        Path path5;
        Path path6;

        path1.moveTo(mWidth/2, mHeight/2);
        path2.moveTo(mWidth/2, mHeight/2);
        path3.moveTo(mWidth/2, mHeight/2);
        path4.moveTo(mWidth/2, mHeight/2);

//        path1.lineTo(0, mHeight/2);
//        path2.lineTo(mWidth, mHeight/2);

        path1.quadTo(mWidth/4,mHeight/4,0, mHeight/2);
        path2.quadTo(mWidth/4*3,mHeight/4*3,mWidth, mHeight/2);
        path3.quadTo(mWidth/4*3,mHeight/4,mWidth/2, 0);
        path4.quadTo(mWidth/4,mHeight/4*3,mWidth/2, mHeight);

        path1.quadTo(mWidth/4,mHeight/4*3,mWidth/2, mHeight/2);
        path2.quadTo(mWidth/4*3,mHeight/4,mWidth/2, mHeight/2);
        path3.quadTo(mWidth/4,mHeight/4,mWidth/2, mHeight/2);
        path4.quadTo(mWidth/4*3,mHeight/4*3,mWidth/2, mHeight/2);

//        path1.lineTo(mWidth/2, mHeight/2);
//        path2.lineTo(mWidth/2, mHeight/2);
//        path3.lineTo(mWidth/2, mHeight/2);
//        path4.lineTo(mWidth/2, mHeight/2);

//        path1.moveTo(mWidth/12*5, 0);
//        path2.moveTo(mWidth/12*7, 0);

//        path1.lineTo(mWidth/12*5, mHeight/6);
//        path1.lineTo(mWidth/12*4, mHeight/10*2);
//        path1.lineTo(mWidth/12*4, mHeight/10*3);
//        path1.lineTo(mWidth/12*5, mHeight/6*2);
//        path1.lineTo(mWidth/12*5, mHeight/2);
//
//        path2.lineTo(mWidth/12*7, mHeight/6);
//        path2.lineTo(mWidth/12*8, mHeight/10*2);
//        path2.lineTo(mWidth/12*8, mHeight/10*3);
//        path2.lineTo(mWidth/12*7, mHeight/6*2);
//        path2.lineTo(mWidth/12*7, mHeight/2);

//        path3 = new Path(path1);
//        path3.offset(-mWidth/6, 0);
//        path5 = new Path(path1);
//        path5.offset(-mWidth/4, 0);
//        path4 = new Path(path2);
//        path4.offset(+mWidth/6, 0);
//        path6 = new Path(path2);
//        path6.offset(+mWidth/4, 0);

        pathView1.setPath(path1);
        pathView2.setPath(path2);
        pathView3.setPath(path3);
        pathView4.setPath(path4);
//        pathView5.setPath(path5);
//        pathView6.setPath(path6);

        pathView1.setLineWidth(5);
        pathView2.setLineWidth(5);
        pathView3.setLineWidth(5);
        pathView4.setLineWidth(5);
//        pathView5.setLineWidth(5);
//        pathView6.setLineWidth(5);

//        pathView3.setMode(PathView.TRAIN_MODE);
//        pathView4.setMode(PathView.TRAIN_MODE);

        pathView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView1.startAnimation();
                pathView2.startAnimation();
                pathView3.startAnimation();
                pathView4.startAnimation();
//                pathView5.startAnimation();
//                pathView6.startAnimation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pathView6.performClick();
    }

    public static int[] getWidth(Context context){
        int[] data = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        data[0] = width;
        data[1] = height;
        return data;
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
