package com.example.leng.myapplication2.ui.myView.yindaoye;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class HighLight {

    private View mHole;
    private Shape shape = Shape.RECTANGLE;
    /**
     * 圆角，仅当shape = Shape.ROUND_RECTANGLE才生效
     */
    private int round;
    /**
     * 高亮相对view的padding
     */
    private int padding;

    public static HighLight newInstance(View view) {
        return new HighLight(view);
    }

    private HighLight(View hole) {
        this.mHole = hole;
    }

    public HighLight setShape(Shape shape) {
        this.shape = shape;
        return this;
    }

    public HighLight setRound(int round) {
        this.round = round;
        return this;
    }

    public HighLight setPadding(int padding) {
        this.padding = padding;
        return this;
    }

    public int getPadding() {
        return padding;
    }

    public Shape getShape() {
        return shape;
    }

    public int getRound() {
        return round;
    }

    public int getRadius() {
        if (mHole == null) {
            throw new IllegalArgumentException("the hight light view is null!");
        }
        return Math.max(mHole.getWidth() / 2, mHole.getHeight() / 2) + padding;
    }

    public RectF getRectF(View target) {
        RectF rectF = new RectF();
        Rect locationInView = ViewUtils.getLocationInView(target, mHole);
        rectF.left = locationInView.left - padding;
        rectF.top = locationInView.top - padding;
        rectF.right = locationInView.right + padding;
        rectF.bottom = locationInView.bottom + padding;
        return rectF;
    }

    public enum Shape {
        CIRCLE,//圆形
        RECTANGLE, //矩形
        OVAL,//椭圆
        ROUND_RECTANGLE;//圆角矩形
    }

}