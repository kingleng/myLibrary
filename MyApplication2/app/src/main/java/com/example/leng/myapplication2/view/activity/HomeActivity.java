package com.example.leng.myapplication2.view.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.router.BaseModule;
import com.example.leng.myapplication2.view.adapter.QuickAdapter;
import com.example.leng.myapplication2.view.myView.FloatDragView;
import com.example.leng.myapplication2.view.tools.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {

    RelativeLayout rela_layout;
    RecyclerView recyclerView;
    ArrayList<ClassData> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(DensityUtil.dip2px(HomeActivity.this, 5), DensityUtil.dip2px(HomeActivity.this, 5), DensityUtil.dip2px(HomeActivity.this, 5), 0);
            }
        });
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
                        BaseModule.startActivityByUrl("www.kingleng.com?adType="+data.adType);
                    }
                });
            }

        });

//        float sum = 0;
//        for(int i=1;i<31;i++){
//            sum += 10;
//            sum *= 1.14f;
//            Log.e("asd","year = "+i+": sum = "+sum);
//        }

    }

    List<String> strings;
    ListPopupWindow popupWindow;
    View mView;

    private void initView(){
//        MyHorizontalScrollView myHorizontalScrollView = (MyHorizontalScrollView)findViewById(R.id.myHorizontalScrollView);
//        recyclerView = myHorizontalScrollView.getContentView();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        rela_layout = (RelativeLayout) findViewById(R.id.rela_layout);
        FloatDragView.addFloatDragView(this, rela_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击事件
                popupWindow.setAnchorView(view);
                popupWindow.show();
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
                BaseModule.startActivityByUrl("http://www.baidu.com");
                popupWindow.dismiss();
            }
        });
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


    }

    public class ClassData{
        public String icon;
        public String name;
        public Class className;
        public String adType;
    }
}
