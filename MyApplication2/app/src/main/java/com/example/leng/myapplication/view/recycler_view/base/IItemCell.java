package com.example.leng.myapplication.view.recycler_view.base;

import android.view.ViewGroup;

public interface IItemCell {

    /**
     * 回收资源
     */
    void releaseResource();

    /**
     * 初始化资源
     */
    void initResource();

    /**
     * 获取viewType
     */
    int getItemType();

    /**
     * 创建ViewHolder
     */
    RecycleBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 数据绑定
     */
    void onBindViewHolder(RecycleBaseViewHolder holder, int position);
}