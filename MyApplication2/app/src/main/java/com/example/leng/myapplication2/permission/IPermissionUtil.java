package com.example.leng.myapplication2.permission;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.leng.myapplication2.base.BaseActivity;


/**
 * Created by 17093475 on 2018/2/23.
 */

public abstract class IPermissionUtil {

    private static String TAG = IPermissionUtil.class.getSimpleName();
    protected /*final*/ String[] mPermission;
    protected /*final*/ BaseActivity mActivity;

    /**
     * 摄像头权限 弹框次数 3次去设置
     */
    protected int mPermissionCount = 0;

    protected boolean mHasPermission = false;

    public boolean isHasPermission() {
        return mHasPermission;
    }

    protected int mPermissionRequestCode;

    protected IPermissionUtil(String[] permission, BaseActivity activity,
                              int permissionRequestCode) {
        this.mPermission = permission;
        this.mActivity = activity;
        this.mPermissionRequestCode = permissionRequestCode;
    }


    public boolean checkPermissionOrRequest() {
        Log.i(TAG, "--- checkPermissionOrRequest");
        return mHasPermission;
    }

    /**
     * 去系统设置
     */
    public void toNotification() {

        IntentUtils.gotoPermissionSetting(mActivity);
    }

    public void requestPermission() {
        //        ActivityCompat.requestPermissions(mStoreBaseActivity, new String[]{mPermission},
        //                                          REQUEST_CAMERA);
    }

    public abstract void handleRequestResult(int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults);

}
