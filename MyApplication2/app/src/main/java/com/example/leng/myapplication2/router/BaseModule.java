package com.example.leng.myapplication2.router;

/**
 * Created by leng on 2019/5/6.
 */
public class BaseModule {

    public static void startActivityByUrl(String url){
        Router.getInstance().startActivity(url);
    }

}
