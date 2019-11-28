package com.cpsdna.app.mykotlinapplication.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.http.SslError
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import android.widget.Toast
import com.cpsdna.app.mykotlinapplication.R
import java.io.File
import java.io.IOException


class WebActivity : AppCompatActivity() {

    val LOG_TAG: String = WebActivity::class.java.simpleName

    private lateinit var webView: WebView

    private var recorder  // 录音类
            : MediaRecorder? = null
    private var fileName  // 录音生成的文件存储路径
            : String? = null

//    lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val mUrl:String ?= intent.getStringExtra("url")


        webView = findViewById(R.id.webview)

//        webView.getSettings().setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
//
//        webView.getSettings().setBuiltInZoomControls(true) //设置内置的缩放控件。若为false，则该WebView不可缩放
//
//        webView.getSettings().setDisplayZoomControls(true) //隐藏原生的缩放控件
//
//        webView.getSettings().setBlockNetworkImage(false)//解决图片不显示
//
//        webView.getSettings().setLoadsImagesAutomatically(true) //支持自动加载图片
//
//        webView.getSettings().setDefaultTextEncodingName("utf-8")//设置编码格式
//
//        // 开启 DOM storage API 功能
//
//        // 开启 DOM storage API 功能
//        // 开启 DOM storage API 功能
//        webView.getSettings().setDomStorageEnabled(true)
//        webView.getSettings().setUseWideViewPort(true)
//        webView.getSettings().setLoadWithOverviewMode(true)

//        txWebview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                refreshHander();
//                return false;
//            }
//        });


//        val settings: WebSettings = webView.settings
//        settings.allowContentAccess = true // 是否可访问Content Provider的资源，默认值 true
//        settings.allowFileAccess = true // 设置允许访问文件数据
//        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
//        settings.allowFileAccessFromFileURLs = true
//        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
//        settings.allowUniversalAccessFromFileURLs = true


//        webView.setWebViewClient(object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                if(!TextUtils.isEmpty(mUrl)){
//                    webView.loadUrl(mUrl)
//                }else{
//                    webView.loadUrl("file:///android_asset/test.html")
//                }
//                return true
//            }
//
//            override fun onReceivedSslError(webView: WebView?, sslErrorHandler: SslErrorHandler, sslError: SslError?) {
//                sslErrorHandler.proceed()
//            }
//
//
//        })

        if(!TextUtils.isEmpty(mUrl)){
            webView.loadUrl(mUrl)
        }else{
            webView.loadUrl("file:///android_asset/test.html")
        }

        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordertest.wav"

        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermission(Manifest.permission.RECORD_AUDIO)

        webView.addJavascriptInterface(object : Any() {
            //定义要调用的方法
            //msg由js调用的时候传递
            @JavascriptInterface
            fun startrecord() {
                toast( "startrecord")
                startRecord()
            }

            @JavascriptInterface
            fun stoprecord() :String{
                toast( "stoprecord")
                return stopRecord()
            }
        }, "JSTest")

//        mediaPlayer = MediaPlayer()
//
//        mediaPlayer.setDataSource("file:///storage/emulated/0/audiorecordertest.mp3") // 设置播放的文件位置
//
//        mediaPlayer.prepare() // 准备文件
//
//        mediaPlayer.start()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.stop()
//        mediaPlayer.release()
//    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun startRecord():Boolean {
        Log.e(LOG_TAG, "startRecord")

        recorder = MediaRecorder()
        try {
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        }catch (e:IllegalStateException){
            Log.i(LOG_TAG, "设置录音源失败")
            e.printStackTrace()
        }

        recorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
        recorder?.setOutputFile(fileName)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder?.prepare()
        }catch (e: IOException){
            Log.e(LOG_TAG, "准备失败")
            e.printStackTrace()
        }
        recorder?.start()
        Log.i(LOG_TAG, "开始录音...")
        return true
    }

    fun stopRecord() :String{
        Log.e(LOG_TAG, "stopRecord")

        recorder!!.stop()
        recorder!!.reset()
        recorder!!.release()
        recorder = null
        Log.e(LOG_TAG, "停止录音"+fileName.toString())

        val file = File(Environment.getExternalStorageDirectory(), "audiorecordertest.wav")
        Log.e(LOG_TAG, "停止录音"+file.getPath())


        return fileName.toString()
    }


    private fun requestPermission(permission:String){
        if(ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        }
    }

    override fun onResume() {
        super.onResume()
        val settings: WebSettings = webView.getSettings()
        settings.javaScriptEnabled = true //允许在WebView中使用js

    }
}
