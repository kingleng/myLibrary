package com.example.leng.myapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.adapter.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorLayoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<String> myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        final String cheeseName = "Title";

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData();
        recyclerView.setAdapter(new QuickAdapter(myData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.list_item;
            }

            @Override
            public void convert(VH holder, Object data, int position) {
                ((TextView)holder.getView(R.id.tv_item)).setText((String)data);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoordinatorLayoutActivity.this,CoordinatorLayout2Activity.class));
            }
        });


    }

    private void loadBackdrop() {
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load("http://dl.bizhi.sogou.com/images/2012/02/16/187433.jpg").placeholder(R.drawable.default_pic).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
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
