package com.example.leng.myapplication2.ui.observer;

import java.util.Observable;

/**
 * Created by leng on 2019/10/14.
 */
public class AndroidObervable extends Observable {
    static final String TAG = AndroidObervable.class.getSimpleName();

    public void postNewInfo(String info){
        setChanged();
        notifyObservers(info);
    }

}
