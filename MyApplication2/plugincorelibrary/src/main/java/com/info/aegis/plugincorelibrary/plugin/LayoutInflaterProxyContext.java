package com.info.aegis.plugincorelibrary.plugin;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

/**
 * @author Lody
 * @version 1.0
 */
public class LayoutInflaterProxyContext extends ContextWrapper {

    private LayoutInflater mInflater;

    public LayoutInflaterProxyContext(Context base) {
        super(base);
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                // 每个Activity都有自己独一无二的Layoutflater
                // 这里首先拿到在SystemServiceRegistry中注册的Application的Layoutflater
                // 然后根据该创建属于每个Activity的PhoneLayoutInflater
                mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return mInflater;
        }
        return getBaseContext().getSystemService(name);
    }

}
