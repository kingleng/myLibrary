package com.example.leng.myapplication2.view.myView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.leng.myapplication2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17092234 on 2018/11/26.
 */

public class MyAnimator extends View {

    int mIndicatorColor; //颜色
    Paint mPaint;
    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 60;
    private boolean mHasAnimation;

    /**
     * 缩放比例
     **/
    float scaleFloats;

    /**
     * 色值,颜色变化
     **/
    int alphas;

    private List<Animator> mAnimators;

    public MyAnimator(Context context) {
        super(context);
        init();
    }

    public MyAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyAnimator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        mPaint.setColor(mIndicatorColor);
        this.invalidate();
    }

    private void init() {
//        mIndicatorColor = Color.WHITE;
        mIndicatorColor = getResources().getColor(R.color.color_ffaaff);
        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw(canvas, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            initAnimation();
        }
    }

    /**
     * 画指示器
     *
     * @param canvas
     * @param paint
     */
    public void draw(Canvas canvas, Paint paint) {
        double radius = Math.sqrt(getHeight()*getHeight() + getWidth()*getWidth());

        Point point = new Point(getRight() - getPaddingRight(), getBottom() - getPaddingBottom());
        paint.setAlpha(alphas);
        canvas.drawCircle(point.x, point.y, (float) (radius*scaleFloats), paint);
    }



    /**
     * 初始化动画
     */
    public void initAnimation() {
        mAnimators = startOpenAnimation();
    }

    public List<Animator> startOpenAnimation(){

        List<Animator> animators = new ArrayList<>();

        ValueAnimator scaleAnim = ValueAnimator.ofFloat(0.05f, 0.30f, 0.55f, 0.8f,1.05f);
        scaleAnim.setDuration(1000);
//        scaleAnim.setRepeatCount(1);
        scaleAnim.setStartDelay(0);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloats = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();

        ValueAnimator alphaAnim = ValueAnimator.ofInt(0, 255, 255, 255, 255);
        alphaAnim.setDuration(1000);
//        alphaAnim.setRepeatCount(1);
        alphaAnim.setStartDelay(0);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alphas= (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        alphaAnim.start();
        animators.add(scaleAnim);
        animators.add(alphaAnim);

        return animators;
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                setAnimationStatus(AnimStatus.END);
            } else {
                setAnimationStatus(AnimStatus.START);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setAnimationStatus(AnimStatus.CANCEL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setAnimationStatus(AnimStatus.START);
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     *
     * @param animStatus
     */
    public void setAnimationStatus(AnimStatus animStatus) {
        if (mAnimators == null) {
            return;
        }
        int count = mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator = mAnimators.get(i);
            boolean isRunning = animator.isRunning();
            switch (animStatus) {
                case START:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END:
                    if (isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL:
                    if (isRunning) {
                        animator.cancel();
                    }
                    break;
            }
        }
    }


    public enum AnimStatus {
        START, END, CANCEL
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }


    final class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}