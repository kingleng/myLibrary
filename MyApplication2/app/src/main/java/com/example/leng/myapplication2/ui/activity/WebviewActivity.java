package com.example.leng.myapplication2.ui.activity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.FloatDragView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.ref.WeakReference;

import static com.alibaba.mtl.log.a.getContext;

public class WebviewActivity extends AppCompatActivity {

    WebView txWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String url = getIntent().getStringExtra("url");

//        url = "https://www.taobao.com/";

//        //原生webview
//        webview = findViewById(R.id.webview);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webview.loadUrl(url);

        initView();

        txWebview = findViewById(R.id.webview_x5);

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

        //该界面打开更多链接
        txWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);

            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);

            }

        });
        //监听网页的加载进度
        txWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                webView.setVisibility(View.VISIBLE);
            }


        });

        if(txWebview.getX5WebViewExtension()!=null){
            Log.e("asd","x5 core");
        }else{
            Log.e("asd","sys core");
            initX5();
        }

        txWebview.loadUrl(url);

    }

    public void initView(){
        RelativeLayout rela_layout = (RelativeLayout) findViewById(R.id.rela_layout);
        FloatDragView.addFloatDragView(this, rela_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击事件
                if (txWebview.canGoBack()) { // 表示按返回键
                    // 网页可以后退时的操作
                    txWebview.goBack(); // 后退
                }else{
                    finish();
                }
            }
        });
    }

    private void initX5() {
        Log.e("asd","1 --- 准备加载浏览器插件");
        showProgressDialog();
        QbSdk.reset(getContext());
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.PreInitCallback preInitCallback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e("asd","初始化插件失败,请重试！");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                if (!b){
                    Log.e("asd","加载失败");
                }else {
                    Log.e("asd","加载成功");
                }
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Message message = new Message();
                message.what = 2;
                message.obj = "插件下载成功, 自动安装中...";
                handler.sendMessage(message);
//
                Message message2 = new Message();
                message2.what = 1;
                message2.arg1 = 100;
                handler.sendMessage(message2);
            }

            @Override
            public void onInstallFinish(int i) {
                if (i == ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    Message message = new Message();
                    message.what = 3;
                    message.obj = "插件安装成功,需要重启APP！";
                    handler.sendMessage(message);
                    return;
                }
                Message message = new Message();
                message.what = 2;
                message.obj = "插件加载失败！";
                handler.sendMessage(message);
            }

            @Override
            public void onDownloadProgress(int i) {
                Message message = new Message();
                message.what = 1;
                message.arg1 = i;
                handler.sendMessage(message);
            }
        });
        QbSdk.initX5Environment(this, preInitCallback);
    }

    //实例化一个MyHandler对象
    MyHandler handler = new MyHandler(this);

    static class MyHandler extends Handler {

        WeakReference<WebviewActivity> activity;
        MyHandler(WebviewActivity fragment0) {
            activity = new WeakReference<WebviewActivity>(fragment0);
        }

        @Override
        public void handleMessage(Message msg) {
            WebviewActivity x5SettingWebviewFragment = activity.get();

            if(x5SettingWebviewFragment!=null){
                switch (msg.what){
                    case 1:
                        if(x5SettingWebviewFragment.progressDialog!=null){
                            x5SettingWebviewFragment.progressDialog.setMessage("下载进度 ：" +  msg.arg1);
                        }
                        break;
                    case 2:
                        if(x5SettingWebviewFragment.progressDialog!=null){
                            x5SettingWebviewFragment.progressDialog.setMessage((CharSequence) msg.obj);
                        }
                        break;
                    case 3:
                        if(x5SettingWebviewFragment.progressDialog!=null){
                            x5SettingWebviewFragment.progressDialog.setMessage((CharSequence) msg.obj);
                        }

                        Message message = new Message();
                        message.what = 4;
                        message.arg1 = 5;
                        x5SettingWebviewFragment.handler.sendMessageDelayed(message,1000);
                        break;
                    case 4:
                        int time = msg.arg1;
                        if (time > 0){
                            if(x5SettingWebviewFragment.progressDialog!=null){
                                x5SettingWebviewFragment.progressDialog.setMessage(time + "  秒后自动重启");
                            }

                            time --;
                            Message message2 = new Message();
                            message2.what = 4;
                            message2.arg1 = time;
                            x5SettingWebviewFragment.handler.sendMessageDelayed(message2,1000);
                        }else {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                        break;
                }
            }

        }
    }

    private AlertDialog progressDialog;
    private void showProgressDialog() {
        // 进度条还有二级进度条的那种形式，这里就不演示了
        progressDialog = new AlertDialog.Builder(this)
                .setTitle("X5内核加载中。。。")
                .setMessage("插件正在下载，请耐心等待(#^.^#)")
                .create();
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        txWebview.onResume();
        txWebview.getSettings().setJavaScriptEnabled(true);

        fullScreen();

    }

    @Override
    public void onPause() {
        super.onPause();
        txWebview.onPause();
        txWebview.getSettings().setLightTouchEnabled(false);

        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
        handler.removeMessages(4);

    }

    //销毁 放置内存泄漏
    @Override
    public void onDestroy() {
        if (this.txWebview != null) {
            txWebview.destroy();
        }
        super.onDestroy();
    }

    /**
     * 全屏
     */
    public void fullScreen() {
        //API19以上，隐藏底部虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        try {
            //隐藏导航栏
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();

            if (actionBar != null)
                actionBar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
