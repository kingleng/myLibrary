package com.cpsdna.app.mykotlinapplication.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpsdna.app.mykotlinapplication.R;
import com.cpsdna.app.mykotlinapplication.TopApplication;

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {

    private TextView borderText = null;///用于描边的TextView

    public StrokeTextView(Context context) {
        super(context);
        borderText = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context,attrs);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context,attrs,defStyle);
        init();

    }

    public void init(){
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(getContext().getResources().getDimension(R.dimen.px2));                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        borderText.setTextColor(getResources().getColor(R.color.color_F8FF543E));  //设置描边颜色
//        borderText.setGravity(getGravity());
        setTypeface(TopApplication.tf);
        setTextColor(getResources().getColor(R.color.color_ffffff));

        setIncludeFontPadding(false);
        setShadowLayer(1.2f,0.8f,0.8f,R.color.color_AC3E2C);
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        borderText.setPadding(left, top, right, bottom);
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        borderText.setTextSize(unit, size);
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
        borderText.setGravity(gravity);
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        borderText.setLineSpacing(add, mult);
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        super.setSingleLine(singleLine);
        borderText.setSingleLine(singleLine);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) {
        super.setTypeface(tf);
//        borderText.setTypeface(tf);
    }

    @Override
    public void setIncludeFontPadding(boolean includepad) {
        super.setIncludeFontPadding(includepad);
        borderText.setIncludeFontPadding(includepad);
    }

    @Override
    public void setShadowLayer(float radius, float dx, float dy, int color) {
//        super.setShadowLayer(radius, dx, dy, color);
        borderText.setShadowLayer(radius, dx, dy, color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

//        AssetManager manager = getContext().getAssets();
//        String path = "SourceHanSerifCN-Bold.otf.ttf";
//        Typeface type = Typeface.createFromAsset(manager, path);

        //两个TextView上的文字必须一致
        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
//            borderText.setTypeface(type);
            borderText.setTypeface(TopApplication.tf);
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);

    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

}