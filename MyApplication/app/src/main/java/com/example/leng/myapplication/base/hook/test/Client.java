package com.example.leng.myapplication.base.hook.test;

import java.lang.reflect.Proxy;

/**
 * Created by leng on 2020/3/9.
 */
public class Client {

    public static void main(String[] args) {

        IShop kingleng = new Kingleng();
        DynamicPurchasing mDynamicPurchasing = new DynamicPurchasing(kingleng);
        ClassLoader loader = kingleng.getClass().getClassLoader();
        IShop purchasing = (IShop) Proxy.newProxyInstance(loader,new Class[]{IShop.class},mDynamicPurchasing);
        purchasing.buy();
    }

}
