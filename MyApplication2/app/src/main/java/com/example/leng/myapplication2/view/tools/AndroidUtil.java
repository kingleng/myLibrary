package com.example.leng.myapplication2.view.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by 17092234 on 2017/9/26.
 */

public class AndroidUtil {

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();

        return width;

    }

    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

//        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return height;

    }

    public static int getTextWidth(Context context, String text, int textSize){
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return (int)paint.measureText(text);
    }

    public static Bitmap loadBitmapFromViewBySystem(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }

    private void changeColor(boolean isWhite, Activity mActivity){
        if(!isWhite){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //android6.0以后可以对状态栏文字颜色和图标进行修改 (黑色)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //android6.0以后可以对状态栏文字颜色和图标进行修改 设置状态栏文字颜色及图标为浅色 (白色)
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }

        }
    }

    private void setPrice(TextView textView,String mPrice, Activity mActivity){
//        String mPrice = itemData.price;
        if(!TextUtils.isEmpty(mPrice)){
            if (!mPrice.contains(".")) {
                mPrice = mPrice + ".00";
            }
            String[] ss = mPrice.split("\\.");
            SpannableString spanString = new SpannableString("¥" + ss[0] + "." + ss[1]);

            AbsoluteSizeSpan span = new AbsoluteSizeSpan(DensityUtil.dip2px(mActivity, 11.5f));
            AbsoluteSizeSpan span2 = new AbsoluteSizeSpan(DensityUtil.sp2px(mActivity, 11.5f));
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(span2, 1 + ss[0].length(), 2 + ss[0].length() + ss[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            singlePrice.setText(spanString);

            if(mPrice.endsWith(".00")){
                textView.setText(spanString.subSequence(0,spanString.length()-3));
            }else{
                textView.setText(spanString);
            }
        }
    }

}
