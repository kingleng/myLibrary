package com.example.leng.myapplication2.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.QuickAdapter;
import com.example.leng.myapplication2.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WNFXActivity extends BaseActivity implements View.OnClickListener {

    Button ib;
    Button ib2;
    TextView currentTime;
    RecyclerView recyclerView;

    InfoBean infoBean;
    InfoBean lastInfoBean;
    List<InfoBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnfx);
        initView();
        initEvent();
    }

    private void initView(){
        ib = findViewById(R.id.ib);
        ib2 = findViewById(R.id.ib2);
        currentTime = findViewById(R.id.currentTime);
        recyclerView = findViewById(R.id.recyclerView);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuickAdapter<InfoBean>(data) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.layout_item_wnfx;
            }

            @Override
            public void convert(VH holder, InfoBean data, int position) {

                holder.setText(R.id.num,"序列 "+position);
                holder.setText(R.id.startTime,df.format(new Date(data.startTime)));
                holder.setText(R.id.endTime,df.format(new Date(data.endTime)));
                holder.setText(R.id.duration,df.format(data.endTime-data.startTime));
                holder.setText(R.id.interval,position == 0?"":df.format(new Date(data.startTime-lastInfoBean.endTime)));

                lastInfoBean = data;
            }

        });
    }

    private void initEvent(){
        ib.setOnClickListener(this);
        ib2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (v.getId()){
            case R.id.ib:
                long cTime = System.currentTimeMillis();

                infoBean = new InfoBean();
                infoBean.startTime = cTime;

                currentTime.setText(df.format(new Date(cTime)));
                break;
            case R.id.ib2:
                long cTime2 = System.currentTimeMillis();
                if(infoBean!=null){
                    infoBean.endTime = cTime2;
                    data.add(infoBean);
                    recyclerView.getAdapter().notifyDataSetChanged();

                }
                currentTime.setText(df.format(new Date(cTime2)));
                break;
        }
    }

    class InfoBean{
        public long startTime;
        public long endTime;
    }
}