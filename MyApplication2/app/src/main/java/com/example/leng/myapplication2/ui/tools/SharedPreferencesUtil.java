package com.example.leng.myapplication2.ui.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by leng on 2019/5/13.
 */
public class SharedPreferencesUtil {

    public static void setKey(Context context, String magazine){
        SharedPreferences sp = context.getSharedPreferences("magazine_cache",Context.MODE_PRIVATE);
        sp.edit().putString("magazine",magazine).apply();
    }

    public static void getKey(Context context, String magazine){
        SharedPreferences sp = context.getSharedPreferences("magazine_cache",Context.MODE_PRIVATE);
        sp.getString("magazine","");
    }
}
