package com.example.leng.myapplication.view.tools;

import android.util.Log;

/**
 * Created by leng on 2017/3/2.
 */

public class MyLog{

    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static Boolean isDebug = true;

    public static void e(String tag,String msg){
        if(isDebug){
            Log.e(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if(isDebug){
            Log.i(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(isDebug){
            Log.d(tag,msg);
        }
    }

    public static void v(String tag,String msg){
        if(isDebug){
            Log.v(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if(isDebug){
            Log.w(tag,msg);
        }
    }

}
