package com.example.leng.myapplication2.ui.tools;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by leng on 2017/2/28.
 */

public class WHUtil {

    public static int[] getWidth(Context context){
        int[] data = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        data[0] = width;
        data[1] = height;
        return data;
    }
}
