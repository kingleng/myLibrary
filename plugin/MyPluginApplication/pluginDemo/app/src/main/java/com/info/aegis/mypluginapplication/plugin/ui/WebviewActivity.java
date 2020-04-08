package com.info.aegis.mypluginapplication.plugin.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.info.aegis.baselibrary.GLog.GLog;
import com.info.aegis.mypluginapplication.plugin.Content;
import com.info.aegis.mypluginapplication.plugin.R;
import com.info.aegis.mypluginapplication.plugin.base.BaseActivity;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.ref.WeakReference;

public class WebviewActivity extends BaseActivity {

    RelativeLayout layout_rela;
    WebView txWebview;
    View view_top;

    ImageButton back_ib;
    RelativeLayout progress_layout;

    MyHandler myHandler;

    String url = "https://zgy.ai-risk-m.aegis-info.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        initViews();

        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(2, Content.TIME_GOBACK);

    }

    public void refreshHander(){
        myHandler.removeMessages(2);
        myHandler.sendEmptyMessageDelayed(2,Content.TIME_GOBACK);
    }

    private void initViews() {

        back_ib = (ImageButton) findViewById(R.id.back_ib);

        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        progress_layout.setVisibility(View.VISIBLE);

        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txWebview.canGoBack()) { // 表示按返回键
                    // 时的操作
                    txWebview.goBack(); // 后退
                    refreshHander();
                }else{
                    finish();
                }
            }
        });

        layout_rela = (RelativeLayout) findViewById(R.id.layout_rela);


        txWebview = (WebView)findViewById(R.id.tx_webview);

        view_top = findViewById(R.id.view_top);

        view_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                refreshHander();
                return false;
            }
        });

        txWebview.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        txWebview.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        txWebview.getSettings().setDisplayZoomControls(true); //隐藏原生的缩放控件
        txWebview.getSettings().setBlockNetworkImage(false);//解决图片不显示
        txWebview.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        txWebview.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        // 开启 DOM storage API 功能
        txWebview.getSettings().setDomStorageEnabled(true);
        txWebview.getSettings().setUseWideViewPort(true);
        txWebview.getSettings().setLoadWithOverviewMode(true);

//        txWebview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                refreshHander();
//                return false;
//            }
//        });

        txWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                txWebview.setVisibility(View.VISIBLE);
            }
        });

        txWebview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress_layout.setVisibility(View.GONE);
            }

        });

        if(txWebview.getX5WebViewExtension()!=null){
            GLog.e("asd","x5 core");
        }else{
            GLog.e("asd","sys core");
        }

        txWebview.loadUrl(url);


        txWebview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                progress_layout.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
            }

        });


    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        txWebview.onResume();
        txWebview.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        txWebview.onPause();
        txWebview.getSettings().setLightTouchEnabled(false);
    }

    //销毁 放置内存泄漏
    @Override
    public void onDestroy() {
        if (this.txWebview != null) {
            txWebview.setVisibility(View.GONE);
            txWebview.destroy();
        }
        super.onDestroy();
    }

    class MyHandler extends Handler{
        WeakReference<WebviewActivity> mActivity;

        public MyHandler(WebviewActivity activity) {
            mActivity = new WeakReference<WebviewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WebviewActivity activity = mActivity.get();
            if(activity!=null){
                activity.finish();
            }

        }
    }
}
