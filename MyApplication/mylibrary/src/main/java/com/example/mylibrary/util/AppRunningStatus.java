package com.example.mylibrary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by leng on 2017/5/5.
 * 判断app是否前台运行
 */

public class AppRunningStatus {


    public static boolean isAppRunningForeground(Context context) {
        ActivityManager var1 = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        try {
            List taskList = var1.getRunningTasks(1);
            if(taskList != null && taskList.size() >= 1) {
                boolean foreground = context.getPackageName().equalsIgnoreCase(((ActivityManager.RunningTaskInfo)taskList.get(0)).baseActivity.getPackageName());
                Log.d("utils", "app running in foregroud：" + foreground);
                return foreground;
            } else {
                return false;
            }
        } catch (SecurityException var4) {
            Log.d("utils", "Apk doesn\'t hold GET_TASKS permission");
            var4.printStackTrace();
            return false;
        }
    }

}
