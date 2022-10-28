//package com.example.leng.myapplication2.ui.notification;
//
//import static com.example.leng.myapplication2.ui.notification.ForegroundService.FOREGROUND_ID;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v4.app.NotificationCompat;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.RemoteViews;
//
//import com.example.leng.myapplication2.R;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by ming.weim@ymm56.com on 2018/10/8.
// */
//
//public class NotificationViewHelper {
//    public final int ORDER_LAYOUT = 1;
//    public final int NAVIGATE_LAYOUT = 2;
//    public final int CARGO_LAYOUT = 3;
//    public final int NOTIFICATION_CONTENT = 4;
//    public final int NOTIFICATION_REMOVE = 5;
//
//    public static NotificationViewHelper instance;
//    public volatile NotificationData notificationCargo;
//    public volatile NotificationData notificationOrderNav;
//    public volatile RemoteViews mRemoteViews;
//    public static final int TYPE_CARGO = 1;
//    public static final int TYPE_ORDER_NAV = 3;
//    public static final int TYPE_DEFAULT = 0;
//    public static int currentShowType = TYPE_DEFAULT;
//    private static final int DEFAULT_CARGO_TIME = 1 * 60 * 1000; // 默认
//    private static final int DEFAULT_LOOP_CYCLE_TIME = 1 * 60 * 1000;
//    private Notification notification;
//    private int orderCount, cargoCount;
//    private static final String TAG = "Notificationrrrr";
//
//    private final NotificationData defaultNotificationDataCargo;
//    private final NotificationData defaultNotificationDataNav;
//
//    private volatile boolean isNewCargoNotificationShowing = false;
//    private volatile boolean isNewOrderNotificationShowing = false;
//    private final long defaultOrderNavContentShowTime = 6 * 60 * 1000;
//
//    private static final String GROUP_NAME = "other";
//
//    private int count;
//
//    public synchronized static NotificationViewHelper get() {
//        if (instance == null) {
//            instance = new NotificationViewHelper();
//        }
//        return instance;
//    }
//
//    private NotificationViewHelper() {
//        defaultNotificationDataCargo = new NotificationData();
//        defaultNotificationDataCargo.setType(NotificationData.NOTIFICATION_TYPE_ORDER);
//        defaultNotificationDataCargo.setContent(getDefaultCargoContent());
//        defaultNotificationDataCargo.setResetNotificationFlag(true);
//
//        defaultNotificationDataNav = new NotificationData();
//        defaultNotificationDataNav.setType(NotificationData.NOTIFICATION_TYPE_ORDER_NAV);
//        defaultNotificationDataNav.setContent(getDefaultOrderContent());
//        defaultNotificationDataNav.setResetNotificationFlag(true);
//
//        notificationOrderNav = defaultNotificationDataNav;
//        notificationCargo = defaultNotificationDataCargo;
//
//        Log.d(TAG, "NotificationViewHelper: loopControl start");
//        loopControl();
//    }
//
//    @NonNull
//    public RemoteViews getNormalView() {
//        if(needResetRemoteViewIfCountOverLimit(100) || mRemoteViews==null){
//            mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_normal_layout);
//            mRemoteViews.setOnClickPendingIntent(R.id.order_layout, getPendingIntent(ContextUtil.get(), ORDER_LAYOUT));
//            mRemoteViews.setOnClickPendingIntent(R.id.navigate_layout, getPendingIntent(ContextUtil.get(), NAVIGATE_LAYOUT));
//            mRemoteViews.setOnClickPendingIntent(R.id.cargo_layout, getPendingIntent(ContextUtil.get(), CARGO_LAYOUT));
//            mRemoteViews.setOnClickPendingIntent(R.id.ll_notification, getPendingIntent(ContextUtil.get(), NOTIFICATION_CONTENT));
//            mRemoteViews.setOnClickPendingIntent(R.id.notification_close, getPendingIntent(ContextUtil.get(), NOTIFICATION_REMOVE));
//            initNotification(ContextUtil.get(),mRemoteViews,true);
//        }
//        return mRemoteViews;
//    }
//
//    @NonNull
//    public Notification initNotification(Context context, @NonNull RemoteViews contentView, boolean needReset) {
//        if(needReset || notification==null){
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NtfConstants.getDefaultChannelId(context))
//                    .setSmallIcon(R.mipmap.icon_driver_app)
//                    .setContentTitle("运满满")
//                    .setColor(Color.RED)
//                    .setCustomContentView(contentView)
//                    .setCustomBigContentView(contentView)
//                    .setOnlyAlertOnce(true)
//                    .setOngoing(true)
//                    .setAutoCancel(false)
//                    .setWhen(System.currentTimeMillis());
//            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
//            builder.setContentIntent(contentIntent);
//            notification = builder.build();
//        }
//        return notification;
//    }
//
//    private boolean needResetRemoteViewIfCountOverLimit(int limit){
//            count++;
//            if(count>limit){
//                count=0;
//                return true;
//            }else{
//                return false;
//            }
//    }
//
////    public void newsIncoming(final
////                             NotificationData data) {
////        MBSchedulers.background().schedule(new Action() {
////            @Override
////            public void action() {
////                if (data != null) {
////                    switch (data.getType()) {
////                        // 插播 优先级最高
////                        case NotificationData.NOTIFICATION_TYPE_CARGO:
////                            Log.d(TAG, "newsIncoming: NOTIFICATION_TYPE_CARGO");
////                            notificationCargo = data;
////                            showFixedTimeCargoContent();
////                            break;
////                        case NotificationData.NOTIFICATION_TYPE_ORDER:
////                            Log.d(TAG, "newsIncoming: NOTIFICATION_TYPE_ORDER");
////                            updateView(data);
////                            break;
////                        case NotificationData.NOTIFICATION_TYPE_ORDER_NAV:
////                            Log.d(TAG, "newsIncoming: NOTIFICATION_TYPE_ORDER_NAV");
////                            if (data.getResetNotificationFlag()) {
////                                resetOrderNavContent();
////                            } else {
////                                notificationOrderNav = data;
////                                showFixedTimeOrderNavContent();
////                            }
////                            break;
////                        default:
////                            break;
////                    }
////                }
////            }
////        });
////    }
//
//    private void showFixedTimeOrderNavContent() {
//        currentShowType = TYPE_ORDER_NAV;
//        isNewOrderNotificationShowing = true;
//        updateView(notificationOrderNav);
//        startNewOrderNavTimer();
//    }
//
//    private void showFixedTimeCargoContent() {
//        currentShowType = TYPE_CARGO;
//        isNewCargoNotificationShowing = true;
//        updateView(notificationCargo);
//        startNewCargoTimer();
//    }
//
//    private void loopControl() {
//        if (notificationOrderNav != null && notificationCargo != null) {
//            startTimerForOrderNav();
//        }
//    }
//
//    public void updateMessageCount(final String type, final int messageCount) {
////        MBSchedulers.single().schedule(new Action() {
////            @Override
////            public void action() {
////                updateMessageCountView(type, messageCount);
////            }
////        });
//    }
//
//    private void updateMessageCountView(String type, int count) {
//        getNormalView();
//        if (mRemoteViews == null) {
//            return;
//        }
//        switch (type) {
//            case NotificationData.NOTIFICATION_TYPE_CARGO:
//                cargoCount = count;
//                break;
//            case NotificationData.NOTIFICATION_TYPE_ORDER:
//                orderCount = count;
//                break;
//            default:
//                break;
//        }
//        setCargoCount();
//        setOrderCount();
//
//        NotificationData data;
//        switch (currentShowType) {
//            case TYPE_CARGO:
//                if (notificationCargo != null) {
//                    data = notificationCargo;
//                } else {
//                    data = null;
//                    currentShowType = TYPE_DEFAULT;
//                }
//                break;
//            case TYPE_ORDER_NAV:
//                if (notificationOrderNav != null) {
//                    data = notificationOrderNav;
//                } else {
//                    if (notificationCargo != null) {
//                        data = notificationCargo;
//                        currentShowType = TYPE_CARGO;
//                    } else {
//                        data = null;
//                        currentShowType = TYPE_DEFAULT;
//                    }
//                }
//                break;
//            default:
//                data = null;
//                break;
//        }
//        updateContent(data);
//        updateNotify();
//    }
//
//    private void setOrderCount() {
//        if (mRemoteViews == null) {
//            return;
//        }
////        if (orderCount > 0) {
////            mRemoteViews.setTextViewText(R.id.order_count, String.valueOf(orderCount));
////            mRemoteViews.setViewVisibility(R.id.order_count, View.VISIBLE);
////
////            if (orderCount > 99) {
////                mRemoteViews.setTextViewText(R.id.order_count, "99+");
////            }
////        } else {
////            mRemoteViews.setViewVisibility(R.id.order_count, View.INVISIBLE);
////        }
//    }
//
//    private void setCargoCount() {
//        if (mRemoteViews == null) {
//            return;
//        }
////        if (cargoCount > 0) {
////            mRemoteViews.setTextViewText(R.id.cargo_count, String.valueOf(cargoCount));
////            mRemoteViews.setViewVisibility(R.id.cargo_count, View.VISIBLE);
////            if (cargoCount > 99) {
////                mRemoteViews.setTextViewText(R.id.cargo_count, "99+");
////            }
////        } else {
////            notificationCargo = defaultNotificationDataCargo;
////            mRemoteViews.setViewVisibility(R.id.cargo_count, View.INVISIBLE);
////        }
//    }
//
//    private void updateNotify() {
////        MBSchedulers.single().schedule(new Action() {
////            @Override
////            public void action() {
////                updateNotifyInner();
////            }
////        });
//    }
//
////    private void updateNotifyInner(){
////        //0917版本增加关闭常驻通知栏
//////        if(!OngoingNotificationSwitch.isNotificationEnable() || !OngoingNotificationStatusCookie.getNotifyIsOpen()){
//////            return;
//////        }
////        //1216版本增加移除间隔
////        if(OngoingNotificationSwitch.isNotificationRemoved()){
////            return;
////        }
////        try {
////            NotificationManager notificationManager = (NotificationManager) ContextUtil.get().getSystemService(Context.NOTIFICATION_SERVICE);
////            notificationManager.notify(FOREGROUND_ID, initNotification(ContextUtil.get(), getNormalView(),false));
////
////            //0917版本增加同时开启前台服务
////            if(OngoingNotificationSwitch.isNotificationEnable()){
////                if(!ForegroundService.isServiceRunning()){
////                    Intent intent = new Intent(ContextUtil.get(), ForegroundService.class);
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                            ForegroundNotificationTask.startNotificationService(intent);
////                        } else {
////                            ContextUtil.get().startService(intent);
////                        }
////                    }
////            }
////        } catch (Exception e) {
////            YmmLogger.commonLog().page("long_notification").elementId("create").view().param("error", e.getMessage()).enqueue();
////        }
////    }
//
//    public void removeNotify(){
//        try {
//            NotificationManager notificationManager = (NotificationManager) ContextUtil.get().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(FOREGROUND_ID);
//        } catch (Exception e) {
//            YmmLogger.commonLog().page("long_notification").elementId("remove").view().param("error", e.getMessage()).enqueue();
//        }
//    }
//
//    private void updateContent(NotificationData data) {
//        if (data == null) {
//            return;
//        }
//        if (mRemoteViews == null) {
//            return;
//        }
//        if (data.getResetNotificationFlag()) {
//            mRemoteViews.setTextViewText(R.id.notification_time, " ");
//            mRemoteViews.setTextViewText(R.id.notification_content, data.getContent());
//            mRemoteViews.setViewVisibility(R.id.cargo_count, View.INVISIBLE);
//            mRemoteViews.setViewVisibility(R.id.order_count, View.INVISIBLE);
//        } else {
////            if (TimeUtils.getShortTime(data.getCreateTime()) != null) {
////                mRemoteViews.setTextViewText(R.id.notification_time, TimeUtils.getShortTime(data.getCreateTime()));
////            } else {
//                mRemoteViews.setTextViewText(R.id.notification_time, "刚刚");
////            }
//            if (currentShowType == TYPE_DEFAULT
//                || (currentShowType == TYPE_ORDER_NAV && data.getType().equals(NotificationData.NOTIFICATION_TYPE_ORDER_NAV))
//                || (currentShowType == TYPE_CARGO && data.getType().equals(NotificationData.NOTIFICATION_TYPE_CARGO))) {
//
//                if (!TextUtils.isEmpty(data.getContent())) {
//                    String str = toDBC(data.getContent());
//                    if (str.length() > 20) {
//                        str = str.substring(0, 20) + "...";
//                    }
//                    mRemoteViews.setTextViewText(R.id.notification_content, str);
//                } else {
//                    mRemoteViews.setTextViewText(R.id.notification_content, " ");
//                }
//            }
//        }
//    }
//
//    private void updateView(NotificationData data) {
//        getNormalView();
//        if (mRemoteViews == null) {
//            return;
//        }
//        updateContent(data);
//        if (cargoCount > 0) {
//            setCargoCount();
//        }
//        if (orderCount > 0) {
//            setOrderCount();
//        }
//        updateNotify();
//    }
//
//    public static String toDBC(String input) {
//        char[] c = input.toCharArray();
//        for (int i = 0; i < c.length; i++) {
//            if (c[i] == 12288) {
//                c[i] = (char) 32;
//                continue;
//            }
//            if (c[i] > 65280 && c[i] < 65375) {
//                c[i] = (char) (c[i] - 65248);
//            }
//        }
//        return new String(c);
//    }
//
//    public static final String COM_YMM_DRIVER_NOTIFY_ACTION_ORDER = "COM_YMM_DRIVER_NOTIFY_ACTION_ORDER";
//    public static final String COM_YMM_DRIVER_NOTIFY_ACTION_ORDER_NAV = "COM_YMM_DRIVER_NOTIFY_ACTION_ORDER_NAV";
//    public static final String COM_YMM_DRIVER_NOTIFY_ACTION_CARGO = "COM_YMM_DRIVER_NOTIFY_ACTION_CARGO";
//    public static final String COM_YMM_DRIVER_NOTIFY_ACTION_CONTENT = "COM_YMM_DRIVER_NOTIFY_ACTION_CONTENT";
//    public static final String COM_YMM_DRIVER_NOTIFY_ACTION_REMOVE = "COM_YMM_DRIVER_NOTIFY_ACTION_REMOVE";
//
//    public static final String COM_YMM_DRIVER_NTF_ACTION = "extra_ntf_action";
//
//    private PendingIntent getPendingIntent(Context context, int resID) {
//        if(resID==NOTIFICATION_REMOVE){
//            Intent intent = new Intent(COM_YMM_DRIVER_NOTIFY_ACTION_REMOVE);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, FOREGROUND_ID*10+resID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//            return pendingIntent;
//        }else{
//            Intent intent = new Intent(ContextUtil.get(), NtfDispatchActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            switch (resID) {
//                case ORDER_LAYOUT:
//                    intent.putExtra(COM_YMM_DRIVER_NTF_ACTION, COM_YMM_DRIVER_NOTIFY_ACTION_ORDER);
//                    break;
//                case NAVIGATE_LAYOUT:
//                    intent.putExtra(COM_YMM_DRIVER_NTF_ACTION, COM_YMM_DRIVER_NOTIFY_ACTION_ORDER_NAV);
//                    break;
//                case CARGO_LAYOUT:
//                    intent.putExtra(COM_YMM_DRIVER_NTF_ACTION, COM_YMM_DRIVER_NOTIFY_ACTION_CARGO);
//                    break;
//                case NOTIFICATION_CONTENT:
//                    intent.putExtra(COM_YMM_DRIVER_NTF_ACTION, COM_YMM_DRIVER_NOTIFY_ACTION_CONTENT);
//                    break;
//                default:
//                    break;
//            }
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, FOREGROUND_ID*10+resID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//            return pendingIntent;
//        }
//    }
//
//    private void resetCargoContent() {
//        isNewCargoNotificationShowing = false;
//        notificationCargo = defaultNotificationDataCargo;
//    }
//    private void resetOrderNavContent() {
//        isNewOrderNotificationShowing = false;
//        notificationOrderNav = defaultNotificationDataNav;
//    }
//
//    private long getFixedCargoContentShow() {
//        ConfigCenterService config = ApiManager.getImpl(ConfigCenterService.class);
//        if (config != null) {
//            long value = config.getConfig(GROUP_NAME, "notification_cargo_interval", DEFAULT_CARGO_TIME);
//            return value;
//        }
//        return DEFAULT_CARGO_TIME;
//    }
//
//    private long getLoopCycleInterval() {
//        ConfigCenterService config = ApiManager.getImpl(ConfigCenterService.class);
//        if (config != null) {
//            long value =  config.getConfig(GROUP_NAME, "notification_loop_cycle_interval", DEFAULT_LOOP_CYCLE_TIME);
//            return value;
//
//        }
//        return DEFAULT_LOOP_CYCLE_TIME;
//    }
//
//    private String getDefaultCargoContent() {
//        String defaultCargoContent = "货源消息语音播报,不要错过一票好货源";
//        ConfigCenterService config = ApiManager.getImpl(ConfigCenterService.class);
//        if (config != null) {
//            return config.getConfig(GROUP_NAME, "default_notification_cargo_content", defaultCargoContent);
//        }
//        return defaultCargoContent;
//    }
//
//    private String getDefaultOrderContent() {
//        String defaultOrderNavContent = "货车导航为您精准避开限行区域";
//        ConfigCenterService config = ApiManager.getImpl(ConfigCenterService.class);
//        if (config != null) {
//            return config.getConfig(GROUP_NAME, "default_notification_order_content", defaultOrderNavContent);
//        }
//        return defaultOrderNavContent;
//    }
//
//    private void startTimerForCargo(){
//            MBSchedulers.background().schedule(new Action() {
//                @Override
//                public void action() {
//                    if (!isNewCargoNotificationShowing && !isNewOrderNotificationShowing) {
//                        currentShowType = TYPE_ORDER_NAV;
//                        updateView(notificationOrderNav);
//                    }
//                    startTimerForOrderNav();
//                }
//            },getLoopCycleInterval(), TimeUnit.MILLISECONDS);
//    }
//
//    private void startTimerForOrderNav(){
//            MBSchedulers.background().schedule(new Action() {
//                @Override
//                public void action() {
//                    if (!isNewCargoNotificationShowing && !isNewOrderNotificationShowing) {
//                        currentShowType = TYPE_CARGO;
//                        updateView(notificationCargo); //随机获取
//                    }
//                    startTimerForCargo();
//                }
//            },getLoopCycleInterval(), TimeUnit.MILLISECONDS);
//    }
//
//    private void startNewCargoTimer(){
//            MBSchedulers.background().schedule(new Action() {
//                @Override
//                public void action() {
//                    resetCargoContent();
//                    if (isNewOrderNotificationShowing) {
//                        showFixedTimeOrderNavContent();
//                    }
//                }
//            },getFixedCargoContentShow(), TimeUnit.MILLISECONDS);
//    }
//
//    private void startNewOrderNavTimer(){
//        long millisInFuture = notificationOrderNav.getExpireAt() > System.currentTimeMillis() ?
//                notificationOrderNav.getExpireAt() - System.currentTimeMillis() : defaultOrderNavContentShowTime;
//
//            MBSchedulers.background().schedule(new Action() {
//                @Override
//                public void action() {
//                    resetOrderNavContent();
//                }
//            },millisInFuture, TimeUnit.MILLISECONDS);
//    }
//}
