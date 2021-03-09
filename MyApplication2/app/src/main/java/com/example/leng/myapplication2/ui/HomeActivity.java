package com.example.leng.myapplication2.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.base.BaseActivity;
import com.example.leng.myapplication2.router.AppModule;
import com.example.leng.myapplication2.adapter.QuickAdapter;
import com.example.leng.myapplication2.router.RouterManager;
import com.example.leng.myapplication2.ui.activity.AdbActivity;
import com.example.leng.myapplication2.ui.activity.AudioActivity;
import com.example.leng.myapplication2.ui.activity.CameraActivity;
import com.example.leng.myapplication2.ui.activity.CoordinatorLayoutActivity;
import com.example.leng.myapplication2.ui.activity.DemoActivity;
import com.example.leng.myapplication2.ui.activity.FrameAnimationActivity;
import com.example.leng.myapplication2.ui.activity.HotfixActivity;
import com.example.leng.myapplication2.ui.activity.ImageMapActivity;
import com.example.leng.myapplication2.ui.activity.JsoupActivity;
import com.example.leng.myapplication2.ui.activity.LargeImageActivity;
import com.example.leng.myapplication2.ui.activity.Main2Activity;
import com.example.leng.myapplication2.ui.activity.Main6Activity;
import com.example.leng.myapplication2.ui.activity.Main7Activity;
import com.example.leng.myapplication2.ui.activity.Main8Activity_LianDong;
import com.example.leng.myapplication2.ui.activity.MainActivity;
import com.example.leng.myapplication2.ui.activity.MarQueeActivity;
import com.example.leng.myapplication2.ui.activity.MusicActivity;
import com.example.leng.myapplication2.ui.activity.MyGameActivity;
import com.example.leng.myapplication2.ui.activity.MyTextNewActivity;
import com.example.leng.myapplication2.ui.activity.Pic2AsciiActivity;
import com.example.leng.myapplication2.ui.activity.RefreshActivity;
import com.example.leng.myapplication2.ui.activity.SendSMSActivity;
import com.example.leng.myapplication2.ui.activity.SipPhoneActivity;
import com.example.leng.myapplication2.ui.activity.TestActivity;
import com.example.leng.myapplication2.ui.activity.VideoViewActivity;
import com.example.leng.myapplication2.ui.activity.ViewPager2Activity;
import com.example.leng.myapplication2.ui.activity.ViewPagerActivity;
import com.example.leng.myapplication2.ui.activity.VlayoutActivity;
import com.example.leng.myapplication2.ui.activity.WNFXActivity;
import com.example.leng.myapplication2.ui.activity.XuanFuActivity;
import com.example.leng.myapplication2.ui.activity.YMTDActivity;
import com.example.leng.myapplication2.ui.customWidget.NameBean;
import com.example.leng.myapplication2.ui.customWidget.SectionDecoration;
import com.example.leng.myapplication2.ui.myView.FloatDragView;
import com.example.leng.myapplication2.ui.service.StartFloatBallService;
import com.example.leng.myapplication2.utils.PluginUtil;
import com.example.leng.myapplication2.voice.VoiceCallback;
import com.example.leng.myapplication2.voice.VoiceHelper;
import com.info.aegis.kl_annotation.Hello;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

@Hello(value = "HomeActivity")
public class HomeActivity extends BaseActivity {

    private FrameLayout mFlAdHolder;

    View figview;
    RelativeLayout rela_layout;
    RecyclerView recyclerView;
    ArrayList<ClassData> datas = new ArrayList<>();

    List<Resources> resources;

    Typeface typeface;

    VoiceHelper voiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(getAssets(), "hwxk.ttf");
        }

        LayoutInflaterCompat.setFactory2(LayoutInflater.from(this), new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);

                if ( view!= null && (view instanceof TextView)) {
                    ((TextView) view).setTypeface(typeface);
                }
                return view;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String toast = getIntent().getStringExtra("toast");
        if(!TextUtils.isEmpty(toast)){
            Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
        }

        initView();
        initData();
        setPullAction(datas);

        resources = PluginUtil.loadPlugin(HomeActivity.this);

        voiceHelper = new VoiceHelper(this);

        voiceHelper.setVoice();

        /*******************************************************************************/


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                outRect.set(DensityUtil.dip2px(HomeActivity.this, 5), DensityUtil.dip2px(HomeActivity.this, 5), DensityUtil.dip2px(HomeActivity.this, 5), 0);
//            }
//        });
        recyclerView.addItemDecoration(new SectionDecoration(dataList, this, new SectionDecoration.DecorationCallback() {
            @Override
            public String getGroupId(int position) {
                if(dataList.get(position).getName()!=null) {
                    return dataList.get(position).getName();
                }
                return "-1";
            }

            @Override
            public String getGroupFirstLine(int position) {
                if(dataList.get(position).getName()!=null) {
                    return dataList.get(position).getName();
                }
                return "";
            }
        }));

        recyclerView.setAdapter(new QuickAdapter<ClassData>(datas) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_home_select;
            }

            @Override
            public void convert(VH holder, final ClassData data, int position) {

                ImageView imageview = holder.getView(R.id.item_icon);
                TextView name = holder.getView(R.id.item_name);
                name.setText(data.name);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this,data.className);
//                        startActivity(intent);
                        if(!TextUtils.isEmpty(data.adType)){
                            AppModule.startActivityByUrl(HomeActivity.this,"www.aegis.com?adType="+data.adType);
                        }else{
                            AppModule.startActivityByUrl(HomeActivity.this,data.url);
                        }

                    }
                });
            }

        });

//        PersonObserver p1 = new PersonObserver("小一");
//        PersonObserver p2 = new PersonObserver("小二");
//        PersonObserver p3 = new PersonObserver("小三");
//        PersonObserver p4 = new PersonObserver("小四");
//
//        AndroidObervable androidObervable = new AndroidObervable();
//
//        androidObervable.addObserver(p1);
//        androidObervable.addObserver(p2);
//        androidObervable.addObserver(p3);
//        androidObervable.addObserver(p4);
//
//        androidObervable.postNewInfo("吃饭了");
//        androidObervable.postNewInfo("吃过了");

    }

    @Override
    public void onDestroy() {
        if(voiceHelper!=null){
            voiceHelper.close();
        }
        super.onDestroy();
    }

    List<NameBean> dataList;
    private void setPullAction(List<ClassData> classDataList) {
        dataList = new ArrayList<>();

        for (int i = 0; i < classDataList.size(); i++) {
            NameBean nameBean = new NameBean();
            String name0 = classDataList.get(i).name;
            nameBean.setName(name0);
            dataList.add(nameBean);
        }
    }


    List<String> strings;
    ListPopupWindow popupWindow;
    View mView;

    int i=0;

    private void initView(){
//        MyHorizontalScrollView myHorizontalScrollView = (MyHorizontalScrollView)findViewById(R.id.myHorizontalScrollView);
//        recyclerView = myHorizontalScrollView.getContentView();

        figview = findViewById(R.id.figview);
        mFlAdHolder = (FrameLayout) findViewById(R.id.fl_adplaceholder);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        rela_layout = (RelativeLayout) findViewById(R.id.rela_layout);

        mFlAdHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                figview.setVisibility(View.VISIBLE);

                voiceHelper.startVoice(new VoiceCallback() {
                    @Override
                    public void result(String result) {

                        figview.setVisibility(View.GONE);

                        toastCenter(result);
//                        edit_text.setText(result);
                    }
                });

//                int id = resources.get(0).getIdentifier("app_name","string","com.info.aegis.mypluginapplication");
//                String appname = resources.get(0).getString(id);
//                Log.e("asd",appname);
//                Intent intent = new Intent();
//                intent.setClassName(HomeActivity.this, "com.info.aegis.mypluginapplication.MainActivity");
//                startActivity(intent);
            }
        });


        FloatDragView.addFloatDragView(this, rela_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ParticleSystem(HomeActivity.this, 1000, R.drawable.p_weather_sun, 3000)
                        .setSpeedModuleAndAngleRange(0.05f, 0.2f, 0, 360)
                        .setRotationSpeed(30)
                        .setScaleRange(0.1f,0.3f)
                        .setAcceleration(0, 360)
                        .oneShot(recyclerView, 200);

                permission();

////                 点击事件
//                popupWindow.setAnchorView(view);
//                popupWindow.show();
//                i++;
//                Toast.makeText(HomeActivity.this,"the i is = "+i,Toast.LENGTH_SHORT).show();
            }
        });

        strings = new ArrayList<String>();
        strings.add("item1");
        strings.add("item2");
        strings.add("item3");
        strings.add("item4");
        strings.add("item5");
        strings.add("item6");

        popupWindow = new ListPopupWindow(this);
//        popupWindow.setAdapter(new ArrayAdapter<String>(this,R.layout.item_popup_item,strings));
        popupWindow.setAdapter(new MyListAdapter(this,strings));
        popupWindow.setAnchorView(rela_layout);
        popupWindow.setWidth(300);
        popupWindow.setHeight(600);
//        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setModal(true);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                AppModule.startActivityByUrl(HomeActivity.this,"http://www.baidu.com");
                AppModule.startActivityByUrl(HomeActivity.this,"https://fszy.ai-risk-v.aegis-info.com/home?debug=1");
                popupWindow.dismiss();
            }
        });
    }

    public void permission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
                return;
            } else {
                //Android6.0以上
                Intent intent = new Intent(this, StartFloatBallService.class);
                startService(intent);
                finish();
            }
        } else {
            //Android6.0以下，不用动态声明权限
            Intent intent = new Intent(this, StartFloatBallService.class);
            startService(intent);
            finish();
        }
    }

    class MyListAdapter extends BaseAdapter{

        List<String> mDatas;
        Activity mActivity;

        public MyListAdapter(Activity mActivity, List<String> mDatas) {
            this.mActivity = mActivity;
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_popup_item,viewGroup,false);
            }

            TextView textView = (TextView)view.findViewById(R.id.item_name);
            textView.setText(mDatas.get(i));

            return view;
        }
    }

    private void initData(){
        datas.clear();

        ClassData classData01 = new ClassData();
        classData01.name = "模块二内容";
        classData01.adType = "110001";
        datas.add(classData01);
        ClassData classData02 = new ClassData();
        classData02.name = "模块二内容";
        classData02.adType = "110002";
        datas.add(classData02);

        ClassData classData0 = new ClassData();
        classData0.name = "test";
        classData0.className = TestActivity.class;
        classData0.adType = "100001";
        datas.add(classData0);

        ClassData classData = new ClassData();
        classData.name = "自定义控件";
        classData.className = MainActivity.class;
        classData.adType = "100002";
        datas.add(classData);

        ClassData classData2 = new ClassData();
        classData2.name = "自定义控件2";
        classData2.className = DemoActivity.class;
        classData2.adType = "100003";
        datas.add(classData2);

        ClassData classData3 = new ClassData();
        classData3.name = "自定义控件3";
        classData3.className = RefreshActivity.class;
        classData3.adType = "100004";
        datas.add(classData3);

        ClassData classData4 = new ClassData();
        classData4.name = "自定义控件4";
        classData4.className = CoordinatorLayoutActivity.class;
        classData4.adType = "100005";
        datas.add(classData4);

        ClassData classData5 = new ClassData();
        classData5.name = "自定义控件5";
        classData5.className = MyGameActivity.class;
        classData5.adType = "100006";
        datas.add(classData5);

        ClassData classData6 = new ClassData();
        classData6.name = "自定义控件6";
        classData6.className = MyTextNewActivity.class;
        classData6.adType = "100007";
        datas.add(classData6);

        ClassData classData7 = new ClassData();
        classData7.name = "自定义控件7";
        classData7.className = Main2Activity.class;
        classData7.adType = "100008";
        datas.add(classData7);

        ClassData classData8 = new ClassData();
        classData8.name = "跑马灯效果";
        classData8.className = MarQueeActivity.class;
        classData8.adType = "100009";
        datas.add(classData8);

        ClassData classData9 = new ClassData();
        classData9.name = "炫酷的动画效果（线条）";
        classData9.className = Main6Activity.class;
        classData9.adType = "100010";
        datas.add(classData9);

        ClassData classData10 = new ClassData();
        classData10.name = "炫酷的动画效果（线条）2";
        classData10.className = Main7Activity.class;
        classData10.adType = "100011";
        datas.add(classData10);

        ClassData classData11 = new ClassData();
        classData11.name = "联动效果（CoordinatorLayout）";
        classData11.className = Main8Activity_LianDong.class;
        classData11.adType = "100012";
        datas.add(classData11);

        ClassData classData12 = new ClassData();
        classData12.name = "音乐播放器";
        classData12.className = MusicActivity.class;
        classData12.adType = "100013";
        datas.add(classData12);

        ClassData classData13 = new ClassData();
        classData13.name = "vlayout demo";
        classData13.className = VlayoutActivity.class;
        classData13.adType = "100014";
        datas.add(classData13);

        ClassData classData14 = new ClassData();
        classData14.name = "ViewPager2 demo";
        classData14.className = ViewPager2Activity.class;
        classData14.adType = "100015";
        datas.add(classData14);

        ClassData classData15 = new ClassData();
        classData15.name = "ViewPager demo";
        classData15.className = ViewPagerActivity.class;
        classData15.adType = "100016";
        datas.add(classData15);

        ClassData classData16 = new ClassData();
        classData16.name = "可收起的悬浮按钮";
        classData16.className = XuanFuActivity.class;
        classData16.adType = "100017";
        datas.add(classData16);

        ClassData classData17 = new ClassData();
        classData17.name = "视频播放";
        classData17.className = VideoViewActivity.class;
        classData17.adType = "100018";
        datas.add(classData17);

        ClassData classData18 = new ClassData();
        classData18.name = "摄像头预览";
        classData18.className = CameraActivity.class;
        classData18.adType = "100019";
        datas.add(classData18);

        ClassData classData19 = new ClassData();
        classData19.name = "帧动画";
        classData19.className = FrameAnimationActivity.class;
        classData19.adType = "100020";
        datas.add(classData19);

        ClassData classData20 = new ClassData();
        classData20.name = "网页";
//        classData20.url = "http://player.videoincloud.com/vod/5193433?src=gkw&cc=1";
        classData20.url = "https://12348hotline.aegis-info.com/im/text/01dQ81.html";
        datas.add(classData20);


        ClassData classData21 = new ClassData();
        classData21.name = "jsoup爬虫（小说）";
        classData21.className = JsoupActivity.class;
        classData21.adType = "100021";
        datas.add(classData21);

        ClassData classData22 = new ClassData();
        classData22.name = "RxHttp 断点下载";
        classData22.className = JsoupActivity.class;
        classData22.adType = "100022";
        datas.add(classData22);

        ClassData classData23 = new ClassData();
        classData23.name = "hotfix 热修复测试";
        classData23.className = HotfixActivity.class;
        classData23.adType = "100023";
        datas.add(classData23);

        ClassData classData24 = new ClassData();
        classData24.name = "短信测试";
        classData24.className = SendSMSActivity.class;
        classData24.adType = "100024";
        datas.add(classData24);

        ClassData classData25 = new ClassData();
        classData25.name = "录音功能";
        classData25.className = AudioActivity.class;
        classData25.adType = "100025";
        datas.add(classData25);

        ClassData classData26 = new ClassData();
        classData26.name = "图片转ascii码字符图";
        classData26.className = Pic2AsciiActivity.class;
        classData26.adType = "100026";
        datas.add(classData26);

        ClassData classData27 = new ClassData();
        classData27.name = "图片区块点击";
        classData27.className = ImageMapActivity.class;
        classData27.adType = "100027";
        datas.add(classData27);

        ClassData classData28 = new ClassData();
        classData28.name = "adb命令远程下发执行测试";
        classData28.className = AdbActivity.class;
        classData28.adType = "100028";
        datas.add(classData28);

        ClassData classData29 = new ClassData();
        classData29.name = "sip呼叫";
        classData29.className = SipPhoneActivity.class;
        classData29.adType = "100029";
        datas.add(classData29);

        ClassData classData30 = new ClassData();
        classData30.name = "胎动计数器";
        classData30.className = YMTDActivity.class;
        classData30.adType = "100030";
        datas.add(classData30);

        ClassData classData31 = new ClassData();
        classData31.name = "喂奶分析";
        classData31.className = WNFXActivity.class;
        classData31.adType = "100031";
        datas.add(classData31);

        ClassData classData32 = new ClassData();
        classData32.name = "超大图显示";
        classData32.className = LargeImageActivity.class;
        classData32.adType = "100032";
        datas.add(classData32);


    }



    public class ClassData{
        public String icon;
        public String name;
        public Class className;
        public String adType;
        public String url;
    }

}
