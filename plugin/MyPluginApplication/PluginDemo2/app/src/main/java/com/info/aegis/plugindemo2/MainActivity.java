package com.info.aegis.plugindemo2;

import android.os.Bundle;
import android.widget.TextView;

import com.info.aegis.baselibrary.base.LibraryActivity;

public class MainActivity extends LibraryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin2_activity_main);
        TextView textView = findViewById(R.id.text);
        textView.setText("插件二");
    }
}
