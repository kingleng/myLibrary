package com.kingleng.baselibrary.router.interceptor;

/**
 * Created by leng on 2021/1/11.
 */
public abstract class BaseInterceptor {

    int priority = 100;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public abstract boolean interceptor(String adType);
}
