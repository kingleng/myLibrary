package com.example.leng.myapplication2.ui.notification;

import android.os.Binder;

import java.lang.ref.WeakReference;

public class ForegroundBinder extends Binder {

    private WeakReference<ForegroundService> weakService;

    /**
     * Inject service instance to weak reference.
     */
    public void onBind(ForegroundService service) {
        this.weakService = new WeakReference<>(service);
    }

    public ForegroundService getService() {
        return weakService == null ? null : weakService.get();
    }
}
