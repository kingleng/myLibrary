package com.example.leng.myapplication.view.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.tools.DensityUtil;

import java.util.ArrayList;

/**
 * Created by 17092234 on 2018/4/24.
 */

public class MarqueeView extends View implements Runnable {

    public static final int REPERT_TYPE_ONE = 0;
    public static final int REPERT_TYPE_TWO = 1;

    public static final int ANIM_TYPE_ONE = 0;
    public static final int ANIM_TYPE_TWO = 1;
    public static final int ANIM_TYPE_THREE = 2;

    public Context context;
    public int repert_type = REPERT_TYPE_ONE;
    public int anim_type = ANIM_TYPE_ONE;
    int text_color;
    int text_size;

    Float text_speed;


    String text = "我来猜猜看，天上有一个太阳我来猜猜看，天上有一个太阳我来猜猜看，天上有一个太阳我来猜猜看，天上有一个太阳";

    Thread mThread;

    private TextPaint mPaint;
    private Rect rect;

    private boolean resetInit = true;
    private boolean isRepert = true;

    private float textHeight;
    private float textWidth;
    private float oneBlack_width;

    private float fx = 0;
    private float fy = 0;
    private float fa = 0;

    public MarqueeView(Context context) {
        this(context,null);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.marqueeview);
        text_color = typedArray.getColor(R.styleable.marqueeview_text_color, Color.BLACK);
        text_size = typedArray.getDimensionPixelSize(R.styleable.marqueeview_text_size, DensityUtil.dip2px(context, 16));
        text_speed = typedArray.getFloat(R.styleable.marqueeview_text_speed, 1);
        anim_type = typedArray.getInt(R.styleable.marqueeview_text_anim_type, ANIM_TYPE_ONE);
        repert_type = typedArray.getInt(R.styleable.marqueeview_text_repert_type, REPERT_TYPE_ONE);
        typedArray.recycle();//释放资源

        init();
        startRoll();
    }

    private void init() {
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(text_color);
        mPaint.setTextSize(text_size);
    }

    private void startRoll() {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }

        mThread = new Thread(this);
        mThread.start();
    }

    String mstring = text;
    ArrayList<String> texts = new ArrayList<>();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (anim_type) {
            case ANIM_TYPE_ONE:
                if (resetInit) {
                    oneBlack_width = getBlacktWidth();
                    textWidth = getContentWidth(text);
                    fx = getWidth();
                    fy = getHeight() / 2;
                    resetInit = false;
                }
                if (repert_type == REPERT_TYPE_ONE) {
                    if ((Math.abs(fx) - oneBlack_width) > Math.max(getWidth(), textWidth)) {
                        isRepert = false;
                    }

                    mstring = text;
                    texts.clear();
                    texts.add(mstring);
                } else {
                    if ((Math.abs(fx) - oneBlack_width) > Math.max(getWidth(), textWidth)) {
                        fx = 0;
                    }
                    if (getWidth() - fx > textWidth) {
                        mstring = text + " " + text;
                        texts.clear();
                        texts.add(mstring);
                    } else {
                        mstring = text;
                        texts.clear();
                        texts.add(mstring);
                    }
                }
                break;
            case ANIM_TYPE_TWO:
                if (resetInit) {
                    texts.clear();
                    texts = getStrings(text);
                    fy = getHeight()+textHeight;
                    resetInit = false;
                }
                mstring = text;
                if (repert_type == REPERT_TYPE_ONE) {
                    if (Math.abs(fy)<0) {
                        isRepert = false;
                    }
                } else {
                    if (fy<-(textHeight+DensityUtil.dip2px(context,5))*texts.size()) {
                        fy = getHeight()+textHeight;
                    }
                }
                break;
            case ANIM_TYPE_THREE:
                if (resetInit) {
                    texts.clear();
                    texts = getStrings(text);
                    fy = textHeight+DensityUtil.dip2px(context,5);
                    resetInit = false;
                }
                mstring = text;
                if (repert_type == REPERT_TYPE_ONE) {
                    if (fa>=1) {
                        isRepert = false;
                    }
                } else {
                    if (fa>=1) {
                        fa = 0;
                    }
                }
                mPaint.setAlpha((int)(256*fa));
                break;
        }
        for(int i=0;i<texts.size();i++){

            canvas.drawText(texts.get(i), fx, fy+(textHeight+DensityUtil.dip2px(context,5))*i, mPaint);
        }
    }

    /**
     * 文本换行处理
     * @param string
     * @return
     */
    private ArrayList<String> getStrings(String string){
        ArrayList<String> strings = new ArrayList<>();
        String data = string;
        while (!TextUtils.isEmpty(data)){
            float mWidth = getContentWidth(data);
            if(mWidth>getWidth()){
                int start = 0;
                int end = data.length();
                int middle = data.length()/2;
                while(!(getContentWidth(data.substring(0,middle))<=getWidth() &&
                        getContentWidth(data.substring(0,middle+1))>getWidth())){
                    if(getContentWidth(data.substring(0,middle))<getWidth()){
                        start = middle;
                        middle = (end+middle)/2;
                    }else{
                        end = middle;
                        middle = (start+middle)/2;
                    }
                }
                strings.add(data.substring(0,middle));
                data = data.substring(middle);
            }else{
                strings.add(data);
                data = null;
            }
        }
        return strings;
    }

    /**
     * 计算出一个空格的宽度
     *
     * @return
     */
    private float getBlacktWidth() {
        String text1 = "en en";
        String text2 = "enen";
        return getContentWidth(text1) - getContentWidth(text2);

    }
    private float getContentWidth(String black) {
        if (black == null || black == "") {
            return 0;
        }

        if (rect == null) {
            rect = new Rect();
        }
        mPaint.getTextBounds(black, 0, black.length(), rect);
        textHeight = getContentHeight();
        return rect.width();
    }

    /**
     * http://blog.csdn.net/u014702653/article/details/51985821
     * 详细解说了
     *
     * @param
     * @return
     */
    private float getContentHeight() {

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        return Math.abs((fontMetrics.bottom - fontMetrics.top)) / 2;
    }


    @Override
    public void run() {
        while (isRepert) {
            try {
                Thread.sleep(10);
                switch (anim_type) {
                    case ANIM_TYPE_ONE:
                        fx -= text_speed;
                        break;
                    case ANIM_TYPE_TWO:
                        fy -= text_speed;
                        break;
                    case ANIM_TYPE_THREE:
                        fa += text_speed/500f;
                        break;
                }

                postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
