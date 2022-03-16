package com.example.leng.myapplication2.adapter;

import com.example.leng.myapplication2.R;

import java.util.ArrayList;

/**
 * Created by leng on 2017/2/6.
 */

public class test {
    public void test(){
        QuickAdapter adapter = new QuickAdapter(new ArrayList()) {
            @Override
            public int getLayoutId(int viewType) {
                return 0;
            }

            @Override
            public void convert(QuickAdapter.VH holder, Object data, int position) {
                holder.setText(R.id.ibtn1,"asd");
            }
        };
    }
}
