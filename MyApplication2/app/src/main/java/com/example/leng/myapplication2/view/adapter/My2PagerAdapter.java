package com.example.leng.myapplication2.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.tools.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leng on 2016/12/27.
 */

public class My2PagerAdapter extends PagerAdapter {

    List<String> datas;
    Context mContext;

    public My2PagerAdapter(Context mContext, List<String> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        final int currentItem = position % datas.size();
        int currentItem = position -1;
        if(currentItem == -1){
            currentItem = datas.size()-1;
        }else if(currentItem == datas.size()){
            currentItem = 0;
        }

        ImageView image=new ImageView(mContext);
        Glide.with(mContext).load(datas.get(currentItem)).into(image);
//        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ((ViewPager) container).addView(image);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
//        return Integer.MAX_VALUE;
        if(datas.size()>1){
            return datas.size()+2;
        }
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
