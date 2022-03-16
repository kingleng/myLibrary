package com.cpsdna.app.mykotlinapplication.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.cpsdna.app.mykotlinapplication.TopApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 作者：jksfood on 2017/9/1 15:30
 */

public class UtilMethod {

    /**
     * 初始化
     */
    public static void init() {

    }

    /**
     * 生成设备唯一标识：IMEI、AndroidId、macAddress 三者拼接再 MD5
     * @return
     */
    public static String generateUniqueDeviceId(){

//        Context context = TopApplication.getTopApplication();
//        String imei = "";
//        String androidId = "";
////        String macAddress = "";
//
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
//        if (telephonyManager != null) {
//            imei = telephonyManager.getDeviceId();
//        }
//        Log.e("asd","imei = "+imei);
//        ContentResolver contentResolver = context.getContentResolver();
//        if (contentResolver != null) {
//            androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
//        }
//        Log.e("asd","androidId = "+androidId);
////        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
////        if (wifiManager != null) {
////            macAddress = wifiManager.getConnectionInfo().getMacAddress();
////        }
//
//        StringBuilder longIdBuilder = new StringBuilder();
//        if (imei != null) {
//            longIdBuilder.append(imei);
//        }
//        if (androidId != null) {
//            longIdBuilder.append(androidId);
//        }
//
//        String deviceId = md5(longIdBuilder.toString());
//        Log.e("asd","deviceId = "+deviceId);
////        if (macAddress != null) {
////            longIdBuilder.append(macAddress);
////        }
//        return deviceId;
        return "9606fee22d5e9cbdaca559684a5d3746";
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 随机主动打招呼
     * @return
     */
    public static String randomResponse4WakeUp(){
        String[] stringArray = {"在呢","请说"};
        Random random = new Random();
        int m = random.nextInt(stringArray.length);
        return stringArray[m];
    }

    /**
     * 将毫秒转换为 00:00:00时间格式
     *
     * @return
     */
    public static String formatMillTool(long duration) {
        String time = null;
        if (duration != 0) {
//            duration /= 1000;
            if (duration < 60) {
//                time = "00:00:" + transform(duration);
                time = "00:" + transform(duration);
            } else if (duration >= 60 && duration < 60 * 60) {
                long min = duration / 60;
//                time = "00:" + transform(min) + ":" + transform(duration % 60);
                time =  transform(min) + ":" + transform(duration % 60);
            } else {
                long hour = duration / 60 / 60;
                long min = 0;
                long sec = 0;
                if (duration - 3600 * hour >= 60) {
                    min = (duration - 3600 * hour) / 60;
                    sec = (duration - 3600 * hour) % 60;
                } else if (duration - 3600 * hour < 60) {
                    min = 00;
                    sec = duration - 3600 * hour;
                }
                time = transform(hour) + ":" + transform(min) + ":" + transform(sec);
            }
        }
        if (time == null) {
//            time = "00:00:00";
            time = "00:00";
        }
        return time;
    }

    /**
     * 工具
     *
     * @param i
     * @return
     */
    private static String transform(long i) {
        if (i >= 10) {
            return i + "";
        } else if (i < 10) {
            return "0" + i;
        }
        return null;
    }

}
