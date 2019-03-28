package com.example.leng.myapplication.view.recycler_view.base;

import android.app.Activity;
import android.os.Handler;

public abstract class RecycleBaseCell<T> implements IItemCell {

    public T mData;
    protected Activity mContext;
    protected RecycleBaseAdapter mAdapter;

    public RecycleBaseCell(T t) {
        mData = t;
    }

    /**
     * TODO 可能有 Exception
     * 移除当前楼层
     */
    public void removeCurrentCell(final RecycleBaseCell itemCell) {
        if (mAdapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.remove(itemCell);
                }
            }, 500);
        }
    }

    @Override
    public void releaseResource() {
        //需要回收的资源，由子类自己实现
    }
}