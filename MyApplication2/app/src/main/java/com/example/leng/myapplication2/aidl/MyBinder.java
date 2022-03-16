package com.example.leng.myapplication2.aidl;

import android.os.RemoteException;

import com.example.leng.myapplication2.IMyAidlInterface;

/**
 * Created by leng on 2020/5/18.
 */
public class MyBinder extends IMyAidlInterface.Stub {

    @Override
    public void getBookName() throws RemoteException {

    }
}
