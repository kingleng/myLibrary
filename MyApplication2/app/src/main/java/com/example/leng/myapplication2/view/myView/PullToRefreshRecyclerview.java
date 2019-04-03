package com.example.leng.myapplication2.view.myView;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leng.myapplication2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leng on 2017/1/17.
 */

public class PullToRefreshRecyclerview extends LinearLayout{

//    SwipeRefreshLayout swipeRefreshLayout;
MySwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView tv;
    Context context;

    public PullToRefreshRecyclerview(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pulltorefresh_recyclerview,this);
        this.context = context;
//        tv = (TextView)findViewById(R.id.textView1);
//        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeRefreshLayout = (MySwipeRefreshLayout)findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView)findViewById(R.id.swipe_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setAdapter();
        //设置刷新时动画的颜色，可以设置4个
//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
//                tv.setText("正在刷新");
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        tv.setText("刷新完成");
                        myData.add("新增数据");
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });


    }

    List<String> myData = new ArrayList<>();
    MyRecyclerViewAdapter adapter;
    private void setData(){
        myData.clear();
        myData.add("第一次");
        myData.add("第2次");
        myData.add("第3次");
        myData.add("第4次");
        myData.add("第5次");
        myData.add("第6次");
        myData.add("第7次");
        myData.add("第8次");
    }

    public void setAdapter(){
        setData();
        adapter = new MyRecyclerViewAdapter(myData);
        recyclerView.setAdapter(adapter);
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

        List<String> data = new ArrayList<>();

        public MyRecyclerViewAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.onBind(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView)itemView.findViewById(R.id.tv_item);
            }

            public void onBind(int position){
                tv.setText(data.get(position));
            }
        }
    }

}
