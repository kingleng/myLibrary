package com.example.leng.myapplication2.ui.myView.star;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarView extends View {

    public StarView(Context context) {
        super(context);
        init();
    }

    public StarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint paint;
    List<DotBean> dots = new ArrayList<>();
    int centerX,centerY;
    ValueAnimator valueAnimator;
    Random random;

    private void init(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        random = new Random();

        valueAnimator = new ValueAnimator().ofFloat(0f,1f);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updataDot((float)animation.getAnimatedValue());
                invalidate();
            }
        });
    }

    private void updataDot(float value){
        for(DotBean dotBean:dots){
            if(dotBean.offset>dotBean.maxOffset){
                dotBean.offset = 0;
                dotBean.speed = random.nextInt(10)+5;
            }

            dotBean.alpha = (int)((1f-dotBean.offset/dotBean.maxOffset)*255);

            dotBean.x = centerX+(float)(Math.cos(dotBean.angle)*(280+dotBean.offset));
            if(dotBean.y>centerY){
                dotBean.y = centerY+(float)(Math.sin(dotBean.angle)*(280+dotBean.offset));
            }else{
                dotBean.y = centerY-(float)(Math.sin(dotBean.angle)*(280+dotBean.offset));
            }

            dotBean.offset += dotBean.speed;
        }
    }

    Path path = new Path();
    PathMeasure pathMeasure = new PathMeasure();
    float[] pos = new float[2];
    float[] tan = new float[2];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w/2;
        centerY = h/2;
        path.addCircle(centerX,centerY,280f, Path.Direction.CCW);
        pathMeasure.setPath(path,false);

        for(int i=0;i<5000;i++){
            pathMeasure.getPosTan(i/5000f*pathMeasure.getLength(),pos,tan);
            float x = pos[0]+random.nextInt(6)-3;
            float y = pos[1]+random.nextInt(6)-3;
            int speed = random.nextInt(10)+5;
            float offset = random.nextInt(300);
            double angle = Math.acos((pos[0]-centerX)/280f);
            dots.add(new DotBean(x,y,speed,400,offset,angle));
        }
        valueAnimator.start();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Long t1 = System.currentTimeMillis();
        for(DotBean dotBean:dots){
            paint.setAlpha(dotBean.alpha);
            canvas.drawCircle(dotBean.x,dotBean.y,dotBean.radius,paint);
        }
//        Long t2 = System.currentTimeMillis();
//        long t3 = t2-t1;
//        Log.e("asd"," time = "+t3);
    }

    class DotBean{
        float x;
        float y;
        float radius = 2;
        float speed = 2;
        int alpha = 255;
        float maxOffset = 600f;
        float offset = 0;
        double angle = 0d;

        public DotBean(float x, float y, float speed, float maxOffset, float offset, double angle) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.maxOffset = maxOffset;
            this.offset = offset;
            this.angle = angle;
        }
    }
}
