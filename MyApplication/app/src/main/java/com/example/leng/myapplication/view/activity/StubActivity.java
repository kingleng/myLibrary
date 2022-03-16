package com.example.leng.myapplication.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class StubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"StubActivity",Toast.LENGTH_SHORT).show();
    }
}
