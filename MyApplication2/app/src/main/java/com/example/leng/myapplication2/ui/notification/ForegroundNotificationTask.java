//package com.example.leng.myapplication2.ui.notification;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.IBinder;
//import android.text.TextUtils;
//
//import com.ymm.biz.cargo.impl.PluginCargoContextUtil;
//import com.ymm.lib.commonbusiness.ymmbase.util.ActivityStack;
//import com.ymm.lib.commonbusiness.ymmbase.util.ContextUtil;
//import com.ymm.lib.schedulers.impl.Action;
//import com.ymm.lib.schedulers.impl.MBSchedulers;
//
//import org.json.JSONObject;
//
///**
// * @author luke.yujb
// * @date 18/10/16
// */
//public class ForegroundNotificationTask implements OngoingNotificationHelper.IOngoingNotification {
//
//    private long lastGetTime = 0;
//    private ActivityStack.ShowStateCallback showStateCallback;
//
//    public void init() {
//        OngoingNotificationHelper.setImpl(this);
//        OngoingNotificationHelper.start();
//    }
//
//    private void dealNotification(JSONObject jsonObject) {
//        String category = jsonObject.optString("category");
//        if ("cargo".equals(category)) {
//            if (System.currentTimeMillis() - lastGetTime < 10 * 1000) {
//                return;
//            }
//            lastGetTime = System.currentTimeMillis();
////            MBSchedulers.background().schedule(new Action() {
////                @Override
////                public void action() {
////                    NotificationApi.getSubscribeList()
////                            .enqueue(new SilentCallback<SubscribeLineBean>() {
////                                @Override
////                                public void onBizSuccess(SubscribeLineBean subscribeLineBean) {
////                                    MBSchedulers.background().schedule(new Action() {
////                                        @Override
////                                        public void action() {
////                                            int cargoCount = 0;
////                                            NotificationData data = new NotificationData();
////                                            data.setType(NotificationData.NOTIFICATION_TYPE_CARGO);
////                                            data.setCreateTime(System.currentTimeMillis());
////                                            if (subscribeLineBean != null && subscribeLineBean.getList() != null) {
////                                                SubscribeLine currentLine = null;
////                                                List<SubscribeLine> lines = subscribeLineBean.getList();
////                                                for (SubscribeLine line : lines) {
////                                                    cargoCount += line.getSubCount();
////                                                    if (currentLine == null && line.getSubCount() > 0) {
////                                                        currentLine = line;
////                                                    }
////                                                }
////                                                if (currentLine == null) {
////                                                    NotificationViewHelper.get().updateMessageCount(NotificationData.NOTIFICATION_TYPE_CARGO, cargoCount);
////                                                    return;
////                                                }
////                                                data.setCargoStartCity(currentLine.getStarts());
////                                                data.setCargoDestinationCity(currentLine.getEnds());
////                                                SpannableStringBuilder builder = SubscribeUtils.formatLine(ContextUtil.get(), currentLine.getStarts(), currentLine.getEnds());
////                                                data.setContent("[新货源]" + builder.toString() + " 有" + currentLine.getSubCount() + "票好货源，快抢！");
////                                                NotificationViewHelper.get().newsIncoming(data);
////                                            }
////                                            NotificationViewHelper.get().updateMessageCount(NotificationData.NOTIFICATION_TYPE_CARGO, cargoCount);
////                                        }
////                                    });
////                                }
////                            });
////                }
////            });
//        } else if ("order".equals(category)) {
//            int orderCount = jsonObject.optInt("transportCount");
//            NotificationViewHelper.get().updateMessageCount(NotificationData.NOTIFICATION_TYPE_ORDER, orderCount);
//        } else if ("order-navigate".equals(category)) {
//            String message = jsonObject.optString("message");
//            NotificationData data = new NotificationData();
//            data.setType(NotificationData.NOTIFICATION_TYPE_ORDER_NAV);
//            if (TextUtils.isEmpty(message)) {
//                data.setResetNotificationFlag(true);
//            } else {
//                long ts = jsonObject.optLong("ts");
//                String orderId = jsonObject.optString("orderId");
//                long expireAt = jsonObject.optLong("expireAt");
//                boolean navigable = jsonObject.optBoolean("navigable");
//                String clickJumpUrl = jsonObject.optString("url");
//                data.setTs(ts);
//                data.setOrderId(orderId);
//                data.setExpireAt(expireAt);
//                data.setContent(message);
//                data.setNavigable(navigable);
//                data.setClickJumpUrl(clickJumpUrl);
//                data.setCreateTime(System.currentTimeMillis());
//            }
//            NotificationViewHelper.get().newsIncoming(data);
//        }
//    }
//
//    public void registerListenerAndStartService(){
////        if (!OngoingNotificationStatusCookie.getNotifyIsOpen()) {
////            return;
////        }
//        final Intent intent = new Intent(ContextUtil.get(), ForegroundService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if(OngoingNotificationSwitch.isNotificationEnable()){
//                startNotificationService(intent);
//            }
//        } else {
//            ContextUtil.get().startService(intent);
//        }
//
//        //增加切换后台展示逻辑
//        if(showStateCallback==null){
//            showStateCallback = new ActivityStack.ShowStateCallback() {
//                @Override
//                public void onShowStateChanged(boolean isFore) {
//                    if (!isFore) {
////                        if (OngoingNotificationSwitch.isNotificationEnable() && isSubscribeOn() && OngoingNotificationStatusCookie.getNotifyIsOpen()) {
//                        if (OngoingNotificationSwitch.isNotificationEnable() && isSubscribeOn()) {
//                            //1216版本增加移除间隔
//                            if(OngoingNotificationSwitch.isNotificationRemoved()){
//                                return;
//                            }
//                            if(!ForegroundService.isServiceRunning()){
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    startNotificationService(intent);
//                                } else {
//                                    ContextUtil.get().startService(intent);
//                                }
//                            }
//                        }
//                    }
//                }
//            };
//        }
////        ActivityStack.getInstance().addShowStateCallbackOnBackgroundThread(showStateCallback);
////
////        RemoteDataHandleListener aliveHandler = new RemoteDataHandleListener() {
////            @Override
////            public void handleResponse(KCResponse kcResponse) {
////                if (kcResponse != null) {
////                    String content = kcResponse.getContent();
////                    Log.d("samuel", content);
////                    try {
////                        JSONObject jsonObject = new JSONObject(content);
////                        dealNotification(jsonObject);
////                    } catch (JSONException e) {
////                    }
////                }
////            }
////        };
////        EverSocket.INSTANCE.registerRemoteDataHandler("alive_notification", aliveHandler);
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(NotificationViewHelper.COM_YMM_DRIVER_NOTIFY_ACTION_REMOVE);
//        ContextUtil.get().registerReceiver(new NtfDispatchReceiver(), intentFilter);
//    }
//
//    //取消广播监听、长连接收、停止service
//    public void unregisterListenerAndStopService(){
//        try{
//            if(showStateCallback!=null){
//                ActivityStack.getInstance().removeShowStateCallback(showStateCallback);
//            }
////            EverSocket.INSTANCE.unRegisterRemoteDataHandler("alive_notification");
//            Intent intent = new Intent(ContextUtil.get(), ForegroundService.class);
//            ContextUtil.get().stopService(intent);
//        }catch (Exception e){
//
//        }
//        NotificationViewHelper.get().removeNotify();
//    }
//
//    @Override
//    public boolean isServiceStarted() {
//        return ForegroundService.isServiceRunning();
//    }
//
//    private static final String SUBSCRIBE_INFO = "subscribe_info"; // App is using this preference.
//    private static final String KEY_RECEIVE_GOODS_NOTIFY = "receive_goods_notify"; // App is using this preference.
//    private static final String KEY_RECEIVE_GOODS_SHORT_NOTIFY = "receive_goods_short_notify"; // App is using this preference.
//
//    //0917版本增加听单或者货源听单开关
//    private static boolean isSubscribeOn(){
//        SharedPreferences settings = ContextUtil.get().getSharedPreferences(SUBSCRIBE_INFO, Context.MODE_PRIVATE);
//        return settings.getBoolean(KEY_RECEIVE_GOODS_NOTIFY, false) || settings.getBoolean(KEY_RECEIVE_GOODS_SHORT_NOTIFY, false);
//    }
//
//    //0929版本通过bind方式启动
//    public static void startNotificationService(final Intent intent){
//        MBSchedulers.background().schedule(new Action() {
//            @Override
//            public void action() {
//                try{
//
//                }catch (Exception e){
//
//                }
//            }
//        });
//    }
//}
