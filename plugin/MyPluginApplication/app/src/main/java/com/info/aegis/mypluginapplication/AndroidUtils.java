package com.info.aegis.mypluginapplication;

import android.os.Environment;

import java.io.File;

/**
 * Created by leng on 2020/3/13.
 */
public class AndroidUtils {
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
