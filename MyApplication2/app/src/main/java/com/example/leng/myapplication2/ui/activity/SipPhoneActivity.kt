package com.example.leng.myapplication2.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.example.leng.myapplication2.R
import com.example.leng.myapplication2.base.BaseActivity
import com.info.aegis.linphonelibrary.LinphoneManager
import com.info.aegis.linphonelibrary.callback.CallState
import com.info.aegis.linphonelibrary.callback.RegiserationState
import kotlinx.android.synthetic.main.activity_sip_phone.*
import org.linphone.core.Call
import java.util.*

class SipPhoneActivity : BaseActivity() {

    private val TAG = "CallActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sip_phone)

        initView()
        initLinphone()
    }

    private fun initView() {
//        phone_number_text = findViewById(R.id.phone_number_text)
//        registeration_icon = findViewById(R.id.registeration_icon)
//        recyclerview = findViewById(R.id.recyclerview)
//        call_btn = findViewById(R.id.call_btn)
//        accept_call = findViewById(R.id.accept_call)
//        terminate_call = findViewById(R.id.terminate_call)
        //        call_layout = findViewById(R.id.call_layout);
//        call_gone = findViewById(R.id.call_gone);
//        call_start = findViewById(R.id.call_start);
//        call_back = findViewById(R.id.call_back);
//        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.setAdapter(object : QuickAdapter<String>(data) {
//            override fun getLayoutId(viewType: Int): Int {
//                return R.layout.layout_call_log
//            }
//
//            override fun convert(holder: VH?, data: String, position: Int) {}
//        })
        call_btn.setOnClickListener(View.OnClickListener {
            val num = phone_number_text.text.toString()
            if (TextUtils.isEmpty(num)) {
                return@OnClickListener
            }
            call(num)
        })
        terminate_call.setOnClickListener { LinphoneManager.getInstance().terminateCall() }

    }

    override fun onResume() {
        super.onResume()
        checkAndRequestCallPermissions()
    }



    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 初始化Linphone
     */
    private fun initLinphone() {
        Log.d(TAG,"开始初始化Linphone")
        LinphoneManager.getInstance().startService(this, { login() })
    }

    /**
     * 登录SIP
     */
    private fun login() {
        LinphoneManager.getInstance().addListener(object : RegiserationState{
            override fun registrationNone() {
                Log.d(TAG,"registrationNone")
                registeration_icon.setImageResource(R.drawable.led_disconnected)
            }

            override fun registrationProgress() {
                Log.d(TAG,"registrationProgress")
                registeration_icon.setImageResource(R.drawable.led_inprogress)
                toastCenter("registrationProgress")
            }

            override fun registrationOk() {
                Log.d(TAG,"registrationOk")
                registeration_icon.setImageResource(R.drawable.led_connected)
                toastCenter("registrationOk")
            }

            override fun registrationCleared() {
                Log.d(TAG,"registrationCleared")
                registeration_icon.setImageResource(R.drawable.led_disconnected)
                toastCenter("registrationCleared")
            }

            override fun registrationFailed() {
                Log.d(TAG,"registrationFailed")
                registeration_icon.setImageResource(R.drawable.led_error)
                toastCenter("registrationFailed")
            }
        }, object : CallState {
            override fun connected() {
                toastCenter("CallState >> connected")
            }

            override fun end() {
                toastCenter("CallState >> end")
            }

            override fun outgoingInit() {
                toastCenter("CallState >> outgoingInit")
            }

            override fun incomingCall(call: Call?) {
                toastCenter("CallState >> incomingCall")
                accept_call.setOnClickListener(View.OnClickListener { LinphoneManager.getInstance().acceptCall(call) })
            }
        })
        val mRobotPhone = "1001"
        val mPassword = "aegis2020"
        val mDomain = "114.116.25.112:9060"
        val mPhoneNumber = "001"
        Log.d(TAG, "开始登录Linphone $mRobotPhone $mPassword $mDomain $mPhoneNumber")
        LinphoneManager.getInstance().configureAccount(mRobotPhone, mPassword, mDomain, mPhoneNumber)
    }

    /**
     * 呼叫
     */
    private fun call(mPhoneNumber: String) {
        LinphoneManager.getInstance().toCall(mPhoneNumber, false)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // Callback for when permissions are asked to the user
        for (i in permissions.indices) {
            Log.i(TAG, "[Permission] "
                    + permissions[i]
                    + " is "
                    + if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "granted" else "denied")
        }
    }

    private fun checkAndRequestCallPermissions() {
        val permissionsList = ArrayList<String>()

        // Some required permissions needs to be validated manually by the user
        // Here we ask for record audio and camera to be able to make video calls with sound
        // Once granted we don't have to ask them again, but if denied we can
        val recordAudio = packageManager
                .checkPermission(Manifest.permission.RECORD_AUDIO, packageName)
        Log.i(TAG, "[Permission] Record audio permission is "
                + if (recordAudio == PackageManager.PERMISSION_GRANTED) "granted" else "denied")
        val camera = packageManager.checkPermission(Manifest.permission.CAMERA, packageName)
        Log.i(TAG, "[Permission] Camera permission is "
                + if (camera == PackageManager.PERMISSION_GRANTED) "granted" else "denied")
        if (recordAudio != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "[Permission] Asking for record audio")
            permissionsList.add(Manifest.permission.RECORD_AUDIO)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "[Permission] Asking for camera")
            permissionsList.add(Manifest.permission.CAMERA)
        }
        if (permissionsList.size > 0) {
            var permissions: Array<String?>? = arrayOfNulls(permissionsList.size)
            permissions = permissionsList.toArray(permissions)
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }
}
