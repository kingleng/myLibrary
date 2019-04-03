package com.example.leng.myapplication2.view.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.adapter.QuickAdapter;
import com.example.leng.myapplication2.view.myView.MyItemAnimator;
import com.example.leng.myapplication2.view.myView.SwitchButton;
import com.example.leng.myapplication2.view.tools.AndroidUtil;
import com.example.mylibrary.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppBarLayout appbar;

    List<String> myData;

    SwitchButton switchbtn;

    ViewTreeObserver vto2;

    LinearLayout txtView_content_pic_lin;

    TextView c_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


        txtView_content_pic_lin = (LinearLayout) findViewById(R.id.txtView_content_pic_lin);


        final SwipeRefreshLayout swipeRefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshlayout);

        swipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshlayout.setRefreshing(false);

                        myData.add(2,"add动画-新增测试");
                        recyclerView.getAdapter().notifyItemInserted(2);
//                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDatas, newDatas), true);
//                        diffResult.dispatchUpdatesTo(recyclerView.getAdapter());
                    }
                },500);
            }
        });

        appbar = (AppBarLayout)findViewById(R.id.appbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new MyItemAnimator());
        setData();
        recyclerView.setAdapter(new QuickAdapter<String>(myData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.list_item;
            }

            @Override
            public void convert(VH holder, String data, int position) {
                ((TextView)holder.getView(R.id.tv_item)).setText(data);
            }
        });


        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                Log.e("AppBarLayout","appBarLayout.getHeight() = "+appBarLayout.getHeight());
                Log.e("AppBarLayout","i = "+i);

//                if ((Math.abs(i) * 1f) < DensityUtil.dp2px(TestActivity.this, 2)) {
                if ((Math.abs(i) * 1f) <= 0 ) {
                    swipeRefreshlayout.setEnabled(true);
                } else {
                    swipeRefreshlayout.setEnabled(false);
                }

                int asd = appbar.getTop();

                Log.e("asd","aa = "+asd);
                switchbtn.writeSwitchButtonState(false);

            }
        });


        switchbtn = (SwitchButton)findViewById(R.id.switchbtn);
        switchbtn.writeSwitchButtonState(true);

        switchbtn.setStateListener(new SwitchButton.StateListener() {
            @Override
            public void onStateChange(boolean state) {
                if(state){
                    Log.e("asd","true");
                }else{
                    Log.e("asd","false");
                }

                float ssa = 40;

                for(int i=0;i<30;i++){
                    Log.e("asdd","i = "+i+" aas = "+ssa);
                    ssa*=1.14f;
                    ssa+=5;
                }

//                String[] ass = {"111","222","333"};
//                ArrayList<String> add = new ArrayList<>();
//                add.add("111");
//                add.add("222");
//                add.add("333");
//                add.add("444");
//                Log.e("asd","ass = "+add.toString().replace("[","").replace("]",""));
//
//                String pgPrice = "100.00";
//                String price = pgPrice;
//                try{
//                    double mp = Double.parseDouble(price);
//                    price = mp+"";
//                }catch (Exception e){
//                }
//                Log.e("asd","price = "+price);
            }
        });


        vto2 = switchbtn.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.e("asd","price :::: ");
                if(vto2.isAlive()){
                    vto2.removeGlobalOnLayoutListener(this);
                }


            }
        });


        int itemWidth = (AndroidUtil.getScreenWidth(TestActivity.this) - DensityUtil.dp2px(TestActivity.this, 130)) / 3;
        for (int i = 0; i < 4; i++) {
            if (i >= 3) {
                break;
            }
            RelativeLayout relativeLayout = new RelativeLayout(TestActivity.this);
            RelativeLayout.LayoutParams rlLayoutParams = new RelativeLayout.LayoutParams(itemWidth+DensityUtil.dp2px(TestActivity.this, 10), itemWidth);
//                    rlLayoutParams.setMargins(0, 0, DisplayUtil.dp2px(activity, 10), 0);
            relativeLayout.setLayoutParams(rlLayoutParams);

            ImageView imageView = new ImageView(TestActivity.this);
//            Meteor.with(activity).loadImage(bean.elementMap.infoImages.elementImageList.get(i).imageUrl, imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth, itemWidth);
//                    layoutParams.setMargins(0, 0, DisplayUtil.dp2px(activity, 10), 0);
            imageView.setLayoutParams(layoutParams);
            relativeLayout.addView(imageView);
            ImageView imageView2 = new ImageView(TestActivity.this);
            imageView2.setLayoutParams(layoutParams);
            imageView2.setImageResource(R.color.color_08000000);
            relativeLayout.addView(imageView2);
            txtView_content_pic_lin.addView(relativeLayout);
        }



        c_tv = findViewById(R.id.c_tv);

        float asf = 40400;
        float aaa = 0;
        for(int i=0;i<10;i++){
            aaa += asf*0.07f/12;
            asf -= 4400;
            Log.e("asd","aaa = "+aaa);
        }

//        String mPrice = "818.8";
//        String mPrice = aaa+"";
//        if(!mPrice.contains(".")){
//            mPrice = mPrice+".00";
//        }
//        String[] ss = mPrice.split("\\.");
//        SpannableString spanString = new SpannableString("¥"+ss[0]+"."+ss[1]);
//
//        AbsoluteSizeSpan span = new AbsoluteSizeSpan(DensityUtil.sp2px(TestActivity.this,11.5f));
//        AbsoluteSizeSpan span2 = new AbsoluteSizeSpan(DensityUtil.sp2px(TestActivity.this,11.5f));
//        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spanString.setSpan(span2, 1+ss[0].length(), 2+ss[0].length()+ss[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String title = "我在这里吃饭<em>好吃的</em>烤肉！";
        int start=title.indexOf("<em>");
        int length =title.indexOf("</em>")-start-4;
        SpannableString spanString = new SpannableString(title.replace("<em>","").replace("</em>",""));

        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBE00")), start,start+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        c_tv.setText(spanString);
//        c_tv.append(spanString);



    }

    void setData(){
        myData = new ArrayList<>();
        myData.add("第1次");
        myData.add("第2次");
        myData.add("第3次");
        myData.add("第4次");
        myData.add("第5次");
        myData.add("第6次");
        myData.add("第7次");
        myData.add("第8次");
        myData.add("第9次");
        myData.add("第10次");
        myData.add("第11次");
        myData.add("第12次");
        myData.add("第13次");
        myData.add("第14次");
        myData.add("第15次");
        myData.add("第16次");
        myData.add("第17次");
        myData.add("第18次");
        myData.add("第19次");
        myData.add("第20次");
        myData.add("第21次");
        myData.add("第22次");
        myData.add("第23次");
        myData.add("第24次");
        myData.add("第25次");
    }
}
