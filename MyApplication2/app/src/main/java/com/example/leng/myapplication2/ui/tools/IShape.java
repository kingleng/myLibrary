package com.example.leng.myapplication2.ui.tools;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Author : xuan.
 * Date : 2018/1/27.
 * Description :interface of shapebuilder
 */

interface IShape {
    ShapeBuilder Type(int type);

    ShapeBuilder Stroke(int px, int color);

    ShapeBuilder Stroke(int px, int color, int dashWidth, int dashGap);

    ShapeBuilder Solid(int color);

    ShapeBuilder Radius(float px);

    ShapeBuilder Radius(float topleft, float topright, float botleft, float botright);

    ShapeBuilder Gradient(int startColor, int centerColor, int endColor);

    ShapeBuilder Gradient(int angle, int startColor, int centerColor, int endColor);

    ShapeBuilder Gradient(GradientDrawable.Orientation orientation, int startColor, int
            centerColor, int endColor);

    ShapeBuilder GradientType(int type);

    ShapeBuilder GradientCenter(float x, float y);

    ShapeBuilder GradientRadius(float radius);

    ShapeBuilder setSize(int width, int height);

    void build(View v);

    GradientDrawable build();
}
