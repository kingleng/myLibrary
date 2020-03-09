package com.example.leng.myapplication.base.hook.test;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by leng on 2020/3/9.
 */
public class DynamicPurchasing implements InvocationHandler {

    private Object obj;

    public DynamicPurchasing(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(obj,args);
        if(method.getName().equals("buy")){
            System.out.println("DynamicPurchasing 买买买");
        }
        return result;
    }
}
