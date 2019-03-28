package com.example.leng.myapplication.view.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Desc :
 * Author : 13075692
 * Date :  2017/2/7
 * Time : 16:27
 */

public class TranslucentBarUtil {

    public static final int COLOR_BLACK = -16777216;
    public static final int COLOR_TRANSLUCENT = 0;
    public static final int COLOR_WHITE = -1;

    public static int mDefaultColor = -1000;

    public static void setTranslucentBar(Activity activity, boolean translucent) {
        if (Build.VERSION.SDK_INT >= 21 && isSupportTranslucentBar() && activity != null) {
            Window window = activity.getWindow();
            if (mDefaultColor == -1000) {
                mDefaultColor = getDefaultBarColor(activity);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (translucent) {
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.setStatusBarColor(mDefaultColor);
            }
        }
    }

    public static void setTranslucentBar2(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21 && isSupportTranslucentBar() && activity != null) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= 23) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @IntRange(from = 0, to = 75)
    public static int getStatusBarOffsetPx(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= 21 && isSupportTranslucentBar()) {
            Context appContext = context.getApplicationContext();
            int resourceId =
                    appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = appContext.getResources().getDimensionPixelSize(resourceId);
            }
            Log.e("asd","TranslucentBarUtil==" + result);
            if (result == 0) {
                result = 72;
            }
        }
        return result;
    }

    @TargetApi(21)
    public static int getDefaultBarColor(Activity activity) {
       int defaultColor = Color.BLACK;
        int color = activity.getWindow().getStatusBarColor();
        if(color == COLOR_BLACK) {
            Log.e("asd","COLOR_BLACK");
            defaultColor = Color.BLACK;
        } else if(color == COLOR_WHITE) {
            Log.e("asd","COLOR_WHITE");
            defaultColor = Color.WHITE;
        } else if(color == COLOR_TRANSLUCENT) {
            Log.e("asd","COLOR_TRANSLUCENT");
            defaultColor = COLOR_TRANSLUCENT;
        }
        return defaultColor;
    }

    private static boolean isSupportTranslucentBar() {
        boolean support = true;
        if ("asus".equalsIgnoreCase(Build.MANUFACTURER)) {
            support = false;
        }
        return support;
    }
}
