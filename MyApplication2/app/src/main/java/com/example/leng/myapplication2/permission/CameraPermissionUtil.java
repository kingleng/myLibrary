package com.example.leng.myapplication2.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.base.BaseActivity;

/**
 * 相机权限工具
 * Created by 17093475 on 2018/2/23.
 */

public class CameraPermissionUtil extends IPermissionUtil {

    private static String TAG = CameraPermissionUtil.class.getSimpleName();

    /**
     * Id to identify a camera permission request.
     */
    public CameraPermissionUtil(BaseActivity activity, int permissionRequestCode) {
        super(new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, activity, permissionRequestCode);
    }

    @Override
    public boolean checkPermissionOrRequest() {
        Log.i(TAG,
                "--- checkPermissionOrRequest : " + mPermission + " mStoreBaseActivity : " + mActivity);

        if (!isGranted()) {
            mActivity.displayDialog("", mActivity
                            .getString(R.string.permission_no_camera), null, null, mActivity
                            .getString(R.string.product_dialog_dismiss),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermission();
                        }
                    });
            mHasPermission = false;
        } else {
            mHasPermission = true;
        }
        return mHasPermission;
    }


    public void requestPermission() {
        Log.i(TAG, "--- requestPermission");
        ActivityCompat.requestPermissions(mActivity, mPermission,
                mPermissionRequestCode);
    }

    public void handleRequestResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {
        Log.i(TAG, "--- handleRequestResult");
        if (requestCode == mPermissionRequestCode) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && isGranted()) {
                mHasPermission = true;
                if (!PermissionTool.isCameraCanUse()) {
                    mHasPermission = false;
                }
                mPermissionCount = 0;

            } else {
                mHasPermission = false;
                mPermissionCount += 1;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                        Manifest.permission.CAMERA) || mPermissionCount >= 3) {
                    //                    SPUtils.getInstance().putPreferencesVal(CAMERA_PAGE_COUNT,1);
                    mActivity.displayDialog("", mActivity
                                    .getString(R.string.permission_no_camera_to_setting), null, null,
                            mActivity
                                    .getString(R.string.to_setting),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toNotification();
                                }
                            });
                } else {
                    mActivity.displayDialog("", mActivity
                                    .getString(R.string.permission_no_camera), null, null,
                            mActivity.getString(
                                    R.string.product_dialog_dismiss),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermission();
                                }
                            });
                }
            }
        }
    }

    private boolean isGranted() {
        boolean isGranted;
        if (ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || !PermissionTool
                .isCameraCanUse()) {
            isGranted = false;
        } else {
            isGranted = true;
        }
        Log.i(TAG, "----- isGranted " + isGranted);
        return isGranted;
    }
}
