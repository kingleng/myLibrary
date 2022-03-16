package com.info.aegis.mypluginapplication.plugin.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.info.aegis.baselibrary.SharePreHelper;
import com.info.aegis.baselibrary.base.LibraryFragment;
import com.info.aegis.baselibrary.router.BaseModule;
import com.info.aegis.baselibrary.utils.CustomToast;
import com.info.aegis.baselibrary.utils.QRCodeUtils;
import com.info.aegis.mypluginapplication.plugin.R;
import com.info.aegis.mypluginapplication.plugin.base.BaseFragment;
import com.info.aegis.mypluginapplication.plugin.model.Case2LawBean;
import com.info.aegis.mypluginapplication.plugin.model.CaseBean;
import com.info.aegis.mypluginapplication.plugin.model.LawBean;
import com.info.aegis.mypluginapplication.plugin.utils.JsonParser;
import com.info.aegis.mypluginapplication.plugin.utils.JsonUtil;
import com.info.aegis.mypluginapplication.plugin.utils.SlideUtil;
import com.info.aegis.mypluginapplication.plugin.utils.SlideUtil2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZnwdFragment extends BaseFragment {


    public ZnwdFragment() {
        // Required empty public constructor
    }

    private TextView rebotName;
    private ScrollView scrollview;
    private LinearLayout lin_layout;
    private EditText edit_text;
    private ImageButton send_btn;
    private View rela_botton_bg;
    private RelativeLayout rela_botton;
    private ImageView rela_botton_btn_layout_icon;
    private TextView rela_botton_title;
    private TextView rela_botton_title2;
    private TextView rela_botton_btn1;
    private TextView rela_botton_btn2;
    private TextView rela_botton_btn3;
    private TextView rela_botton_btn4;
    private LinearLayout rela_botton_lin;
    private ImageButton back_ib;
    private ImageButton video_btn;
    private ImageView voice_bg;

    private Case2LawBean case2LawBean;

    SlideUtil mSlideUtil;

    private boolean isAddOther = false;

    private int SLayoutHeight = 0;

    private String qr_code;

    SlideUtil2 mSlideUtil2;
    LinearLayout share_layout;
    ImageView share_iv;
    View share_layout_bg;

    private String content = "";
    //    private String qid = "";
    private String event_ids = "1";
    private String industry_ids = "1";
    //    private String user_id = "test_userA";
    private String category = "";
    private String location = "南京";
    private String rebot_name = "南京擎盾";

    String answer;
    List<LawBean> laws = new ArrayList<>();
    List<String> choices = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            qr_code = getArguments().getString("qr_code");
            event_ids = getArguments().getString("event_ids");
            rebot_name = getArguments().getString("rebot_name");
            location = getArguments().getString("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_znwd, container, false);
        initView(view);

        rebotName.setText(rebot_name);

        isAddOther = false;
        content = "你是谁";
        getData("");

        setVovice();

        return view;
    }

    private void initView(View view) {
        rebotName = view.findViewById(R.id.rebot_name);

        scrollview = view.findViewById(R.id.scrollview);
        lin_layout = view.findViewById(R.id.lin_layout);
        edit_text = view.findViewById(R.id.edit_text);
        send_btn = view.findViewById(R.id.send_btn);

        back_ib = view.findViewById(R.id.back_ib);
        video_btn = view.findViewById(R.id.video_btn);
        voice_bg = view.findViewById(R.id.voice_bg);
        voice_bg.setVisibility(View.GONE);


        lin_layout.removeAllViews();

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = edit_text.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    send_btn.setEnabled(false);
                } else {
                    send_btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send_btn.setEnabled(false);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                addItem();
                getData("");

                edit_text.setText("");
            }
        });

        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null){
                    getActivity().finish();
                }

            }
        });

        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoice();
                Glide.with(view.getContext())
                        .load(R.drawable.z_voice_bg)
                        .into(voice_bg);
                voice_bg.setVisibility(View.VISIBLE);
            }
        });


        /*********************************  底部弹框（以案说法） *********************************************/
        rela_botton_bg = view.findViewById(R.id.rela_botton_bg);
        rela_botton = view.findViewById(R.id.rela_botton);
        rela_botton_btn_layout_icon = view.findViewById(R.id.rela_botton_btn_layout_icon);
        rela_botton_title = view.findViewById(R.id.rela_botton_title);
        rela_botton_title2 = view.findViewById(R.id.rela_botton_title2);

        rela_botton_btn1 = view.findViewById(R.id.rela_botton_btn1);
        rela_botton_btn2 = view.findViewById(R.id.rela_botton_btn2);
        rela_botton_btn3 = view.findViewById(R.id.rela_botton_btn3);
        rela_botton_btn4 = view.findViewById(R.id.rela_botton_btn4);
        rela_botton_lin = view.findViewById(R.id.rela_botton_lin);

        mSlideUtil = new SlideUtil.Builder(rela_botton)
                .withDistance(1632)
                .build();
        mSlideUtil.hide(false);
        rela_botton.setVisibility(View.GONE);
        rela_botton_bg.setVisibility(View.GONE);
        rela_botton_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideUtil.hide(true);
                rela_botton_bg.setVisibility(View.GONE);
            }
        });
        rela_botton_btn1.setOnClickListener(v -> {
            changeBottonBtn(0);
        });
        rela_botton_btn2.setOnClickListener(v -> {
            changeBottonBtn(1);
        });
        rela_botton_btn3.setOnClickListener(v -> {
            changeBottonBtn(2);
        });
        rela_botton_btn4.setOnClickListener(v -> {
            changeBottonBtn(3);
        });


        /*********************************  扫码弹框 *********************************************/
        share_layout = (LinearLayout) view.findViewById(R.id.share_layout);
        share_iv = (ImageView) view.findViewById(R.id.share_iv);
        share_layout_bg = view.findViewById(R.id.share_layout_bg);

        if(!TextUtils.isEmpty(qr_code)){
            share_layout.setVisibility(View.VISIBLE);
        }else{
            share_layout.setVisibility(View.GONE);
        }

        mSlideUtil2 = new SlideUtil2.Builder(share_layout)
//                .withDistance(DensityUtil.dip2px(this, 100))
                .withDistance(200)
                .build();

        mSlideUtil2.hide(false);
        share_layout_bg.setVisibility(View.GONE);

        share_layout_bg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mSlideUtil2.isShow()) {
                    mSlideUtil2.hide(true);
                    share_layout_bg.setVisibility(View.GONE);
                }
                return false;
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mSlideUtil2.isShow()) {
                    mSlideUtil2.hide(true);
                    share_layout_bg.setVisibility(View.GONE);
                } else {
                    mSlideUtil2.canShow = true;
                    mSlideUtil2.show(true);
                    share_layout_bg.setVisibility(View.VISIBLE);
                }

            }
        });

        if(!TextUtils.isEmpty(qr_code)){
            share_layout.setVisibility(View.VISIBLE);
            QRCodeUtils.createQRcodeImage(qr_code,share_iv);
        }
    }

    private void changeBottonBtn(int position){
        rela_botton_lin.removeAllViews();



        if(case2LawBean == null){
            return;
        }

        rela_botton_title.setText(case2LawBean.title);
        rela_botton_title2.setText(case2LawBean.casenum);

        TextView textView = new TextView(rela_botton_lin.getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int) getResources().getDimension(R.dimen.px40);
        textView.setLayoutParams(lp);
        if(position == 0){
            rela_botton_btn1.setBackgroundResource(R.drawable.z_btn_bg);
            rela_botton_btn1.setTextColor(getResources().getColor(R.color.color_ffffff));
            rela_botton_btn2.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn2.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn3.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn3.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn4.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn4.setTextColor(getResources().getColor(R.color.color_65799b));
            textView.setText(case2LawBean.reason);
        }else if(position == 1){
            rela_botton_btn1.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn1.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn2.setBackgroundResource(R.drawable.z_btn_bg);
            rela_botton_btn2.setTextColor(getResources().getColor(R.color.color_ffffff));
            rela_botton_btn3.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn3.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn4.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn4.setTextColor(getResources().getColor(R.color.color_65799b));
            textView.setText(case2LawBean.claim);
        }else if(position == 2){
            rela_botton_btn1.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn1.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn2.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn2.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn3.setBackgroundResource(R.drawable.z_btn_bg);
            rela_botton_btn3.setTextColor(getResources().getColor(R.color.color_ffffff));
            rela_botton_btn4.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn4.setTextColor(getResources().getColor(R.color.color_65799b));
            textView.setText(case2LawBean.content);
        }else if(position == 3){
            rela_botton_btn1.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn1.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn2.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn2.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn3.setBackgroundResource(R.drawable.z_btn_bg2);
            rela_botton_btn3.setTextColor(getResources().getColor(R.color.color_65799b));
            rela_botton_btn4.setBackgroundResource(R.drawable.z_btn_bg);
            rela_botton_btn4.setTextColor(getResources().getColor(R.color.color_ffffff));
            textView.setText(case2LawBean.result);
        }

        textView.setLineSpacing(0, 1.5f);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.px24));
        rela_botton_lin.addView(textView);
    }

    /**
     * 热门问题
     **/
    private void addHotItem() {

        View view = LayoutInflater.from(lin_layout.getContext()).inflate(R.layout.layout_hot, lin_layout, false);
        View refresh_layout = view.findViewById(R.id.refresh_layout);
        ImageView refresh_layout_iv = view.findViewById(R.id.refresh_layout_iv);
        LinearLayout hot_lin = view.findViewById(R.id.hot_lin);



        refresh_layout.setOnClickListener(v -> {

            refresh_layout_iv.clearAnimation();
            RotateAnimation rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            LinearInterpolator lin = new LinearInterpolator();
            rotate.setInterpolator(lin);
            rotate.setDuration(1000);//设置动画持续时间
            rotate.setRepeatCount(0);//设置重复次数
            rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
            rotate.setStartOffset(0);//执行前的等待时间
            refresh_layout_iv.setAnimation(rotate);

            String event_id = "1";
            String count = "5";
            String url = MessageFormat.format("https://xiaofa.aegis-info.com/api/law_inference/simple/recommend?event_id={0}&count={1}", event_id, count);
            requstInfo(url, new LibraryFragment.NetMsgListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSucceed(Object o) {
                    JSONObject jsonObject = (JSONObject) o;
                    if (jsonObject.has("data")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            hot_lin.removeAllViews();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String title = jsonArray.opt(i).toString();
                                TextView textView = new TextView(hot_lin.getContext());
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                lp.topMargin = (int) getResources().getDimension(R.dimen.px20);
                                lp.bottomMargin = (int) getResources().getDimension(R.dimen.px20);
                                lp.leftMargin = (int) getResources().getDimension(R.dimen.px60);
                                lp.rightMargin = (int) getResources().getDimension(R.dimen.px60);
                                textView.setLayoutParams(lp);
                                textView.setText(title);
                                textView.setLineSpacing(0, 1.5f);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.px24));
                                textView.setTextColor(getResources().getColor(R.color.color_007eff));
                                String key = title;
                                textView.setOnClickListener(v -> {
                                    ((TextView)v).setTextColor(v.getContext().getResources().getColor(R.color.color_113260));

                                    content = key;
                                    addItem();
                                    getData("");
                                });
                                hot_lin.addView(textView);
                            }
                        }
                    }
                }

                @Override
                public void onFailed(String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
        });

        refresh_layout.callOnClick();


        lin_layout.addView(view);

    }

    /**
     * 推荐功能
     **/
    private void addOtherItem() {

        View view = LayoutInflater.from(lin_layout.getContext()).inflate(R.layout.layout_other, lin_layout, false);
        LinearLayout layout1 = view.findViewById(R.id.lin_layout1);
        LinearLayout layout2 = view.findViewById(R.id.lin_layout2);
        LinearLayout layout3 = view.findViewById(R.id.lin_layout3);
        LinearLayout layout4 = view.findViewById(R.id.lin_layout4);

        layout1.setOnClickListener(v -> BaseModule.startActivityByUrl(getActivity(), "https://h5.law.push.aegis-info.com/html/doctemplate.html"));

        layout2.setOnClickListener(v -> {

            String str = JsonUtil.getJson("case.json",getActivity());
            try {
                JSONArray jsonArray = new JSONArray(str);

                List<CaseBean> caseBeans = new ArrayList<>();
                if(jsonArray!=null && jsonArray.length()>0){
                    Gson gson = new Gson();
                    for(int i=0;i<jsonArray.length();i++){
                        CaseBean caseBean = gson.fromJson(jsonArray.opt(i).toString(),CaseBean.class);
                        caseBeans.add(caseBean);
                    }
                }
                while (caseBeans.size()>5){
                    int num;
                    if(android.os.Build.VERSION.SDK_INT>=21){
                        num = ThreadLocalRandom.current().nextInt(0, caseBeans.size());
                    }else{
                        //获取0-k之间的随机数
                        Random r = new Random();
                        num = r.nextInt(caseBeans.size());
                    }
                    caseBeans.remove(num);
                }
                addCaseItem(caseBeans);
            } catch (Exception e) {

            }
        });

        layout3.setOnClickListener(v -> BaseModule.startActivityByUrl(getActivity(), "https://h5.law.push.aegis-info.com/html/calculator.html?typeid=legal&id=521"));

        layout4.setOnClickListener(v -> BaseModule.startActivityByUrl(getActivity(), "https://h5.law.push.aegis-info.com/html/penalties_list"));

        lin_layout.addView(view);
    }

    /**
     * 以案说法功能
     **/
    private void addCaseItem(List<CaseBean> cases) {

        View view = LayoutInflater.from(lin_layout.getContext()).inflate(R.layout.layout_hot, lin_layout, false);
        TextView hot_text = view.findViewById(R.id.hot_text);
        View refresh_layout = view.findViewById(R.id.refresh_layout);
        LinearLayout hot_lin = view.findViewById(R.id.hot_lin);

        hot_text.setText("以案说法");

        refresh_layout.setVisibility(View.GONE);
        hot_lin.removeAllViews();
        for (int i = 0; i < cases.size(); i++) {
            TextView textView = new TextView(hot_lin.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = (int) getResources().getDimension(R.dimen.px20);
            lp.bottomMargin = (int) getResources().getDimension(R.dimen.px20);
            lp.leftMargin = (int) getResources().getDimension(R.dimen.px60);
            lp.rightMargin = (int) getResources().getDimension(R.dimen.px60);
            textView.setLayoutParams(lp);
            textView.setText(cases.get(i).title);
            textView.setLineSpacing(0, 1.5f);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.px24));
            textView.setTextColor(getResources().getColor(R.color.color_007eff));
            String case_id = cases.get(i).id;
            String case_title = cases.get(i).title;
            textView.setOnClickListener(v -> {
                ((TextView)v).setTextColor(v.getContext().getResources().getColor(R.color.color_113260));
                if(!mSlideUtil.isShow()){
                    rela_botton.setVisibility(View.VISIBLE);
                    mSlideUtil.canShow = true;
                    mSlideUtil.show(true);
                    rela_botton_bg.setVisibility(View.VISIBLE);
                    getCaseInfo(case_id);
                }
            });
            hot_lin.addView(textView);
        }

        lin_layout.addView(view);

        new Handler().postDelayed(() -> {
            SLayoutHeight = lin_layout.getHeight();
            int dy = (int) (SLayoutHeight - 100);
            Log.e("asd", "dy = " + dy);
            scrollview.smoothScrollBy(0, dy);
        }, 500);


    }

    private void getCaseInfo(String id){
        rela_botton_lin.removeAllViews();
        rela_botton_title.setText("");
        rela_botton_title2.setText("");

        String url = MessageFormat.format("https://xcx.wechat.aegis-info.com/api/wxapplet/user/case2law?content={0}&flag=2",  id);
        requstInfo(url, new LibraryFragment.NetMsgListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSucceed(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.has("code") && jsonObject.optInt("code") == 0
                        && jsonObject.has("data")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if(data!=null){
                        Gson gson = new Gson();
                        case2LawBean = gson.fromJson(data.toString(),Case2LawBean.class);

                        changeBottonBtn(0);
                    }

                }
            }

            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 用户提问
     **/
    private void addItem() {

        View view = LayoutInflater.from(lin_layout.getContext()).inflate(R.layout.layout_user, lin_layout, false);
//            CircleImageView user_image = view.findViewById(R.id.user_image);
        TextView user_text = view.findViewById(R.id.user_text);
        user_text.setText(content);

        lin_layout.addView(view);


        new Handler().postDelayed(() -> {
            SLayoutHeight = lin_layout.getHeight();
            int dy = (int) (SLayoutHeight - 100);
            Log.e("asd", "dy = " + dy);
            scrollview.smoothScrollBy(0, dy);
        }, 500);


    }

    /**
     * 机器人回答
     **/
    private void addRobotItem(String q_id) {

        View view = LayoutInflater.from(lin_layout.getContext()).inflate(R.layout.layout_robot, lin_layout, false);

        TextView robot_text = view.findViewById(R.id.robot_text);
        RelativeLayout rebot_law_layout = view.findViewById(R.id.rebot_law_layout);
        LinearLayout rebot_law_more_layout = view.findViewById(R.id.rebot_law_more_layout);
        LinearLayout rebot_law_lin = view.findViewById(R.id.rebot_law_lin);
        RelativeLayout rebot_choices_layout = view.findViewById(R.id.rebot_choices_layout);
        LinearLayout rebot_choices_lin = view.findViewById(R.id.rebot_choices_lin);
        robot_text.setText(answer);

        rebot_law_lin.removeAllViews();
        rebot_choices_lin.removeAllViews();

        if (laws.size() > 0) {
            rebot_law_lin.setVisibility(View.VISIBLE);
            rebot_law_layout.setVisibility(View.GONE);
            for (int i = 0; i < laws.size(); i++) {
                TextView textView = new TextView(rebot_law_lin.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = (int) getResources().getDimension(R.dimen.px40);
                textView.setLayoutParams(lp);
                textView.setText(laws.get(i).content);
                textView.setLineSpacing(0, 1.5f);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.px28));
                textView.setTextColor(rebot_law_lin.getContext().getResources().getColor(R.color.color_3c3e45));
                rebot_law_lin.addView(textView);
            }

            rebot_law_more_layout.setOnClickListener(v -> {
                if(rebot_law_layout.getVisibility() == View.GONE){
                    rebot_law_layout.setVisibility(View.VISIBLE);
                }else{
                    rebot_law_layout.setVisibility(View.GONE);
                }
            });

        }

        if (choices.size() > 0) {
            rebot_choices_lin.setVisibility(View.VISIBLE);
            rebot_choices_layout.setVisibility(View.VISIBLE);
            for (int i = 0; i < choices.size(); i++) {
                TextView textView = new TextView(rebot_choices_lin.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = (int) getResources().getDimension(R.dimen.px30);
                textView.setLayoutParams(lp);
                textView.setText(choices.get(i));
                textView.setLineSpacing(0, 1.5f);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.px28));
                textView.setTextColor(getResources().getColor(R.color.color_007eff));
                String key = choices.get(i);
                textView.setOnClickListener(v -> {
                    ((TextView)v).setTextColor(v.getContext().getResources().getColor(R.color.color_113260));

                    content = key;
                    addItem();
                    getData(q_id);
                });
                rebot_choices_lin.addView(textView);
            }

        }


        lin_layout.addView(view);

    }

    /**
     * 引导语
     **/
    private void addGuideItem() {

        View view = LayoutInflater.from(lin_layout.getContext()).inflate(R.layout.layout_guide, lin_layout, false);
        lin_layout.addView(view);

    }



    private void getData(String q_id) {

        answer = "";

        String deviceId = SharePreHelper.getDeviceId();
        String user_id = "bb15c0b0054b685594b8_"+deviceId;

        String url = "https://xiaofa.aegis-info.com/api/law_inference/simple/law_qa";
        Map<String, String> map = new HashMap();
        map.put("content", content);
        map.put("qid", q_id);
        map.put("event_ids", event_ids);
        map.put("industry_ids", industry_ids);
        map.put("location", location);
        map.put("user_id", user_id);
        map.put("category", category);
        requstInfo(url, map, "post", new LibraryFragment.NetMsgListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSucceed(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.has("code") && jsonObject.optInt("code") == 0 && jsonObject.has("data")) {

                    JSONObject mData = jsonObject.optJSONObject("data");

                    answer = mData.optString("answer");
                    laws.clear();
                    if (mData.has("laws")) {
                        JSONArray lawsJson = mData.optJSONArray("laws");
                        Gson gson = new Gson();
                        for (int i = 0; i < lawsJson.length(); i++) {
                            LawBean lawBean = gson.fromJson(lawsJson.opt(i).toString(), LawBean.class);
                            laws.add(lawBean);
                        }
                    }

                    choices.clear();
                    if (mData.has("choices")) {
                        JSONArray choicesJson = mData.optJSONArray("choices");
                        for (int i = 0; i < choicesJson.length(); i++) {
                            choices.add(choicesJson.opt(i).toString());
                        }
                    }
                    if (choices.isEmpty() && mData.has("similar_question")) {
                        JSONArray choicesJson = mData.optJSONArray("similar_question");
                        for (int i = 0; i < choicesJson.length(); i++) {
                            choices.add(choicesJson.opt(i).toString());
                        }
                    }


                    if (!isAddOther) {

                        addGuideItem();

//                        answer = "您是想问一下问题吗？可以直接点击哦。如果不是，请直接在输入框提问 \\(≧∇≦)/";
//
//                        addRobotItem("");
                        edit_text.setText("");

                        addHotItem();

                        isAddOther = true;
                        addOtherItem();
                    }else{
                        addRobotItem(mData.optString("qid"));
                        edit_text.setText("");
                    }
                } else {
                    CustomToast.showLongToast("接口数据解析出错:" + jsonObject.toString());
                }
            }

            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });

    }

    public void startVoice(){
        if(mIat==null){
            return;
        }
        //开始识别，并设置监听器
        mIat.startListening(mRecognizerListener);
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            toastCenter("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。

            toastCenter(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            toastCenter("结束说话");

            voice_bg.setVisibility(View.GONE);
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());

            printResult(results);

            if (isLast & cyclic) {
//                // TODO 最后的结果
//                Message message = Message.obtain();
//                message.what = 0x001;
//                han.sendMessageDelayed(message,100);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            toastCenter("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

//        toastCenter(resultBuffer.toString());
        edit_text.setText(resultBuffer.toString());
    }

}
