package com.info.aegis.plugindemo2.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.info.aegis.baselibrary.router.BaseRouter;
import com.info.aegis.baselibrary.router.TypeManager;
import com.info.aegis.plugindemo2.MainActivity;

/**
 * Created by leng on 2019/7/29.
 */
public class PluginRouter extends BaseRouter {

    @Override
    public boolean router(Context context, String adTypeCode, Bundle bundle) {
        boolean isUsed = true;

        Intent intent;
        switch (adTypeCode){
            case TypeManager.ZNWD_ITEM_0:
                intent = new Intent(context, MainActivity.class);
                intent.putExtras(bundle);
                if(!(context instanceof Activity)){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
                break;
            default:
                isUsed = false;
        }

        return isUsed;
    }
}
