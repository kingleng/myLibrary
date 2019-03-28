package com.example.leng.myapplication.view.myView.yindaoye;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.hubert.guide.NewbieGuide;

import java.util.ArrayList;

/**
 * Created by 17092234 on 2018/6/28.
 */

public class GuideUtil {

    private FrameLayout mParentView;

    private int indexOfChild = -1;//使用anchor时记录的在父布局的位置

    ArrayList<GuideLayout> mGuideLayouts = new ArrayList<>();

    int mCurrent = 0;
    int showCounts = 1;
    String tag;

    private SharedPreferences sp;

    public GuideUtil(Activity context) {
        View anchor = null;
        if (anchor == null) {
            anchor = context.getWindow().getDecorView();
        }
        if (anchor instanceof FrameLayout) {
            mParentView = (FrameLayout) anchor;
        } else {
            FrameLayout frameLayout = new FrameLayout(context);
            ViewGroup parent = (ViewGroup) anchor.getParent();
            indexOfChild = parent.indexOfChild(anchor);
            parent.removeView(anchor);
            if (indexOfChild >= 0) {
                parent.addView(frameLayout, indexOfChild, anchor.getLayoutParams());
            } else {
                parent.addView(frameLayout, anchor.getLayoutParams());
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(anchor, lp);
            mParentView = frameLayout;
        }

        sp = context.getSharedPreferences(NewbieGuide.TAG, Activity.MODE_PRIVATE);

    }

    public static GuideUtil newInstance(Activity context) {
        return new GuideUtil(context);
    }

    public GuideUtil AddGuideLayout(GuideLayout guideLayout){
        mGuideLayouts.add(guideLayout);
        return this;
    }

    public GuideUtil setTag(String tag){
        this.tag = tag;
        return this;
    }

    public GuideUtil setShowCounts(int showCounts){
        this.showCounts = showCounts;
        return this;
    }

    public void show(){
        int showed = sp.getInt(tag, 0);
        if(showed>=showCounts){
            return;
        }

        if(mGuideLayouts==null || mGuideLayouts.size()==0){
            return;
        }

        mCurrent = 0;
        showGuidePage();

        sp.edit().putInt(tag, showed + 1).apply();

    }

    private void showGuidePage(){

        if(mCurrent>=mGuideLayouts.size()){
            return;
        }
        GuideLayout guideLayout = mGuideLayouts.get(mCurrent);
        addCustomToLayout(guideLayout);

        guideLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
            @Override
            public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                mCurrent++;
                showGuidePage();
            }
        });

        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addCustomToLayout(final GuideLayout guideLayout) {
        guideLayout.removeAllViews();
        View view = LayoutInflater.from(guideLayout.getContext()).inflate(guideLayout.mPage.mLayoutRes, guideLayout, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideLayout.remove();
            }
        });

        guideLayout.addView(view, params);
    }
}
