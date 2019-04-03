package com.example.leng.myapplication2.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.adapter.QuickAdapter;
import com.example.leng.myapplication2.view.tools.DensityUtil;
import com.leochuan.ScaleLayoutManager;
import com.leochuan.ViewPagerLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Activity extends AppCompatActivity {

    RecyclerView recyclerView;

    ViewPagerLayoutManager viewPagerLayoutManager;

    List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        viewPagerLayoutManager = createLayoutManager();
        recyclerView.setLayoutManager(viewPagerLayoutManager);

        initData();
        recyclerView.setAdapter(new QuickAdapter<String>(mData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_viewpager;
            }

            @Override
            public void convert(VH holder, String data, int position) {

            }
        });


    }

    private void initData(){
        mData = new ArrayList<>();
        mData.add("a");
        mData.add("a");
        mData.add("a");
        mData.add("a");
        mData.add("a");
        mData.add("a");
        mData.add("a");
    }

    protected ScaleLayoutManager createLayoutManager() {
        return new ScaleLayoutManager(this, DensityUtil.dip2px(this, 10));
    }
}
