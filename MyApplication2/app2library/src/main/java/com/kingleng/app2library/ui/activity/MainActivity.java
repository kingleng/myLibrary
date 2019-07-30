package com.kingleng.app2library.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kingleng.app2library.App2Module;
import com.kingleng.app2library.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app2_activity_main);
    }

    public void onClick(View view){
//        App2Module.startActivityByTypeCode(MainActivity.this,"100020");
        App2Module.startActivityByUrl(MainActivity.this,"https://www.jd.com/");
    }
}
