package com.example.leng.myapplication2.ui.notification;

/**
 * Created by lincheng on 2021/11/26.
 */
public class OngoingNotificationHelper {

    private static IOngoingNotification impl;

    public static void start(){
        if(impl!=null && !impl.isServiceStarted()){
            impl.registerListenerAndStartService();
        }
    }

    public static void stop(){
        if(impl!=null && impl.isServiceStarted()){
            impl.unregisterListenerAndStopService();
        }
    }

    public static void setImpl(IOngoingNotification iOngoingNotification){
        impl = iOngoingNotification;
    }

    public interface IOngoingNotification{
        void registerListenerAndStartService();
        void unregisterListenerAndStopService();
        boolean isServiceStarted();
    }
}
