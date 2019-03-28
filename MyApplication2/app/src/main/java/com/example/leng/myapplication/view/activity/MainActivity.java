package com.example.leng.myapplication.view.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.myView.MyView;
import com.example.leng.myapplication.view.myView.MyView2;
import com.example.leng.myapplication.view.myView.MyView3;
import com.example.leng.myapplication.view.myView.MyView4;
import com.example.leng.myapplication.view.myView.MyView7;
import com.example.leng.myapplication.view.myView.MyView8;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyView myView;
    private MyView2 myView2;
    private MyView3 myView3;
    private MyView4 myView4;
    private MyView7 myView7;
    private MyView8 myView8;

    TextView myview_text;


    private AnimatorSet mAnimatorSet;
    private ObjectAnimator myview2Anim;

    private ObjectAnimator myview3Anim;
    private ObjectAnimator myview4Anim;

    private ObjectAnimator myview44Anim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (MyView)findViewById(R.id.myview);
        myView2 = (MyView2)findViewById(R.id.myview2);
        myView3 = (MyView3)findViewById(R.id.myview3);
        myView4 = (MyView4)findViewById(R.id.myview4);
        myView7 = (MyView7)findViewById(R.id.myview7);
        myView8 = (MyView8)findViewById(R.id.myview8);
        myview_text = (TextView)findViewById(R.id.myview_text);


        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main4Activity.class));
            }
        });


        final MyView4 btn1 = (MyView4)findViewById(R.id.ibtn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main5Activity.class));
            }
        });

        final MyView4 btn2 = (MyView4)findViewById(R.id.ibtn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main3Activity.class));
            }
        });
        final MyView4 btn3 = (MyView4)findViewById(R.id.ibtn3);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myView3.isOpen()){
                    if(myview3Anim != null){
                        myview3Anim.start();
                        myView3.setOpen(true);
                    }
                }else{
                    if(myview4Anim != null){
                        myview4Anim.start();
                        myView3.setOpen(false);
                    }
                }
            }
        });

        Animator();

        myView7.setAnimTime(10);
        myView7.startAnim();

        myView8.setDuretion(10);


        NewbieGuide.with(MainActivity.this)
                .setLabel("guide1")
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(myView)
                        .setLayoutRes(R.layout.view_guide_simple))
                .show();



    }

    private void Animator(){
        //myview
        List<Animator> animators = new ArrayList<Animator>();
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                myView, "myValue", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(2000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(myView, "rotation", 0F, 360F);//360度旋转
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(myview_text, "rotation", 0F, 360F);//360度旋转
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setDuration(10000);
        animator2.setInterpolator(new LinearInterpolator());

        animators.add(waveShiftAnim);
        animators.add(animator2);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);


        //myview2
        myview2Anim = ObjectAnimator.ofFloat(
                myView2, "progress", 0f, 1f);
        myview2Anim.setRepeatCount(ValueAnimator.INFINITE);
        myview2Anim.setDuration(20000);
        myview2Anim.setInterpolator(new LinearInterpolator());

        //myview3
        myview3Anim = ObjectAnimator.ofFloat(
                myView3, "openValue", 0f, 1f);
//        myview3Anim.setRepeatCount(ValueAnimator.INFINITE);
        myview3Anim.setDuration(200);
        myview3Anim.setInterpolator(new LinearInterpolator());
        myview4Anim = ObjectAnimator.ofFloat(
                myView3, "openValue", 1f, 0f);
//        myview3Anim.setRepeatCount(ValueAnimator.INFINITE);
        myview4Anim.setDuration(200);
        myview4Anim.setInterpolator(new LinearInterpolator());

        //myview4
        myview44Anim = ObjectAnimator.ofFloat(
                myView4, "myValue", 1f, 0f);
        myview44Anim.setRepeatCount(ValueAnimator.INFINITE);
        myview44Anim.setDuration(20000);
        myview44Anim.setInterpolator(new LinearInterpolator());

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
        if(myview2Anim != null){
            myview2Anim.start();
        }

        if(myview44Anim != null){
            myview44Anim.start();
        }

        myView8.startAnim();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
        if(myview2Anim != null){
            myview2Anim.end();
        }
        if(myview3Anim != null){
            myview3Anim.end();
        }
        if(myview4Anim != null){
            myview4Anim.end();
        }

        if(myview44Anim != null){
            myview44Anim.end();
        }

        myView8.stopAnim();

    }

}
