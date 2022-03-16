package com.example.leng.myapplication2.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.QuickAdapter;
import com.example.leng.myapplication2.base.BaseActivity;
import com.google.gson.Gson;
import com.info.aegis.kl_annotation.Hello;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Hello(value = "WNFXActivity")
public class WNFXActivity extends BaseActivity implements View.OnClickListener {

    Button ib;
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


        SharedPreferences sp = getSharedPreferences("wn_cache", Context.MODE_PRIVATE);
        String s = sp.getString("wn_time","");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            if(jsonArray.length()>0){
                Gson gson = new Gson();
                for(int i=0;i<jsonArray.length();i++){
                    InfoBean item = gson.fromJson(jsonArray.optString(i),InfoBean.class);
                    if(item!=null){
                        data.add(item);
                    }

                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initView(){
        ib = findViewById(R.id.ib);
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
                holder.setText(R.id.duration,handlTime(data.endTime-data.startTime));
                if(position != 0){
                    holder.setText(R.id.interval,handlTime(data.startTime-lastInfoBean.endTime));
                }else{
                    holder.setText(R.id.interval,"");
                }

                lastInfoBean = data;
            }

        });
    }

    private void initEvent(){
        ib.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        switch (v.getId()){
            case R.id.ib:
                long cTime = System.currentTimeMillis();

                if(TextUtils.equals(ib.getText(),"开始")){
                    infoBean = new InfoBean();
                    infoBean.startTime = cTime;
//                    currentTime.setText(df.format(new Date(cTime)));
                    ib.setText("结束");
                    ib.setSelected(true);
                    time = 0;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    currentTime.setText(handlTime(time));
                                }
                            });

                            time += 1000;
                        }
                    },0,1000);
                }else{
                    if(infoBean!=null){
                        infoBean.endTime = cTime;
                        data.add(infoBean);
                        recyclerView.getAdapter().notifyDataSetChanged();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("data",data);
                            SharedPreferences sp = getSharedPreferences("wn_cache", Context.MODE_PRIVATE);
                            sp.edit().putString("wn_time",jsonObject.toString()).apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
//                    currentTime.setText(df.format(new Date(cTime)));
                    ib.setText("开始");
                    ib.setSelected(false);
                    timer.cancel();
                }

                break;
        }


    }

    long time = 0;

    Timer timer = new Timer();

    private String handlTime(long time){
        long t = time/1000;
        long s = t%60;
        long f = (t/60)%60;
        long h = (t/3600)%24;

//        String ss = s<10?("0"+s):(s+"");
//        String ff = f<10?("0"+f):(f+"");
//        String hh = h<10?("0"+h):(h+"");
        String ss = (s+"");
        String ff = (f+"");
        String hh = (h+"");

        if(h>0){
            return hh+"小时"+ff+"分钟"+ss+"秒";
        }else if(f>0){
            return ff+"分钟"+ss+"秒";
        }else {
            return ss+"秒";
        }

    }

    class InfoBean{
        public long startTime;
        public long endTime;

        @Override
        public String toString() {
            return "{" +
                    "startTime:" + startTime +
                    ", endTime:" + endTime +
                    '}';
        }
    }
}