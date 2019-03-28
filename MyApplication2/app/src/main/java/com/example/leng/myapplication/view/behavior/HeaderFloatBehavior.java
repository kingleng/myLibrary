//package com.example.leng.myapplication.view.behavior;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.support.design.widget.CoordinatorLayout;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.example.leng.myapplication.R;
//
//import java.lang.ref.WeakReference;
//
///**
// * Created by 17092234 on 2018/6/11.
// */
//
//public class HeaderFloatBehavior  extends CoordinatorLayout.Behavior<View>{
//
//    WeakReference<View> dependentView;
//
//    public HeaderFloatBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//    }
//
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        if(dependency!=null && dependency.getId() == R.id.edit_search){
//            dependentView = new WeakReference<View>(dependency);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        Resources resources = getDependentView().getResources();
//
//        final float progress = 1.0f - Math.abs(dependency.getTranslationY()/(dependency.getHeight() - resources.getDimension(R.dimen.collapsed_header_height)));
//
//        final float collapsedOffset = resources.getDimension(R.dimen.collapsed_float_offset_y);
//        final float initOffset = resources.getDimension(R.dimen.init_float_offset_y);
//        final float translateY = collapsedOffset + (initOffset - collapsedOffset)*progress;
//
//        child.setTranslationY(translateY);
//
//        return super.onDependentViewChanged(parent, child, dependency);
//    }
//
//    private View getDependentView() {
//        return dependentView.get();
//    }
//}
