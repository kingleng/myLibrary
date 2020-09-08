package com.example.leng.myapplication2.permission;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;


import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.base.BaseActivity;


/**
 * 存储权限工具
 * Created by 17093475 on 2018/2/23.
 * SYSTEM_ALERT_WINDOW
 */


public class AlertWindowPermissionUtil extends IPermissionUtil {

    private static String TAG = AlertWindowPermissionUtil.class.getSimpleName();

    /**
     * Id to identify a camera permission request.
     */
    public AlertWindowPermissionUtil(BaseActivity activity, int permissionRequestCode) {
        super(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, activity, permissionRequestCode);
    }

    @Override
    public boolean checkPermissionOrRequest() {

        if (Build.VERSION.SDK_INT >= 23) { // Android6.0及以后需要动态申请权限
            if (!Settings.canDrawOverlays(mActivity)) {
                mActivity.displayDialog("", mActivity
                                .getString(R.string.permission_no_alert_window), null, null, mActivity
                                .getString(R.string.product_dialog_dismiss),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermission();
                            }
                        });
                mHasPermission = false;

            } else {
                // 弹出悬浮窗
                mHasPermission = true;
            }
        } else {
            // 弹出悬浮窗
            mHasPermission = true;
        }
        return mHasPermission;
    }


    public void requestPermission() {
        Log.i(TAG, "--- requestPermission");
        //启动Activity让用户授权
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivityForResult(intent, 1010);

    }

    public void handleRequestResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {
        Log.i(TAG, "--- handleRequestResult");
        if (Build.VERSION.SDK_INT >= 23) { // Android6.0及以后需要动态申请权限
            if (Settings.canDrawOverlays(mActivity)) {
                // 弹出悬浮窗
                mHasPermission = true;
            } else {
                mHasPermission = false;
            }
        }

    }
}
