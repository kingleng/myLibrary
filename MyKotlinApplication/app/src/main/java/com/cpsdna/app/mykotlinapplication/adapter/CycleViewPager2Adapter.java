package com.cpsdna.app.mykotlinapplication.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.cpsdna.app.mykotlinapplication.module.HomeModuleBean;
import com.cpsdna.app.mykotlinapplication.ui.fragment.HomeNewFragment;
import com.cpsdna.app.mykotlinapplication.ui.fragment.PicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：kingleng on 2019/8/1 13:40
 */

public class CycleViewPager2Adapter extends FragmentPagerAdapter {

    int pagerNum;
    List<HomeModuleBean> datas;

    List<Fragment> mFragments = new ArrayList<>();

    Context mContext;
    HomeNewFragment.OnClickListener onClickListener;

    public CycleViewPager2Adapter(FragmentManager fm, Context mContext, HomeNewFragment.OnClickListener onClickListener, List<HomeModuleBean> datas) {
        super(fm);
        this.mContext = mContext;
        this.onClickListener = onClickListener;
        this.datas = datas;
        pagerNum = datas.size();
    }

    @Override
    public Fragment getItem(int position) {
        int position0 = position;
        if (position == 0) {
            position0 = pagerNum - 1;
        } else if (position == pagerNum + 1) {
            position0 = 0;
        } else {
            position0 -= 1;
        }

        Fragment fragment = null;

        if (position < mFragments.size() && mFragments.get(position) != null) {
            fragment = mFragments.get(position);
        } else {
            fragment = new PicFragment(mContext, "", position0, datas.get(position0), onClickListener);
            mFragments.add(fragment);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return pagerNum + 2;
    }

}
