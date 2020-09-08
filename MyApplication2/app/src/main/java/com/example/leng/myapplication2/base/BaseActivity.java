package com.example.leng.myapplication2.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 17092234 on 2019/2/18.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Toast toast;
    public void toastCenter(String msg){
        if(toast!=null){
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void displayDialog(CharSequence title, CharSequence msg,
                              CharSequence leftBtnText, View.OnClickListener leftClickListener,
                              CharSequence rightBtnText, View.OnClickListener rightClickListener) {
        CustomDialog dialog = new CustomDialog.Builder().setTitle(title)
                .setMessage(msg).setLeftButton(leftBtnText, leftClickListener)
                .setRightButton(rightBtnText, rightClickListener)
                .setCancelable(false).create();
        dialog.show(getFragmentManager(),dialog.getName());
    }
}
