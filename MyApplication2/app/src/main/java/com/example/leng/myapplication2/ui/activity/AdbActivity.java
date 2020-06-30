package com.example.leng.myapplication2.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.base.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdbActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb);


        try {
            startAdb("ls");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startAdb(String adb) throws IOException {
        Process p = Runtime.getRuntime().exec(adb);

        String data = null;
        BufferedReader ie = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String error = null;

        while ((error = ie.readLine()) != null
                && !error.equals("null")) {
            data += error + "\n";
        }
        String line = null;
        while ((line = in.readLine()) != null
                && !line.equals("null")) {
            data += line + "\n";
        }

        Log.e("asd",data);
    }
}
