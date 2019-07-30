package com.kingleng.baselibrary.base;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by leng on 2019/5/6.
 */
public abstract class BaseModule {

    private BaseRouter baseRouter;

    public BaseModule() {
        this.baseRouter = onBind();
    }

    public abstract BaseRouter onBind();

    public boolean startActivity(Context context, String adTypeCode, Bundle bundle){
        return baseRouter.router(context,adTypeCode,bundle);
    }

    public static void startActivityByUrl(Context context, String url){
        Router.getInstance().startActivityByUrl(context, url);
    }

    public static void startActivityByTypeCode(Context context, String adTypeCode){
        Router.getInstance().startActivityByUrl(context, "www.kingleng.com?adType=" + adTypeCode);
    }

}
