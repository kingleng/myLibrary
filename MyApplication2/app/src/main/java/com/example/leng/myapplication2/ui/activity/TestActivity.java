package com.example.leng.myapplication2.ui.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.QuickAdapter;
import com.example.leng.myapplication2.base.BaseActivity;
import com.example.leng.myapplication2.permission.AlertWindowPermissionUtil;
import com.example.leng.myapplication2.permission.CameraPermissionUtil;
import com.example.leng.myapplication2.permission.RecordPermissionUtil;
import com.example.leng.myapplication2.permission.StoragePermissionUtil;
import com.example.leng.myapplication2.ui.myView.MyItemAnimator;
import com.example.leng.myapplication2.ui.myView.SwitchButton;
import com.example.leng.myapplication2.ui.tools.AndroidUtil;
import com.example.mylibrary.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_RECORD = 2;
    private static final int REQUEST_STORAGE = 21;
    private static final int REQUEST_ALERT = 31;

    RecyclerView recyclerView;
    AppBarLayout appbar;

    List<String> myData;

    SwitchButton switchbtn;

    ViewTreeObserver vto2;

    LinearLayout txtView_content_pic_lin;

    TextView c_tv;

    private RadioGroup sex_radiogroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;

    CameraPermissionUtil mCameraPermissionUtil;
    RecordPermissionUtil mRecordPermissionUtil;
    StoragePermissionUtil mStoragePermissionUtil;
    AlertWindowPermissionUtil mAlertWindowPermissionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        initView();

        getCamera();

    }

    private void initView(){

        final SwipeRefreshLayout swipeRefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshlayout);
        txtView_content_pic_lin = (LinearLayout) findViewById(R.id.txtView_content_pic_lin);
        appbar = (AppBarLayout)findViewById(R.id.appbar);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        switchbtn = (SwitchButton)findViewById(R.id.switchbtn);
        switchbtn.writeSwitchButtonState(true);
        c_tv = findViewById(R.id.c_tv);

        sex_radiogroup = (RadioGroup) findViewById(R.id.sex_radiogroup);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);




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




        switchbtn.setStateListener(new SwitchButton.StateListener() {
            @Override
            public void onStateChange(boolean state) {
                if(state){
                    Log.e("asd","true");
                }else{
                    Log.e("asd","false");
                }

            }
        });

        vto2 = switchbtn.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
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

        String title = "我在这里吃饭<em>好吃的</em>烤肉！";
        int start=title.indexOf("<em>");
        int length =title.indexOf("</em>")-start-4;
        SpannableString spanString = new SpannableString(title.replace("<em>","").replace("</em>",""));

        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBE00")), start,start+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        c_tv.setText(spanString);



        sex_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButton1.getId()) {
                    Toast.makeText(getBaseContext(),"男",Toast.LENGTH_SHORT).show();
                } else if (checkedId == radioButton2.getId()) {
                    Toast.makeText(getBaseContext(),"女",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void getCamera(){
        mCameraPermissionUtil = new CameraPermissionUtil(this, REQUEST_CAMERA);
        mCameraPermissionUtil.checkPermissionOrRequest();

//        mRecordPermissionUtil = new RecordPermissionUtil(this, REQUEST_RECORD);
//        mRecordPermissionUtil.checkPermissionOrRequest();

        mStoragePermissionUtil = new StoragePermissionUtil(this,REQUEST_STORAGE);
        mStoragePermissionUtil.checkPermissionOrRequest();

        mAlertWindowPermissionUtil = new AlertWindowPermissionUtil(this,REQUEST_ALERT);
        mAlertWindowPermissionUtil.checkPermissionOrRequest();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.i(TAG, "--- onRequestPermissionsResult requestCode : "+requestCode+" permissions : "+permissions);

        if(requestCode == REQUEST_CAMERA){
            mCameraPermissionUtil.handleRequestResult(requestCode, permissions, grantResults);
        }

//        if(requestCode == REQUEST_RECORD){
//            mRecordPermissionUtil.handleRequestResult(requestCode, permissions, grantResults);
//        }


        if(requestCode == REQUEST_STORAGE){
            mStoragePermissionUtil.handleRequestResult(requestCode, permissions, grantResults);
        }

        if(requestCode == REQUEST_ALERT){
            mAlertWindowPermissionUtil.handleRequestResult(requestCode, permissions, grantResults);
        }

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
