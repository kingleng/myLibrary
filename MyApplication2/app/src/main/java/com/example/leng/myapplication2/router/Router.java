package com.example.leng.myapplication2.router;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.leng.myapplication2.view.activity.CoordinatorLayoutActivity;
import com.example.leng.myapplication2.view.activity.DemoActivity;
import com.example.leng.myapplication2.view.activity.Main2Activity;
import com.example.leng.myapplication2.view.activity.Main6Activity;
import com.example.leng.myapplication2.view.activity.Main7Activity;
import com.example.leng.myapplication2.view.activity.Main8Activity_LianDong;
import com.example.leng.myapplication2.view.activity.MainActivity;
import com.example.leng.myapplication2.view.activity.MarQueeActivity;
import com.example.leng.myapplication2.view.activity.MusicActivity;
import com.example.leng.myapplication2.view.activity.MyGameActivity;
import com.example.leng.myapplication2.view.activity.MyTextNewActivity;
import com.example.leng.myapplication2.view.activity.RefreshActivity;
import com.example.leng.myapplication2.view.activity.TestActivity;
import com.example.leng.myapplication2.view.activity.VideoViewActivity;
import com.example.leng.myapplication2.view.activity.ViewPager2Activity;
import com.example.leng.myapplication2.view.activity.ViewPagerActivity;
import com.example.leng.myapplication2.view.activity.VlayoutActivity;
import com.example.leng.myapplication2.view.activity.WebviewActivity;
import com.example.leng.myapplication2.view.activity.XuanFuActivity;

/**
 * Created by leng on 2019/5/6.
 */
public class Router {

    private Context mContext;

    public Router(Context mContext) {
        this.mContext = mContext;
    }

    public static void init(Context mContext){
        instance = new Router(mContext);
    }

    private static Router instance;

    public static Router getInstance(){
        return instance;
    }

    public void startActivity(String url){
        if(!url.contains("adType")){
            Intent intent = new Intent(mContext, WebviewActivity.class);
            intent.putExtra("url",url);
            mContext.startActivity(intent);
        }
        String params = url.split("\\?")[1];
        String adType = params.split("&")[0].replace("adType=","");
        switch (adType){
            case TypeManager.MIAN_ITEM_1:
                mContext.startActivity(new Intent(mContext, TestActivity.class));
                return;
            case TypeManager.MIAN_ITEM_2:
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                return;
            case TypeManager.MIAN_ITEM_3:
                mContext.startActivity(new Intent(mContext, DemoActivity.class));
                return;
            case TypeManager.MIAN_ITEM_4:
                mContext.startActivity(new Intent(mContext, RefreshActivity.class));
                return;
            case TypeManager.MIAN_ITEM_5:
                mContext.startActivity(new Intent(mContext, CoordinatorLayoutActivity.class));
                return;
            case TypeManager.MIAN_ITEM_6:
                mContext.startActivity(new Intent(mContext, MyGameActivity.class));
                return;
            case TypeManager.MIAN_ITEM_7:
                mContext.startActivity(new Intent(mContext, MyTextNewActivity.class));
                return;
            case TypeManager.MIAN_ITEM_8:
                mContext.startActivity(new Intent(mContext, Main2Activity.class));
                return;
            case TypeManager.MIAN_ITEM_9:
                mContext.startActivity(new Intent(mContext, MarQueeActivity.class));
                return;
            case TypeManager.MIAN_ITEM_10:
                mContext.startActivity(new Intent(mContext, Main6Activity.class));
                return;
            case TypeManager.MIAN_ITEM_11:
                mContext.startActivity(new Intent(mContext, Main7Activity.class));
                return;
            case TypeManager.MIAN_ITEM_12:
                mContext.startActivity(new Intent(mContext, Main8Activity_LianDong.class));
                return;
            case TypeManager.MIAN_ITEM_13:
                mContext.startActivity(new Intent(mContext, MusicActivity.class));
                return;
            case TypeManager.MIAN_ITEM_14:
                mContext.startActivity(new Intent(mContext, VlayoutActivity.class));
                return;
            case TypeManager.MIAN_ITEM_15:
                mContext.startActivity(new Intent(mContext, ViewPager2Activity.class));
                return;
            case TypeManager.MIAN_ITEM_16:
                mContext.startActivity(new Intent(mContext, ViewPagerActivity.class));
                return;
            case TypeManager.MIAN_ITEM_17:
                mContext.startActivity(new Intent(mContext, XuanFuActivity.class));
                return;
            case TypeManager.MIAN_ITEM_18:
                mContext.startActivity(new Intent(mContext, VideoViewActivity.class));
                return;
            default:
                Toast.makeText(mContext, "页面找不到了！", Toast.LENGTH_SHORT).show();
        }

    }

}
