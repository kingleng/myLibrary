package com.example.leng.myapplication2.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.app.SophixStubApplication;
import com.example.leng.myapplication2.router.AppModule;
import com.taobao.sophix.SophixManager;

public class HotfixActivity extends AppCompatActivity {

    TextView textView;
    Button btn;
    Button next_btn;
    ImageView circle_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);

        textView = findViewById(R.id.text);
        btn = findViewById(R.id.download_btn);
        next_btn = findViewById(R.id.next_btn);
        circle_imageview = findViewById(R.id.circle_imageview);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SophixManager.getInstance().queryAndLoadNewPatch();
//                textView.setText("神墓，长生界，遮天，完美世界，圣墟");
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppModule.startActivityByUrl(HotfixActivity.this,"http://www.baidu.com");
            }
        });

        SophixStubApplication.msgDisplayListener = new SophixStubApplication.MsgDisplayListener() {
            @Override
            public void handle(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(msg);
                    }
                });
            }
        };

        circle_imageview.setImageResource(R.drawable.defult_bg);

    }


}
