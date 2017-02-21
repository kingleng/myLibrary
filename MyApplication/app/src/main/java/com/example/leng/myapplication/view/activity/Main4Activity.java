package com.example.leng.myapplication.view.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.leng.myapplication.R;

public class Main4Activity extends AppCompatActivity {

    private WebView webView;
    private Button btn;

    private static final String BASE_URL = "file:///android_asset/";
    public static final String ADDVEHICLEDESCRIPTION_PAGE = "mytext.html";

    String json = "{\n" +
            "  \"categories\":[\"衬衫\",\"羊毛衫\",\"雪纺衫\",\"裤子\",\"高跟鞋\",\"袜子\"],\n" +
            "  \"data\":[5, 20, 36, 10, 10, 20]\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        webView = (WebView)findViewById(R.id.webview);
        btn = (Button)findViewById(R.id.btn);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl(BASE_URL + ADDVEHICLEDESCRIPTION_PAGE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:funFromjs("+json+")");
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:funFromjs("+json+")");
            }
        },2000);



    }


//    public class MyObject{
//
//        public String getJson(){
//
//            Toast.makeText(Main4Activity.this,"json",Toast.LENGTH_SHORT).show();
//             return json;
//        }
//    }

}
