package com.kingleng.app2library.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kingleng.app2library.R;
import com.kingleng.app2library.adapter.CycleViewPagerAdapter;

/**
 * Created by leng on 2019/4/28.
 * 海报内容 --  纯图片样式界面
 */
@SuppressLint("ValidFragment")
public class PicFragment extends Fragment {

    Context mContext;
    String imgurl;
    int position;
    CycleViewPagerAdapter.OnClickListener onClickListener;

    ImageView imageView;
    public String url = "";

    public PicFragment(Context mContext, String imgurl, int position,  CycleViewPagerAdapter.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.imgurl = imgurl;
        this.position = position;
        this.onClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        imageView = (ImageView) view.findViewById(R.id.image_iv);

        Glide.with(mContext)
                .load(imgurl)
                .error(R.drawable.defult_pic)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view, position);
                }
            }
        });
        return view;
    }

    private void getData() {


    }

}
