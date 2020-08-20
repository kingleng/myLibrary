package com.info.aegis.mypluginapplication.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.info.aegis.mypluginapplication.ZZ_MainActivity;
import com.kingleng.baselibrary.router.BaseRouter;
import com.kingleng.baselibrary.router.TypeManager;

/**
 * Created by leng on 2019/7/29.
 */
public class TestPluginRouter extends BaseRouter {

    @Override
    public boolean router(Context context, String adTypeCode, Bundle bundle) {
        boolean isUsed = true;
        switch (adTypeCode){
            case TypeManager.ZNWD_ITEM_0:
                startMainActivity(context,bundle);
                break;
            default:
                isUsed = false;
        }

        return isUsed;
    }

    public void startMainActivity(Context context, Bundle bundle){
        Intent intent = new Intent(context, ZZ_MainActivity.class);
        intent.putExtras(bundle);
        if(!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
