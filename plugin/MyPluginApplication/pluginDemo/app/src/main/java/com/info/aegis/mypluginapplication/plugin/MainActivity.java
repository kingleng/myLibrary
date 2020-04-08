package com.info.aegis.mypluginapplication.plugin;


import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.info.aegis.baselibrary.base.LibraryActivity;
import com.info.aegis.baselibrary.router.BaseModule;
import com.lange.jnimake.Testjni;

public class MainActivity extends LibraryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity_main);

        TextView textView = findViewById(R.id.aa);

        Testjni testjni = new Testjni();
        textView.setText(testjni.getString());

        textView.setOnClickListener(v -> {
            textView.setText(R.string.plugin_aaa);
            new Handler().postDelayed(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("event_ids","1");
                bundle.putString("rebot_name","南京擎盾");
                bundle.putString("location","南京");

                BaseModule.startActivityByUrl(MainActivity.this,"www.aegis.com?adType=110001",bundle);

            },300);
        });

    }


}
