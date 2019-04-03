package com.example.leng.myapplication2.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.adapter.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class Main8Activity_LianDong extends AppCompatActivity {

    RecyclerView recyclerView;

    List<String> myData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8_lian_dong);

        recyclerView = findViewById(R.id.recyclerview);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }

    void setData(){
        myData = new ArrayList<>();
        myData.add("第一次");
        myData.add("第2次");
        myData.add("第3次");
        myData.add("第4次");
        myData.add("第5次");
        myData.add("第6次");
        myData.add("第7次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
    }
}
