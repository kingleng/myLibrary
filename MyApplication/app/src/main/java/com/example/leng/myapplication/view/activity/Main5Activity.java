package com.example.leng.myapplication.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.leng.myapplication.R;
import com.example.myloginlibrary.MyLogin;
import com.example.myloginlibrary.Mylistener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

import java.util.Map;

public class Main5Activity extends AppCompatActivity implements View.OnClickListener {

    Button btn1,btn2,btn3;
    Button btn11,btn22,btn33;
    Button btn4;

    MyLogin myLogin;

    Mylistener mylistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);

        btn11 = (Button)findViewById(R.id.btn11);
        btn22 = (Button)findViewById(R.id.btn22);
        btn33 = (Button)findViewById(R.id.btn33);

        btn4 = (Button)findViewById(R.id.btn4);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn22.setOnClickListener(this);
        btn33.setOnClickListener(this);
        btn4.setOnClickListener(this);

        mylistener = new Mylistener() {
            @Override
            public void getData(Map<String, String> data) {
                for (String key : data.keySet()) {
                    com.umeng.socialize.utils.Log.e("mylistener::xxxxxx key = "+key+"    value= "+data.get(key));
                }
            }
        };

        myLogin = new MyLogin(getApplicationContext(),mylistener);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
                myLogin.loginQQ(Main5Activity.this);
                break;
            case R.id.btn11:
                myLogin.cancelQQ(Main5Activity.this);
                break;
            case R.id.btn2:
                myLogin.loginWEIXIN(Main5Activity.this);
                break;
            case R.id.btn22:
                myLogin.cancelWEIXIN(Main5Activity.this);
                break;
            case R.id.btn3:
                myLogin.loginSINA(Main5Activity.this);
                break;
            case R.id.btn33:
                myLogin.cancelSINA(Main5Activity.this);
                break;
            case R.id.btn4:
                myLogin.Share(Main5Activity.this);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("kakaoxx", "requestCode="+requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
