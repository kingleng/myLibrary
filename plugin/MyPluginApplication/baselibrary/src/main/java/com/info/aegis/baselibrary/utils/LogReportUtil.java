package com.info.aegis.baselibrary.utils;

import android.content.Context;

import com.info.aegis.baselibrary.SharePreHelper;
import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;
import com.wenming.library.save.imp.LogWriter;
import com.wenming.library.upload.email.EmailReporter;

/**
 * 异常日志发送邮件
 * Created by gallon on 2018/1/24.
 */

public class LogReportUtil {

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        if (AppUtils.isDebug()) return;

        LogReport.getInstance()
                .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                .setLogDir(context, "sdcard/" + "CrashLog" + "/")//定义路径为：sdcard/[app name]/
                .setWifiOnly(false)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setLogSaver(new CrashWriter(context))//支持自定义保存崩溃信息的样式
                //.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(context);
        LogWriter.writeLog("DevicesID", SharePreHelper.getDeviceId());
        initEmailReporter(context);
    }

    /**
     * 使用EMAIL发送日志
     * @param context
     */
    private static void initEmailReporter(Context context) {
        EmailReporter email = new EmailReporter(context);
        email.setReceiver("android@aegis-data.cn");//收件人
//        email.setReceiver("yinjialun@aegis-data.cn");//收件人
        email.setSender("gallonyin@163.com");//发送人邮箱
        email.setSendPassword("qq233333");//邮箱的客户端授权码，注意不是邮箱密码
        email.setSMTPHost("smtp.163.com");//SMTP地址
        email.setPort("465");//SMTP 端口
        LogReport.getInstance().setUploadType(email);
        LogReport.getInstance().upload(context);
    }
}
