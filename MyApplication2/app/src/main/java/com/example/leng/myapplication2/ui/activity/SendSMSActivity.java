package com.example.leng.myapplication2.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

import com.example.leng.myapplication2.R;

public class SendSMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);//android.intent.action.SENDTO
                //2). 携带数据(号码/内容)
                String number = "13914780025";
                String sms = "测试短信！";
                intent.setData(Uri.parse("smsto:"+number));
                //携带额外数据
                intent.putExtra("sms_body", sms);
                //3). startActivity(intent)
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1). 得到SmsManager的对象
                SmsManager smsManager = SmsManager.getDefault();
                //2). 发送文本信息(短信)
                String number = "13914780025";
                String sms = "测试短信！";
                smsManager.sendTextMessage(number, null, sms, null, null);
            }
        });
    }
}
