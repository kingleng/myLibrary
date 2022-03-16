package com.kingleng.baselibrary.router;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by leng on 2019/5/6.
 */
public class BaseRouter {

    private static BaseRouter instance;

    public static BaseRouter getInstance() {
        if(instance == null){
            instance = new BaseRouter();
        }
        return instance;
    }

    public static void homeBtnForward(Context context, String url){
        getInstance().startMainRouter(context,url);
    }

    private  void startMainRouter(Context context, String url){

    }

    public boolean router(Context context, String adTypeCode, Bundle bundle){
        return false;
    }

}
