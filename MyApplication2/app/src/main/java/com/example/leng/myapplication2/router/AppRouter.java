package com.example.leng.myapplication2.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.leng.myapplication2.ui.activity.AdbActivity;
import com.example.leng.myapplication2.ui.activity.AudioActivity;
import com.example.leng.myapplication2.ui.activity.CameraActivity;
import com.example.leng.myapplication2.ui.activity.CoordinatorLayoutActivity;
import com.example.leng.myapplication2.ui.activity.DemoActivity;
import com.example.leng.myapplication2.ui.activity.FrameAnimationActivity;
import com.example.leng.myapplication2.ui.HomeActivity;
import com.example.leng.myapplication2.ui.activity.HotfixActivity;
import com.example.leng.myapplication2.ui.activity.ImageMapActivity;
import com.example.leng.myapplication2.ui.activity.JsoupActivity;
import com.example.leng.myapplication2.ui.activity.LargeImageActivity;
import com.example.leng.myapplication2.ui.activity.Main2Activity;
import com.example.leng.myapplication2.ui.activity.Main6Activity;
import com.example.leng.myapplication2.ui.activity.Main7Activity;
import com.example.leng.myapplication2.ui.activity.Main8Activity_LianDong;
import com.example.leng.myapplication2.ui.activity.MainActivity;
import com.example.leng.myapplication2.ui.activity.MarQueeActivity;
import com.example.leng.myapplication2.ui.activity.MusicActivity;
import com.example.leng.myapplication2.ui.activity.MyGameActivity;
import com.example.leng.myapplication2.ui.activity.MyTextNewActivity;
import com.example.leng.myapplication2.ui.activity.Pic2AsciiActivity;
import com.example.leng.myapplication2.ui.activity.RecyclerViewPagerActivity;
import com.example.leng.myapplication2.ui.activity.RefreshActivity;
import com.example.leng.myapplication2.ui.activity.RxHttpActivity;
import com.example.leng.myapplication2.ui.activity.SVGActivity;
import com.example.leng.myapplication2.ui.activity.SendSMSActivity;
import com.example.leng.myapplication2.ui.activity.SipPhoneActivity;
import com.example.leng.myapplication2.ui.activity.TestActivity;
import com.example.leng.myapplication2.ui.activity.VideoViewActivity;
import com.example.leng.myapplication2.ui.activity.ViewPager2Activity;
import com.example.leng.myapplication2.ui.activity.ViewPagerActivity;
import com.example.leng.myapplication2.ui.activity.VlayoutActivity;
import com.example.leng.myapplication2.ui.activity.WNFXActivity;
import com.example.leng.myapplication2.ui.activity.WebviewActivity;
import com.example.leng.myapplication2.ui.activity.XuanFuActivity;
import com.example.leng.myapplication2.ui.activity.YMTDActivity;
import com.example.leng.myapplication2.ui.activity.star.StarActivity;
import com.kingleng.baselibrary.router.BaseRouter;

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
            case TypeManager.MIAN_ITEM_26:
                context.startActivity(new Intent(context, Pic2AsciiActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_27:
                context.startActivity(new Intent(context, ImageMapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_28:
                context.startActivity(new Intent(context, AdbActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_29:
                context.startActivity(new Intent(context, SipPhoneActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_30:
                context.startActivity(new Intent(context, YMTDActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_31:
                context.startActivity(new Intent(context, WNFXActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_32:
                context.startActivity(new Intent(context, LargeImageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_33:
                context.startActivity(new Intent(context, SVGActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_34:
                context.startActivity(new Intent(context, StarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case TypeManager.MIAN_ITEM_35:
                context.startActivity(new Intent(context, RecyclerViewPagerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:
                isUsed = false;
        }

        return isUsed;
    }
}
