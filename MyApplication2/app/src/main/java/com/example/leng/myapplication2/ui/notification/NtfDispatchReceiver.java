package com.example.leng.myapplication2.ui.notification;

//import static com.example.leng.myapplication2.ui.notification.NotificationViewHelper.COM_YMM_DRIVER_NOTIFY_ACTION_REMOVE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author lincheng
 * @since 2021/12/08
 */
public class NtfDispatchReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        switch(intent.getAction()){
//            case COM_YMM_DRIVER_NOTIFY_ACTION_REMOVE:
                OngoingNotificationHelper.stop();
//                OngoingNotificationSwitch.setNotificationRemoved();
//                break;
//            default:
//                break;
//        }
    }
}
