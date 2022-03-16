package com.example.myloginlibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by leng on 2016/11/9.
 */

public class MyLogin {

    public Context context;
    Mylistener mylistener;

    public MyLogin(Context context,Mylistener mylistener) {
        this.context = context;
        this.mylistener = mylistener;
    }

    public void loginQQ(Activity context0){
        UMShareAPI mShareAPI = UMShareAPI.get( context0 );
//        mShareAPI.doOauthVerify(context0, SHARE_MEDIA.QQ, umAuthListener);
        mShareAPI.getPlatformInfo(context0, SHARE_MEDIA.QQ, umAuthListener);
    }

    public void loginWEIXIN(Activity context0){
        UMShareAPI mShareAPI = UMShareAPI.get( context0 );
        mShareAPI.doOauthVerify(context0, SHARE_MEDIA.WEIXIN, umAuthListener);
//        mShareAPI.getPlatformInfo(context0, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    public void loginSINA(Activity context0){
        UMShareAPI mShareAPI = UMShareAPI.get( context0 );
        mShareAPI.doOauthVerify(context0, SHARE_MEDIA.SINA, umAuthListener);
//        mShareAPI.getPlatformInfo(context0, SHARE_MEDIA.SINA, umAuthListener);
    }

    public void cancelQQ(Activity context0){
        UMShareAPI mShareAPI = UMShareAPI.get( context0 );
        mShareAPI.deleteOauth(context0, SHARE_MEDIA.QQ, umdelAuthListener);
    }

    public void cancelWEIXIN(Activity context0){
        UMShareAPI mShareAPI = UMShareAPI.get( context0 );
        mShareAPI.deleteOauth(context0, SHARE_MEDIA.WEIXIN, umdelAuthListener);
    }

    public void cancelSINA(Activity context0){
        UMShareAPI mShareAPI = UMShareAPI.get( context0 );
        mShareAPI.deleteOauth(context0, SHARE_MEDIA.SINA, umdelAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mylistener.getData(data);
            Toast.makeText(context, "Authorize succeed", Toast.LENGTH_SHORT).show();
            Log.e("MYdata","Authorize succeed");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( context, "Authorize fail", Toast.LENGTH_SHORT).show();
            Log.e("MYdata","Authorize fail");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( context, "Authorize cancel", Toast.LENGTH_SHORT).show();
            Log.e("MYdata","Authorize cancel");
        }
    };

    /** delauth callback interface**/
    private UMAuthListener umdelAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(context, "delete Authorize succeed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( context, "delete Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( context, "delete Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    public void Share(Activity context0){
//        new ShareAction(context0).setPlatform(SHARE_MEDIA.QQ)
//                .withText("hello")
//                .setCallback(umShareListener)
//                .share();
        new ShareAction(context0).withText("hello")
                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);

            Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
