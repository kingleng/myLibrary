package com.example.leng.myapplication2.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.leng.myapplication2.ui.activity.AudioActivity;
import com.example.leng.myapplication2.ui.activity.CameraActivity;
import com.example.leng.myapplication2.ui.activity.CoordinatorLayoutActivity;
import com.example.leng.myapplication2.ui.activity.DemoActivity;
import com.example.leng.myapplication2.ui.activity.FrameAnimationActivity;
import com.example.leng.myapplication2.ui.activity.HomeActivity;
import com.example.leng.myapplication2.ui.activity.HotfixActivity;
import com.example.leng.myapplication2.ui.activity.JsoupActivity;
import com.example.leng.myapplication2.ui.activity.Main2Activity;
import com.example.leng.myapplication2.ui.activity.Main6Activity;
import com.example.leng.myapplication2.ui.activity.Main7Activity;
import com.example.leng.myapplication2.ui.activity.Main8Activity_LianDong;
import com.example.leng.myapplication2.ui.activity.MainActivity;
import com.example.leng.myapplication2.ui.activity.MarQueeActivity;
import com.example.leng.myapplication2.ui.activity.MusicActivity;
import com.example.leng.myapplication2.ui.activity.MyGameActivity;
import com.example.leng.myapplication2.ui.activity.MyTextNewActivity;
import com.example.leng.myapplication2.ui.activity.RefreshActivity;
import com.example.leng.myapplication2.ui.activity.RxHttpActivity;
import com.example.leng.myapplication2.ui.activity.SendSMSActivity;
import com.example.leng.myapplication2.ui.activity.TestActivity;
import com.example.leng.myapplication2.ui.activity.VideoViewActivity;
import com.example.leng.myapplication2.ui.activity.ViewPager2Activity;
import com.example.leng.myapplication2.ui.activity.ViewPagerActivity;
import com.example.leng.myapplication2.ui.activity.VlayoutActivity;
import com.example.leng.myapplication2.ui.activity.WebviewActivity;
import com.example.leng.myapplication2.ui.activity.XuanFuActivity;

import com.kingleng.baselibrary.base.BaseRouter;

/**
 * Created by leng on 2019/7/29.
 */
public class AppRouter extends BaseRouter {

    @Override
    public boolean router(Context context, String adTypeCode, Bundle bundle) {
//        return super.router(context, adTypeCode, bundle);
        boolean isUsed = true;

        Intent intent;
        switch (adTypeCode){
            case "1000":
                intent = new Intent(context, HomeActivity.class);
                intent.putExtras(bundle);
                if(!(context instanceof Activity)){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
                break;
            case TypeManager.MIAN_ITEM_0:
                intent = new Intent(context, WebviewActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case TypeManager.MIAN_ITEM_1:
                context.startActivity(new Intent(context, TestActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_2:
                context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_3:
                context.startActivity(new Intent(context, DemoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_4:
                context.startActivity(new Intent(context, RefreshActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_5:
                context.startActivity(new Intent(context, CoordinatorLayoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_6:
                context.startActivity(new Intent(context, MyGameActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_7:
                context.startActivity(new Intent(context, MyTextNewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_8:
                context.startActivity(new Intent(context, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_9:
                context.startActivity(new Intent(context, MarQueeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_10:
                context.startActivity(new Intent(context, Main6Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_11:
                context.startActivity(new Intent(context, Main7Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_12:
                context.startActivity(new Intent(context, Main8Activity_LianDong.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_13:
                context.startActivity(new Intent(context, MusicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_14:
                context.startActivity(new Intent(context, VlayoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_15:
                context.startActivity(new Intent(context, ViewPager2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_16:
                context.startActivity(new Intent(context, ViewPagerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_17:
                context.startActivity(new Intent(context, XuanFuActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_18:
                context.startActivity(new Intent(context, VideoViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_19:
                context.startActivity(new Intent(context, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_20:
                context.startActivity(new Intent(context, FrameAnimationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_21:
                context.startActivity(new Intent(context, JsoupActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_22:
                context.startActivity(new Intent(context, RxHttpActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_23:
                context.startActivity(new Intent(context, HotfixActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_24:
                context.startActivity(new Intent(context, SendSMSActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_25:
                context.startActivity(new Intent(context, AudioActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:
                isUsed = false;
        }

        return isUsed;
    }



//    public void startActivity(Context mContext, String url){
//
//        if(!url.contains("adType")){
//            Intent intent = new Intent(mContext, WebviewActivity.class);
//            intent.putExtra("url",url);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//            return;
//        }
//        String params = url.split("\\?")[1];
//        String adType = params.split("&")[0].replace("adType=","");
//
//        switch (adType){
//            case TypeManager.MIAN_ITEM_1:
//                mContext.startActivity(new Intent(mContext, TestActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_2:
//                mContext.startActivity(new Intent(mContext, com.example.leng.myapplication2.ui.activity.MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_3:
//                mContext.startActivity(new Intent(mContext, DemoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_4:
//                mContext.startActivity(new Intent(mContext, RefreshActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_5:
//                mContext.startActivity(new Intent(mContext, CoordinatorLayoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//                return;
//            case TypeManager.MIAN_ITEM_6:
//                mContext.startActivity(new Intent(mContext, MyGameActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_7:
//                mContext.startActivity(new Intent(mContext, MyTextNewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_8:
//                mContext.startActivity(new Intent(mContext, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_9:
//                mContext.startActivity(new Intent(mContext, MarQueeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_10:
//                mContext.startActivity(new Intent(mContext, Main6Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_11:
//                mContext.startActivity(new Intent(mContext, Main7Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_12:
//                mContext.startActivity(new Intent(mContext, Main8Activity_LianDong.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_13:
//                mContext.startActivity(new Intent(mContext, MusicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_14:
//                mContext.startActivity(new Intent(mContext, VlayoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_15:
//                mContext.startActivity(new Intent(mContext, ViewPager2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_16:
//                mContext.startActivity(new Intent(mContext, ViewPagerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_17:
//                mContext.startActivity(new Intent(mContext, XuanFuActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_18:
//                mContext.startActivity(new Intent(mContext, VideoViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_19:
//                mContext.startActivity(new Intent(mContext, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_20:
//                mContext.startActivity(new Intent(mContext, FrameAnimationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_21:
//                mContext.startActivity(new Intent(mContext, JsoupActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_22:
//                mContext.startActivity(new Intent(mContext, RxHttpActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_23:
//                mContext.startActivity(new Intent(mContext, HotfixActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            case TypeManager.MIAN_ITEM_24:
//                mContext.startActivity(new Intent(mContext, SendSMSActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                return;
//            default:
//                Toast.makeText(mContext, "页面找不到了！", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}
