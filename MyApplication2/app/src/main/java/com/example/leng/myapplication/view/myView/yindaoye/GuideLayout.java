package com.example.leng.myapplication.view.myView.yindaoye;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by 17092234 on 2018/6/27.
 */

public class GuideLayout extends RelativeLayout {


    public static final int DEFAULT_BACKGROUND_COLOR = 0xb2000000;

    private Paint mPaint;
    public GuidePage mPage;
    private OnGuideLayoutDismissListener listener;

    public GuideLayout(Context context, GuidePage page) {
        super(context);
        init();
        setGuidePage(page);
    }

    private GuideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private GuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(xfermode);

        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //ViewGroup默认设定为true，会使onDraw方法不执行，如果复写了onDraw(Canvas)方法，需要清除此标记
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(DEFAULT_BACKGROUND_COLOR);
        drawHightLights(canvas);
    }

    private void drawHightLights(Canvas canvas) {

        RectF rectF = mPage.mHighLight.getRectF((View) getParent());
        canvas.drawRect(rectF, mPaint);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void setGuidePage(GuidePage page) {
        this.mPage = page;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    public void setOnGuideLayoutDismissListener(OnGuideLayoutDismissListener listener) {
        this.listener = listener;
    }

    public void remove() {
        dismiss();
    }

    private void dismiss() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            if (listener != null) {
                listener.onGuideLayoutDismiss(this);
            }
        }
    }

    public interface OnGuideLayoutDismissListener {
        void onGuideLayoutDismiss(GuideLayout guideLayout);
    }
}
