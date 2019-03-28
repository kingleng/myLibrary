package com.example.leng.myapplication.view.activity;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.myView.MyImageView;
import com.example.leng.myapplication.view.myView.PullToRefreshRecyclerview;

public class RefreshActivity extends AppCompatActivity {

    PullToRefreshRecyclerview pullToRefreshRecyclerview;

    MyImageView imageView;
    int width;
    int height;

    private float currentScale = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
//        getSupportActionBar().setTitle("My new title"); // set the top title
//
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        pullToRefreshRecyclerview = (PullToRefreshRecyclerview)findViewById(R.id.pulltorefreshRecyclerView0);

//        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
//        width = wm.getDefaultDisplay().getWidth();
//        height = wm.getDefaultDisplay().getHeight();

//        imageView = (MyImageView)findViewById(R.id.test_image);

//        //获得ImageView中Image的变换矩阵
//        Matrix m = imageView.getImageMatrix();
//        float[] values = new float[10];
//        m.getValues(values);
//
//        //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
//        float sx = values[0];
//        currentScale = sx;
//        Log.e("lxy", "currentScale = " + currentScale);

//        imageView.setOnTouchListener(new TouchListener());

    }

    public void onComposeAction(MenuItem mi){
        Toast.makeText(this,"onComposeAction",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    private final class TouchListener implements View.OnTouchListener {

        /** 记录是拖拉照片模式还是放大缩小照片模式 */
        private int mode = 0;// 初始状态
        /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;

        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        /** 用于记录拖拉图片移动的坐标位置 */
        private Matrix matrix = new Matrix();
        /** 用于记录图片要进行拖拉时候的坐标位置 */
        private Matrix currentMatrix = new Matrix();

        /** 两个手指的开始距离 */
        private float startDis;
        /** 两个手指的中间点 */
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            //获得ImageView中Image的变换矩阵
            Matrix m = imageView.getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];
            Log.d("lxy", "scale_X = " + sx + ", scale_Y = " + sy);
            //Image在绘制过程中的变换矩阵，从中获得x和y方向的偏移量
            float mx = values[2];
            float my = values[5];
            Log.d("lxy", "X偏移量 = " + values[2] + ", Y偏移量 = " + values[5]);

            //获得ImageView中Image的真实宽高，
            int dw = imageView.getDrawable().getBounds().width();
            int dh = imageView.getDrawable().getBounds().height();
            Log.d("lxy", "drawable_X = " + dw + ", drawable_Y = " + dh);

            //计算Image在屏幕上实际绘制的宽高
            int cw = (int)(dw * sx);
            int ch = (int)(dh * sy);
            Log.d("lxy", "caculate_W = " + cw + ", caculate_H = " + ch);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);

                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        if(sx>currentScale){
                            float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                            float dy = event.getY() - startPoint.y; // 得到x轴的移动距离

                            if(cw<width){
                                dx = 0;
                            }
                            if(ch<height){
                                dy = 0;
                            }
//                            if((mx>=0) && (dx>=0)){
//                                dx = 0;
//                                Log.e("ssss","dx1111111");
//                            }else{
//                                Log.e("ssss","dx22222222");
//                            }
//                            Log.e("ssss","dx="+dx+"dy="+dy);
//                            if(((mx+cw)<width) && (dx<0)){
//                                dx = 0;
//                            }

                            // 在没有移动之前的位置上进行移动
                            matrix.set(currentMatrix);
                            matrix.postTranslate(dx, dy);
                        }
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
//                            currentScale = currentScale* scale;
//                            currentScale=(float)(Math.round(currentScale*100)/100);
//                            Log.e("currentScale","::"+currentScale);

                            //放大
                            if(scale>1d){
                                //放大限制
                                if(mx>(width*2)){
                                    matrix.set(currentMatrix);
//                                    matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                                }else{
                                    matrix.set(currentMatrix);
                                    matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                                }

                            }//缩小
                            else{
                                matrix.set(currentMatrix);
                                matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                            }

//                            if(sx>=currentScale){
//                                if(cw>width && ch>height){
//                                    if(scale<1){
//                                        matrix.set(currentMatrix);
//                                        matrix.postScale(scale, scale,midPoint.x,midPoint.y);
//                                    }
//                                }else{
//                                    matrix.set(currentMatrix);
//                                    matrix.postScale(scale, scale,midPoint.x,midPoint.y);
//                                }
//
//                            }else{
//                                if(scale>1){
//
//                                }
//                            }
                        }
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;

                    if(cw<width && ch<height){
//                        float scale0 = Math.min(width/cw,height/ch);
//                        matrix.set(currentMatrix);
//                        matrix.postScale(scale0, scale0);
                        center(cw,ch);
                    }
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }

        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }

        //自动居中  左右及上下都居中
        protected void center(int ww,int hh) {
            center(true,true,ww,hh);
        }

        private void center(boolean horizontal, boolean vertical,int ww,int hh) {
            Matrix m = new Matrix();
            m.set(matrix);
            RectF rect = new RectF(0, 0, ww, hh);
            m.mapRect(rect);
            float mheight = rect.height();
            float mwidth = rect.width();
            float deltaX = 0, deltaY = 0;
            if (vertical) {
                if (mheight < height) {
                    deltaY = (height - mheight)/2 - rect.top;
                }else if (rect.top > 0) {
                    deltaY = -rect.top;
                }else if (rect.bottom < height) {
                    deltaY = mheight - rect.bottom;
                }
            }

            if (horizontal) {
                if (mwidth < width) {
                    deltaX = (width - mwidth)/2 - rect.left;
                }else if (rect.left > 0) {
                    deltaX = -rect.left;
                }else if (rect.right < width) {
                    deltaX = width - rect.right;
                }
            }
            matrix.postTranslate(deltaX/2, deltaY/2);
        }

    }
}
