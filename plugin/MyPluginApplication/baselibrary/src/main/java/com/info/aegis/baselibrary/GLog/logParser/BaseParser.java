package com.info.aegis.baselibrary.GLog.logParser;

import android.util.Log;

import com.info.aegis.baselibrary.GLog.GLog;

/**
 * 基本日志打印，可以打印大String
 */
public class BaseParser {

    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        //不超过四千个字符
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case GLog.V:
                Log.v(tag, sub);
                break;
            case GLog.D:
                Log.d(tag, sub);
                break;
            case GLog.I:
                Log.i(tag, sub);
                break;
            case GLog.W:
                Log.w(tag, sub);
                break;
            case GLog.E:
                Log.e(tag, sub);
                break;
            case GLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }

}
