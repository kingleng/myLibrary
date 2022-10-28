package com.example.leng.myapplication2.ui.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * @author lincheng
 * @since 2020/12/31
 */
public class NtfDispatchActivity extends Activity {

    private static final String TAG = "NtfDispatchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            dispatchNtf(this,getNtfType());
        }catch (Exception e){

        }finally {
            finish();
        }
    }

    private String getNtfType(){
        if(getIntent()!=null){
//            return getIntent().getStringExtra(NotificationViewHelper.COM_YMM_DRIVER_NTF_ACTION);
        }
        return "";
    }

    private void dispatchNtf(Context context,String type){
        collapseStatusBar(context);
//        switch (type) {
//            case COM_YMM_DRIVER_NOTIFY_ACTION_CARGO:
//                Intent intent1 = ApiManager.getImpl(AccountService.class).isLogin(context) ?
//                        Router.route(context, Uri.parse("ymm://view/subscribed")) : Router.route(context, Uri.parse("ymm://view/login"));
//                if (intent1 != null) {
//                    startActivity(intent1);
//                }
//                YmmLogger.commonLog().page("fixed_notification_bar").elementId("cargoes_new").tap().enqueue();
//                break;
//            case COM_YMM_DRIVER_NOTIFY_ACTION_ORDER_NAV:
//
//                navJump(context);
//                Log.d(TAG, "COM_YMM_DRIVER_NOTIFY_ACTION_ORDER_NAV " + NotificationViewHelper.get().notificationOrderNav);
//                YmmLogger.commonLog().page("fixed_notification_bar").elementId("navi_click").tap().enqueue();
//                break;
//            case COM_YMM_DRIVER_NOTIFY_ACTION_ORDER:
//                Intent intent3 = ApiManager.getImpl(AccountService.class).isLogin(context) ?
//                        Router.route(context, Uri.parse("ymm://view/orders?status=1")) : Router.route(context, Uri.parse("ymm://view/login"));
//                if (intent3 != null) {
//                    startActivity(intent3);
//                }
//
//                YmmLogger.commonLog().page("fixed_notification_bar").elementId("order").tap().enqueue();
//                break;
//            case COM_YMM_DRIVER_NOTIFY_ACTION_CONTENT:
//                Intent intent4;
//                if (!ApiManager.getImpl(AccountService.class).isLogin(context)) {
//                    intent4 = Router.route(context, Uri.parse("ymm://view/login"));
//                } else {
//                    if (NotificationViewHelper.currentShowType == NotificationViewHelper.TYPE_ORDER_NAV
//                            && (NotificationViewHelper.get().notificationOrderNav != null)) {
//                        navJump(context);
//                        Log.d(TAG, "COM_YMM_DRIVER_NOTIFY_ACTION_CONTENT: " + NotificationViewHelper.get().notificationOrderNav.getClickJumpUrl());
//                        if (!NotificationViewHelper.get().notificationOrderNav.getResetNotificationFlag()) {
//                            YmmLogger.commonLog().page("fixed_notification_bar").elementId("order_text").tap().enqueue();
//                        } else {
//                            YmmLogger.commonLog()
//                                    .page("fixed_notification_bar")
//                                    .elementId("default_text")
//                                    .param("default_text", NotificationViewHelper.get().notificationOrderNav.getContent())
//                                    .tap().enqueue();
//                        }
//                        return;
//                    } else if (NotificationViewHelper.currentShowType == NotificationViewHelper.TYPE_CARGO
//                            && (NotificationViewHelper.get().notificationCargo != null)) {
//                        intent4 = Router.route(context, Uri.parse("ymm://view/subscribed"));
//                        if (!NotificationViewHelper.get().notificationCargo.getResetNotificationFlag()) {
//                            YmmLogger.commonLog().page("fixed_notification_bar").elementId("cargoes_new_text").tap().enqueue();
//                        } else {
//                            YmmLogger.commonLog()
//                                    .page("fixed_notification_bar")
//                                    .elementId("default_text")
//                                    .param("default_text", NotificationViewHelper.get().notificationCargo.getContent())
//                                    .tap().enqueue();
//                        }
//                    } else {
//                        intent4 = Router.route(context, Uri.parse("ymm://view/subscribed"));
//                        YmmLogger.commonLog().page("fixed_notification_bar").elementId("default_text").tap().enqueue();
//                    }
//                }
//                if (intent4 != null) {
//                    startActivity(intent4);
//                }
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 收起通知栏
     */
    private static void collapseStatusBar(Context context) {
        try {
            @SuppressLint("WrongConstant") Object statusBarManager = context.getSystemService("statusbar");
            if (statusBarManager == null) {
                return;
            }

            Method collapse;

            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

//    private void navJump(Context context) {
//        Uri uri = Uri.parse(getNavUrl());
//        uri = uri.buildUpon().appendQueryParameter("needlogin","1").build();
//        Intent intent2 = Router.route(context, uri);
//        if (intent2 != null) {
//            startActivity(intent2);
//        }
//    }

//    private String getNavUrl() {
//        // TODO: 2020/4/22
//        String url = "wlqq://activity/truck_map_main?source=notify";
//        if (NotificationViewHelper.get().notificationOrderNav != null ) {
//            if (!TextUtils.isEmpty(NotificationViewHelper.get().notificationOrderNav.getClickJumpUrl())) {
//                url = NotificationViewHelper.get().notificationOrderNav.getClickJumpUrl();
//            }
//        }
//        return url;
//    }
}