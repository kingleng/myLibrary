package com.info.aegis.mypluginapplication.plugin.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.info.aegis.mypluginapplication.plugin.R;
import com.info.aegis.mypluginapplication.plugin.base.BaseActivity;
import com.info.aegis.mypluginapplication.plugin.ui.fragment.ZnwdFragment;

public class ZHomeActivity extends BaseActivity {

    private static final String TAG = "ZHomeActivity";

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 20200402;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhome);




        String qr_code = getIntent().getStringExtra("qr_code");
        String event_ids = getIntent().getStringExtra("event_ids");
        String rebot_name = getIntent().getStringExtra("rebot_name");
        String location = getIntent().getStringExtra("location");

        ZnwdFragment mZnwdFragment = new ZnwdFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("event_ids",event_ids);
//        bundle.putString("rebot_name",rebot_name);
//        bundle.putString("location",location);
        bundle.putString("event_ids","1");
        bundle.putString("rebot_name","南京擎盾");
        bundle.putString("location","南京");
        if(!TextUtils.isEmpty(qr_code)){
            bundle.putString("qr_code",qr_code);
        }
        mZnwdFragment.setArguments(bundle);

        if (hasPermission()) {
            changeFragment(mZnwdFragment);
        } else {
            requestPermission();
        }




    }

//    跳转（添加fragment）
    public void changeFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.id_container, fragment);
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean hasPermission() {

        Log.d(TAG, "hasPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestPermission() {

        Log.d(TAG, "requestPermission");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE_STORAGE);
        }
    }


}
