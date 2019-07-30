package com.kingleng.app2library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kingleng.app2library.ui.activity.MainActivity;

import com.kingleng.baselibrary.base.BaseRouter;

/**
 * Created by leng on 2019/7/29.
 */
public class App2Router extends BaseRouter {


    @Override
    public boolean router(Context context, String adTypeCode, Bundle bundle) {
//        return super.router(context, adTypeCode, bundle);
        boolean isUsed = true;
        switch (adTypeCode){
            case "110001":
                startMainActivity(context,bundle);
                break;
            default:
                isUsed = false;
        }

        return isUsed;
    }

    public void startMainActivity(Context context, Bundle bundle){
        Intent intent = new Intent(context, MainActivity.class);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


}
