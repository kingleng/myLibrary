package com.example.leng.myapplication2.ui.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.HomeActivity;

public class ForegroundService extends Service {
    private static final String TAG = "FRG.Svs";
    private static final boolean DEBUG = false;
    public final static int FOREGROUND_ID = 1000;

    private final ForegroundBinder fgBinder = new ForegroundBinder();

    private static boolean isRunning;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate start");
        isRunning = true;
        super.onCreate();
        Log.d(TAG, "onCreate finish");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand start");
        isRunning = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            showNotification();
        }
        return START_STICKY;
    }

    private void showNotification() {
//        NotificationViewHelper.get().removeNotify();
        startForeground(FOREGROUND_ID, initNotification(this, getNormalView(),false));
    }

    private Notification notification;
    public volatile RemoteViews mRemoteViews;

    @NonNull
    public Notification initNotification(Context context, @NonNull RemoteViews contentView, boolean needReset) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "cargo通知";
            NotificationChannel channel = new NotificationChannel("amh.app.msg.cargom", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel description");
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0L});
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        if(notification==null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "amh.app.msg.cargom")
                    .setSmallIcon(R.mipmap.icon_driver_app)
                    .setContentTitle("运满满")
                    .setColor(Color.RED)
                    .setCustomContentView(contentView)
                    .setCustomBigContentView(contentView)
//                    .setContentTitle("aaaa")
//                    .setContentText("bbbbbbb")
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .setWhen(System.currentTimeMillis());
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(contentIntent);
            notification = builder.build();
        }
        return notification;
    }

    @NonNull
    public RemoteViews getNormalView() {
        if(mRemoteViews==null){
            mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_normal_layout);
//            mRemoteViews.setOnClickPendingIntent(R.id.order_layout, getPendingIntent(ContextUtil.get(), ORDER_LAYOUT));
//            mRemoteViews.setOnClickPendingIntent(R.id.navigate_layout, getPendingIntent(ContextUtil.get(), NAVIGATE_LAYOUT));
//            mRemoteViews.setOnClickPendingIntent(R.id.cargo_layout, getPendingIntent(ContextUtil.get(), CARGO_LAYOUT));
            mRemoteViews.setOnClickPendingIntent(R.id.ll_notification, getPendingIntent());
//            mRemoteViews.setOnClickPendingIntent(R.id.notification_close, getPendingIntent(ContextUtil.get(), NOTIFICATION_REMOVE));
//            initNotification(ContextUtil.get(),mRemoteViews,true);
        }
        return mRemoteViews;
    }

    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(this, NtfDispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, FOREGROUND_ID*10, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onLowMemory() {
        if (DEBUG) {
            Log.d(TAG, "foregroundservice onLowMemory");
        }
        super.onLowMemory();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (DEBUG) {
            Log.d(TAG, "foregroundservice onTaskRemoved");
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onTrimMemory(int level) {
        if (DEBUG) {
            Log.d(TAG, "foregroundservice onTrimMemory");
        }
        super.onTrimMemory(level);
    }

    @Override
    public void onDestroy() {
        if (DEBUG) {
            Log.d(TAG, "foregroundservice destroy");
        }
        isRunning = false;
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        fgBinder.onBind(this);
        return fgBinder;
    }

    public void forceForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(this, ForegroundService.class);
            ContextCompat.startForegroundService(this, intent);
            showNotification();
        }
    }

    public static boolean isServiceRunning(){
        return isRunning;
    }
}
