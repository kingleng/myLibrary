package com.example.leng.myapplication2.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.myView.yindaoye.GuideLayout;
import com.example.leng.myapplication2.view.myView.yindaoye.GuidePage;
import com.example.leng.myapplication2.view.myView.yindaoye.GuideUtil;
import com.example.myloginlibrary.MyLogin;
import com.example.myloginlibrary.Mylistener;

import java.util.Map;

public class Main5Activity extends AppCompatActivity implements View.OnClickListener {

    Button btn1,btn2,btn3;
    Button btn11,btn22,btn33;
    Button btn4;

    MyLogin myLogin;

    Mylistener mylistener;

    private FrameLayout mParentView;

    private int indexOfChild = -1;//使用anchor时记录的在父布局的位置

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
                    Log.e("asd","mylistener::xxxxxx key = "+key+"    value= "+data.get(key));
                }
            }
        };

        myLogin = new MyLogin(getApplicationContext(),mylistener);

//        View anchor = null;
//        if (anchor == null) {
//            anchor = getWindow().getDecorView();
//        }
//        if (anchor instanceof FrameLayout) {
//            mParentView = (FrameLayout) anchor;
//        } else {
//            FrameLayout frameLayout = new FrameLayout(this);
//            ViewGroup parent = (ViewGroup) anchor.getParent();
//            indexOfChild = parent.indexOfChild(anchor);
//            parent.removeView(anchor);
//            if (indexOfChild >= 0) {
//                parent.addView(frameLayout, indexOfChild, anchor.getLayoutParams());
//            } else {
//                parent.addView(frameLayout, anchor.getLayoutParams());
//            }
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
//                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            frameLayout.addView(anchor, lp);
//            mParentView = frameLayout;
//        }
//
//        showGuide();

        GuideUtil.newInstance(this).setTag("test").setShowCounts(2)
                .AddGuideLayout(new GuideLayout(Main5Activity.this,
                        GuidePage.newInstance()
                                .AddHighLight(btn1)
                                .setLayoutRes(R.layout.view_guide_simple)))
                .AddGuideLayout(new GuideLayout(Main5Activity.this,
                        GuidePage.newInstance()
                                .AddHighLight(btn2)
                                .setLayoutRes(R.layout.view_guide_simple)))
                .AddGuideLayout(new GuideLayout(Main5Activity.this,
                        GuidePage.newInstance()
                                .AddHighLight(btn3)
                                .setLayoutRes(R.layout.view_guide_simple)))
                .show();

    }

    private void showGuide(){
        GuideLayout guideLayout = new GuideLayout(Main5Activity.this,
                GuidePage.newInstance()
                        .AddHighLight(btn1)
                        .setLayoutRes(R.layout.view_guide_simple));
        addCustomToLayout(guideLayout);
        guideLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
            @Override
            public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                showGuide();
            }
        });

        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void addCustomToLayout(final GuideLayout guideLayout) {
        guideLayout.removeAllViews();
        View view = LayoutInflater.from(Main5Activity.this).inflate(guideLayout.mPage.mLayoutRes, guideLayout, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideLayout.remove();
            }
        });

        guideLayout.addView(view, params);
    }
}
