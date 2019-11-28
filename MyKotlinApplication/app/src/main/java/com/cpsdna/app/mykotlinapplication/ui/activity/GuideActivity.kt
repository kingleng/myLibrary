package com.cpsdna.app.mykotlinapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.cpsdna.app.mykotlinapplication.Content
import com.cpsdna.app.mykotlinapplication.R
import com.cpsdna.app.mykotlinapplication.SharePreHelper
import com.cpsdna.app.mykotlinapplication.net.CallServerNohttp
import com.cpsdna.app.mykotlinapplication.ui.MsgDialogFragment
import com.cpsdna.app.mykotlinapplication.ui.base.LibraryActivity
import com.cpsdna.app.mykotlinapplication.util.CustomToast
import com.cpsdna.app.mykotlinapplication.util.GLog.GLog
import com.cpsdna.app.mykotlinapplication.util.UtilMethod
import com.yanzhenjie.nohttp.NoHttp
import com.yanzhenjie.nohttp.RequestMethod
import com.yanzhenjie.nohttp.rest.OnResponseListener
import com.yanzhenjie.nohttp.rest.Request
import com.yanzhenjie.nohttp.rest.Response
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.text.MessageFormat

class GuideActivity : LibraryActivity() {

    private lateinit var context: Context
    private lateinit var videoView: VideoView
    private lateinit var nextActivityText: TextView
    private lateinit var qrImg: ImageView
    private lateinit var idToast: TextView
    private lateinit var deviceIdText: TextView

    private lateinit var timeHandler: MyHandler

    class MyHandler(a: GuideActivity) : Handler() {
        private var mActivity: WeakReference<GuideActivity> = WeakReference(a)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity = mActivity.get()
            activity?.nextActivityText?.callOnClick()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        context = this

        videoView = findViewById(R.id.id_video)
        nextActivityText = findViewById(R.id.id_next_activity)
        qrImg = findViewById(R.id.id_qr_img)
        idToast = findViewById(R.id.id_toast)
        deviceIdText = findViewById(R.id.id_device_id)

        nextActivityText.visibility = View.INVISIBLE
        idToast.visibility = View.INVISIBLE

        timeHandler = MyHandler(this)

        startPlayVideoAnimation()

        nextActivityText.setOnClickListener {
            timeHandler.removeMessages(0)
            startActivity(Intent(this@GuideActivity, PHomeActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.id_set).setOnClickListener(OnClickListener {
            val wm = this@GuideActivity
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val width = wm.defaultDisplay.width
            val height = wm.defaultDisplay.height
            val wm1: WindowManager = this@GuideActivity.windowManager
            val width1 = wm1.defaultDisplay.width
            val height1 = wm1.defaultDisplay.height
            val manager: WindowManager = this@GuideActivity.windowManager
            val outMetrics = DisplayMetrics()
            manager.defaultDisplay.getMetrics(outMetrics)
            val width2 = outMetrics.widthPixels
            val height2 = outMetrics.heightPixels
            val resources: Resources = this@GuideActivity.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val density = dm.density
            val width3 = dm.widthPixels
            val height3 = dm.heightPixels
            CustomToast.showLongToast("width:$width, height:" + height
                    .toString() + "\nwidth1:" + width1.toString() + ", height1:" + height1
                    .toString() + "\nwidth2:" + width2.toString() + ", height2:" + height2
                    .toString() + "\nwidth3:" + width3.toString() + ", height3:" + height3)
        })


        deviceIdText.text = "当前设备id：" + SharePreHelper.getDeviceId()
        Glide.with(this)
                .load("http://ng.aegis-info.com:8500/api/tools/qr_code?url=" + SharePreHelper.getDeviceId())
                .into(qrImg)

        getdata()

    }

    fun getdata() {
        if (isNetWorkAvailable(this) && isOnline) {
            idToast.visibility = View.INVISIBLE
            if (TextUtils.isEmpty(SharePreHelper.getDeviceId())) {
                //获得设备ID
                val deviceId: String = UtilMethod.generateUniqueDeviceId()
                SharePreHelper.setDeviceId(deviceId)
            }
            getUpdataInfo()
        } else {
//            Toast.makeText(this,"没有连接网络，正在尝试重新连接，请稍后！",Toast.LENGTH_SHORT).show();

            idToast.text = "没有连接网络，正在尝试重新连接，请稍后！"
            idToast.visibility = View.VISIBLE
            Handler().postDelayed({ getdata() }, 3000)
        }
    }

    private fun getUpdataInfo() {
        val deviceId: String? = SharePreHelper.getDeviceId()
        val url: String? = MessageFormat.format("{0}/apkVersion?deviceId={1}&apkName={2}", Content.WEB_PATH, deviceId, Content.APK_NAME)
        val request: Request<JSONObject?> = NoHttp.createJsonObjectRequest(url, RequestMethod.GET)
        CallServerNohttp.getInstance().add(0, request, object : OnResponseListener<JSONObject?> {
            override fun onStart(what: Int) {}
            override fun onSucceed(what: Int, response: Response<JSONObject?>) {
                val jsonObject: JSONObject ?= response.get()
                GLog.e(response.toString())
                if(jsonObject==null){
                    return
                }
                if (jsonObject.has("data")) {
                    val data: JSONObject? = jsonObject.optJSONObject("data")
                    if (data != null) {
                        val robotCreateTime = data.optLong("robotCreateTime")
                        val robotDueTime = data.optLong("robotDueTime")
                        val currentTime = System.currentTimeMillis()
                        var isActive = false
                        if (currentTime > robotCreateTime && currentTime < robotDueTime) {
                            isActive = true
                        }
                        if (isActive) {

//                            Toast.makeText(GuideActivity.this,"正在获取设备基本信息，请稍等。",Toast.LENGTH_LONG).show();

                            idToast.text = "正在获取设备基本信息，请稍等。"
                            idToast.visibility = View.VISIBLE
                            val request: Request<JSONObject?> = NoHttp.createJsonObjectRequest(MessageFormat.format("{0}/robot/android/?deviceId={1}", Content.WEB_PATH, SharePreHelper.getDeviceId()), RequestMethod.GET)
                            CallServerNohttp.getInstance().add(0, request, object : OnResponseListener<JSONObject?> {
                                override fun onStart(what: Int) {}
                                override fun onSucceed(what: Int, response: Response<JSONObject?>) {
                                    val jsonObject2: JSONObject ?= response.get()
                                    if(jsonObject2 == null){
                                        return
                                    }
                                    if (jsonObject2.has("code") && jsonObject2.optInt("code") == 0 && jsonObject2.has("data")) {
                                        val data2: JSONObject = jsonObject2.optJSONObject("data")
                                        Content.province = data2.optString("province")
                                        Content.city = data2.optString("city")
                                        Content.organization = data2.optString("organization")

                                    }
                                    handData(jsonObject)
                                }

                                override fun onFailed(what: Int, response: Response<JSONObject?>?) {
                                    handData(jsonObject)
                                }

                                override fun onFinish(what: Int) {}
                            })
                        } else {
                            Toast.makeText(this@GuideActivity, "设备未激活，请先激活设备！", Toast.LENGTH_LONG).show()
                            idToast.text = "设备未激活，请先激活设备！"
                            idToast.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(this@GuideActivity, "设备未激活，请先激活设备！", Toast.LENGTH_LONG).show()
                        idToast.text = "设备未激活，请先激活设备！"
                        idToast.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this@GuideActivity, "设备未激活，请先激活设备！", Toast.LENGTH_LONG).show()
                    idToast.text = "设备未激活，请先激活设备！"
                    idToast.visibility = View.VISIBLE
                }
            }

            override fun onFailed(what: Int, response: Response<JSONObject?>?) {
                Toast.makeText(this@GuideActivity, "接口连接失败，正在重试，请稍等。", Toast.LENGTH_LONG).show()
                getUpdataInfo()
            }

            override fun onFinish(what: Int) {}
        })
    }

    private fun handData(jsonObject: JSONObject) {
        val data: JSONObject = jsonObject.optJSONObject("data")
        nextActivityText.visibility = View.VISIBLE
        if (jsonObject.has("code") && jsonObject.optInt("code") == 1) {
            val msgDialogFragment = MsgDialogFragment()
            val bundle = Bundle()
            bundle.putString("apk_url", data.optString("apkUrl"))
            bundle.putString("apk_name", data.optString("apkName"))
            msgDialogFragment.setArguments(bundle)
//                                    msgDialogFragment.show(getFragmentManager(), "dialog_fragment");


            msgDialogFragment.show(supportFragmentManager, "dialog_fragment")
        } else {
            timeHandler.sendEmptyMessageDelayed(0, 5000)
            val deviceId: String? = SharePreHelper.getDeviceId()
            val url: String? = MessageFormat.format("{0}/apkVersion/update?deviceId={1}&apkName={2}.apk", Content.WEB_PATH, deviceId, Content.APK_NAME)
            val request: Request<JSONObject?> = NoHttp.createJsonObjectRequest(url, RequestMethod.GET)
            CallServerNohttp.getInstance().add(0, request, object : OnResponseListener<JSONObject?> {
                override fun onStart(what: Int) {}
                override fun onSucceed(what: Int, response: Response<JSONObject?>) {
                    val jsonObject: JSONObject ?= response.get()
                    if(jsonObject==null){
                        return
                    }
                    if (jsonObject.has("code") && jsonObject.optInt("code") == 1 && jsonObject.has("data")) {
                        val data: JSONObject? = jsonObject.optJSONObject("data")
                    }
                }

                override fun onFailed(what: Int, response: Response<JSONObject?>?) {}
                override fun onFinish(what: Int) {}
            })
        }
    }


    override fun onResume() {
        super.onResume()
        resumeVideoAnimation()
    }

    override fun onPause() {
        super.onPause()
        pauseVideoAnimation()
    }

    override fun onDestroy() {
        timeHandler.removeMessages(0)
        super.onDestroy()
    }

    internal fun startPlayVideoAnimation() {
        val uri = "android.resource://" + packageName.toString() + "/" + R.raw.guide_animation
        videoView.setVideoURI(Uri.parse(uri))
        videoView.setOnCompletionListener { mPlayer ->
            mPlayer.start()
            mPlayer.isLooping = true
        }
        videoView.start()
    }

    internal fun resumeVideoAnimation() {
        if (!videoView.isPlaying) startPlayVideoAnimation()
    }

    internal fun pauseVideoAnimation() {
        videoView.pause()
    }


}
