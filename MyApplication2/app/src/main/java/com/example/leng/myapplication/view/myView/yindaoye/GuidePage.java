package com.example.leng.myapplication.view.myView.yindaoye;

import android.view.View;

/**
 * Created by 17092234 on 2018/6/28.
 */

public class GuidePage {

    public HighLight mHighLight;
    public int mLayoutRes;

    public static GuidePage newInstance() {
        GuidePage mGuidePage = new GuidePage();
        return mGuidePage;
    }

    public GuidePage AddHighLight(View view){
        mHighLight = HighLight.newInstance(view).setPadding(0);
        return this;
    }

    public GuidePage setLayoutRes(int layoutRes){
        this.mLayoutRes = layoutRes;
        return this;
    }

}
