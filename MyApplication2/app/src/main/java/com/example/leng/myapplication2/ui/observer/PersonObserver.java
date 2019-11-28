package com.example.leng.myapplication2.ui.observer;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by leng on 2019/10/14.
 */
public class PersonObserver implements Observer {

    static final String TAG = PersonObserver.class.getSimpleName();

    String name;

    public PersonObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.e(TAG,name + " 收到通知 " + arg);
    }
}
