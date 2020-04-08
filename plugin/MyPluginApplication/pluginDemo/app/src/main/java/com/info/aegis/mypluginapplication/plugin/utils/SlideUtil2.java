package com.info.aegis.mypluginapplication.plugin.utils;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlideUtil2 implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {


    private View mSliderView;
    private int mAutoSlideDuration;
    private int mDistance;
    private List<Listener> mListeners;
    private boolean isShow = true;
    public boolean canShow = true;

    private ValueAnimator mValueAnimator;
    private float mSlideAnimationTo;

//    private TimeInterpolator mInterpolator = new DecelerateInterpolator();
    private TimeInterpolator mInterpolator = new JellyInterpolator();

    public interface Listener {

        interface Slide extends Listener {

            /**
             * @param percent percents of complete slide <b color="#EF6C00">(100 = HIDDEN, 0 = SHOWED)</b>
             */
            void onSlide(float percent);
        }

        interface Visibility extends Listener {

            /**
             * @param visibility (<b>GONE</b> or <b>VISIBLE</b>)
             */
            void onVisibilityChanged(int visibility);
        }

        interface Events extends Visibility, Slide {
        }
    }

    public final static class Builder {

        private View mSliderView;
        private int mDistance;
        private List<Listener> mListeners = new ArrayList<>();
        private int mAutoSlideDuration = 300;
//        private TimeInterpolator mInterpolator = new DecelerateInterpolator();
        private TimeInterpolator mInterpolator = new JellyInterpolator();


        public Builder(@NonNull View sliderView) {
            mSliderView = sliderView;
        }

        public Builder withDistance(int mDistance) {
            this.mDistance = mDistance;
            return this;
        }

        public Builder withListeners(@NonNull List<Listener> listeners) {
            this.mListeners = listeners;
            return this;
        }

        public Builder withListeners(@NonNull Listener... listeners) {
            List<Listener> listeners_list = new ArrayList<>();
            Collections.addAll(listeners_list, listeners);
            return withListeners(listeners_list);
        }

        public SlideUtil2 build() {
            return new SlideUtil2(this);
        }

    }


    private SlideUtil2(Builder builder) {
        mListeners = builder.mListeners;
        mSliderView = builder.mSliderView;
        mAutoSlideDuration = builder.mAutoSlideDuration;
        mDistance = builder.mDistance;

        init();
    }

    private void init() {
        createAnimation();
        mSliderView.setTranslationX(mDistance);
        isShow = false;
        canShow = true;
    }

    public void hide(boolean immediately) {
//        if(mValueAnimator.isRunning() || !isShow()){
        if(!isShow()){
//            Log.e("asd","mValueAnimator.isRunning() = "+mValueAnimator.isRunning());
//            Log.e("asd","isShow = "+isShow);
            return;
        }

        isShow = false;
        canShow = false;

        endAnimation();

        if (!immediately) {
            mSliderView.setTranslationX(mDistance);
//            mSliderView.setTranslationY(mDistance);
        } else {
            mValueAnimator.setInterpolator(new DecelerateInterpolator());
            mValueAnimator.setDuration(300);
            setValuesAndStart(0, mDistance);
        }
    }

    public void show(boolean immediately) {
        if(mValueAnimator.isRunning() || isShow() || !canShow){
            return;
        }

        isShow = true;
        canShow = false;

        endAnimation();

        if (!immediately) {
            mSliderView.setTranslationX(-mDistance);
//            mSliderView.setTranslationY(-mDistance);
        } else {
            mValueAnimator.setInterpolator(new JellyInterpolator());
            mValueAnimator.setDuration(1000);
            setValuesAndStart(mDistance, 0);
        }
    }

    public boolean isShow(){
//        return mSliderView.getTranslationX() == 0;
        return isShow;
    }

    private void endAnimation() {
        if (mValueAnimator != null && mValueAnimator.getValues() != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
        }
    }


    private void setValuesAndStart(float from, float to) {
        mSlideAnimationTo = to;
        mValueAnimator.setFloatValues(from, to);
        mValueAnimator.start();
    }

    private void createAnimation() {
        mValueAnimator = ValueAnimator.ofFloat();
        mValueAnimator.setDuration(mAutoSlideDuration);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
    }

    @Override
    public final void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        mSliderView.setTranslationX(value);
//        mSliderView.setTranslationY(value);
    }

    @Override
    public final void onAnimationStart(Animator animator) {
//        Log.e("asd","onAnimationStart");
    }

    @Override
    public final void onAnimationEnd(Animator animator) {
//        Log.e("asd","onAnimationEnd");
    }

    @Override
    public final void onAnimationCancel(Animator animator) {
    }

    @Override
    public final void onAnimationRepeat(Animator animator) {
    }

    public static class JellyInterpolator implements TimeInterpolator {

        // 因子数值越小振动频率越高
        private float factor;

        public JellyInterpolator() {
            this.factor = 0.15f;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }

}
