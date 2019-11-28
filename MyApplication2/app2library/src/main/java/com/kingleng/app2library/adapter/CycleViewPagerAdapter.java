package com.kingleng.app2library.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;

/**
 * 作者：kingleng on 2019/8/1 13:40
 */

public class CycleViewPagerAdapter extends FragmentStatePagerAdapter {

    int pagerNum;
    List<Fragment> mFragments;

    public CycleViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
        pagerNum = mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position % pagerNum);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public interface OnClickListener{
        void onClick(View view,int position);
    }

}
