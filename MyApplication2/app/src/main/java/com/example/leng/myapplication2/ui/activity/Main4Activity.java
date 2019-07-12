package com.example.leng.myapplication2.ui.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.example.leng.myapplication2.R;

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



        //新增多页模式，即一个引导层显示多页引导内容
        NewbieGuide.with(this)
                .setLabel("page")//设置引导层标示区分不同引导层，必传！否则报错
//                .anchor(anchor)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {

                    }

                    @Override
                    public void onRemoved(Controller controller) {

                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {

                    @Override
                    public void onPageChanged(int page) {
                        //引导页切换，page为当前页位置，从0开始
                        Toast.makeText(Main4Activity.this, "引导页切换：" + page, Toast.LENGTH_SHORT).show();
                    }
                })
                .alwaysShow(true)//是否每次都显示引导层，默认false，只显示一次
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(btn)//添加高亮的view
                                .setLayoutRes(R.layout.view_guide_simple)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {

                                    @Override
                                    public void onLayoutInflated(View view) {
//                                        //引导页布局填充后回调，用于初始化
//                                        TextView tv = view.findViewById(R.id.textView2);
//                                        tv.setText("我是动态设置的文本");
                                    }
                                })
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(btn, HighLight.Shape.RECTANGLE, 20)
                        .setLayoutRes(R.layout.view_guide_simple)//引导页布局，点击跳转下一页或者消失引导层的控件id
//                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(btn)
                        .setLayoutRes(R.layout.view_guide_dialog)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view) {

                            }
                        })
                )
                .show();//显示引导层(至少需要一页引导页才能显示)




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
