//package com.example.leng.myapplication2.ui.notification;
//
//import android.os.Build;
//import android.text.TextUtils;
//
//import com.ymm.biz.configcenter.service.ConfigCenterService;
//import com.ymm.lib.componentcore.ApiManager;
//import com.ymm.lib.schedulers.impl.Action;
//import com.ymm.lib.schedulers.impl.MBSchedulers;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by lincheng on 2020/12/3.
// */
//public class OngoingNotificationSwitch {
//
//    private static boolean isFetchNotificationNewStrategy;
//    private static boolean notificationNewStrategyResult;
//
//    private static boolean isRemoved;
//    private static Action removeAction;
//
//    //0929版本常驻通知栏新策略修改
//    public static boolean isNotificationEnable(){
//        if (!isFetchNotificationNewStrategy) {
//            notificationNewStrategyResult = getNotificationEnable();
//            isFetchNotificationNewStrategy = true;
//        }
//        return notificationNewStrategyResult;
//    }
//
//    private static boolean getNotificationEnable(){
//        int willOpenONew = ApiManager.getImpl(ConfigCenterService.class).getConfig("other","0929_sticky_notification_open",0);
//        String disablesStr = ApiManager.getImpl(ConfigCenterService.class).getConfig("other","0429_notification_disable_brand_sdk","$");
////        List<String> disables = TextUtils.isEmpty(disablesStr) ? Collections.emptyList() : Arrays.asList(disablesStr.split(","));
//        List<String> disables;
//        if(TextUtils.isEmpty(disablesStr)){
//            disables =  Collections.emptyList();
//        }else{
//            disables =  Arrays.asList(disablesStr.split(","));
//        }
//
//        //命中品牌黑名单 且 命中sdk版本黑名单
//        return willOpenONew == 1 && !disables.contains(Build.BRAND.toLowerCase() + '$' + Build.VERSION.SDK_INT);
//    }
//
//    public static boolean isNotificationRemoved(){
//        return isRemoved;
//    }
//
//    public synchronized static void setNotificationRemoved(){
//        if(isRemoved){
//            MBSchedulers.background().cancel(removeAction);
//        }
//        isRemoved = true;
//        //移除持续时长（秒）
//        long removeInterval = ApiManager.getImpl(ConfigCenterService.class).getConfig("other","1216_sticky_notification_remove_interval",300);
//        removeAction = new Action(){
//
//            @Override
//            public void action() {
//                isRemoved = false;
//            }
//        };
//        MBSchedulers.background().schedule(removeAction,removeInterval, TimeUnit.SECONDS);
//    }
//}
