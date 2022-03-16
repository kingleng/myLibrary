package com.info.aegis.baselibrary.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by leng on 2020/1/10.
 */
public class SystemUtils {

    /**
     * 获取系统内存大小
     * @return
     */
    public static String getSysteTotalMemorySize(Context context){
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo) ;
        long memSize = memoryInfo.totalMem ;
        //字符类型转换
        String availMemStr = formateFileSize(memSize);
        return availMemStr ;
    }

    private static String formateFileSize(long memSize){
        if(memSize>1024){
            memSize = memSize/1024;
        }else{
            return memSize+"b";
        }
        if(memSize>1024){
            memSize = memSize/1024;
        }else{
            return memSize+"kb";
        }
        if(memSize>1024){
            memSize = memSize/1024;
        }else{
            return memSize+"mb";
        }
        return memSize+"gb";
    }

}
