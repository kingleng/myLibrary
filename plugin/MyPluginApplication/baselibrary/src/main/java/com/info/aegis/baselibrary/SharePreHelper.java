package com.info.aegis.baselibrary;

import android.text.TextUtils;

import com.info.aegis.baselibrary.utils.TestSaveUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * 作者：lj on 2020/1/2
 */

public class SharePreHelper {

    /**
     * 设置设备id
     *
     * @param deviceId
     */
    public static void setDeviceId(String deviceId) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("deviceId", deviceId).apply();
        TestSaveUtils.wtiteFileFromBytes(deviceId);
    }

    public static String getDeviceId() {
        String deviceId = TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("deviceId", "");
        if(TextUtils.isEmpty(deviceId)){
            deviceId = TestSaveUtils.getFileData();
            TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("deviceId", deviceId).apply();
        }
        return deviceId;
    }


    /**
     * 设置eventId
     *
     * @param eventId
     */
    public static void setEventId(String eventId) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("eventId", eventId).apply();
    }

    /**
     * 获取eventId
     *
     * @return
     */
    public static String getEventId() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("eventId", "");

    }

    /**
     * 设置机构名称
     */
    public static void setEventName(String eventName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("eventName", eventName).apply();
    }

    /**
     * 获取机构名称
     */
    public static String getEventName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("eventName", "");
    }

    /**
     * 设置apk版本
     *
     * @param apkVersion
     */
    public static void setApkVersion(String apkVersion) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("apkVersion", apkVersion).apply();
    }

    public static String getApkVersion() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("apkVersion", "");
    }

    /**
     * 设置开发环境
     *
     * @param environment
     */
    public static void setEnvironment(String environment) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("environment", environment).apply();
    }

    public static String getEnvironment() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("environment", "");
    }

}
