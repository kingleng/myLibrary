package com.example.leng.myapplication2.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.QuickAdapter;
import com.example.leng.myapplication2.ui.HomeActivity;
import com.example.mylibrary.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewPagerActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<String> data = new ArrayList<>();

    //不允许父控件拦截
    boolean disallowIntercept = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_pager);
        recyclerView = findViewById(R.id.recyclerview_ll);

        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");
        data.add("a");

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(DensityUtil.dp2px(RecyclerViewPagerActivity.this, 5), DensityUtil.dp2px(RecyclerViewPagerActivity.this, 5), DensityUtil.dp2px(RecyclerViewPagerActivity.this, 5), 0);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuickAdapter<String>(data) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_layout_wrap;
            }

            @Override
            public void convert(VH holder, String data, int position) {

                View layout_ll = holder.getView(R.id.layout_ll);
                TextView textView = holder.getView(R.id.text);
                textView.setText(data);
                int a = position%3;
                if(a == 0){
                    textView.setBackgroundResource(R.color.color_fde801);
                    layout_ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(RecyclerViewPagerActivity.this,540)));
                }else if(a == 1){
                    textView.setBackgroundResource(R.color.color_ffaaff);
                    layout_ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(RecyclerViewPagerActivity.this,640)));
                }else{
                    textView.setBackgroundResource(R.color.colorAccent);
                    layout_ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(RecyclerViewPagerActivity.this,600)));
                }
            }

        });


        MyPagerSnapHelper snapHelper = new MyPagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);//初始化数据


//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            float lastY;
//            @Override
//            public boolean onTouch(View v, MotionEvent e) {
//                Log.e("asd","e.getAction() = "+e.getAction());
//                switch (e.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        disallowIntercept = false;
//                        lastY = e.getY();
//                        return false;
//                    case MotionEvent.ACTION_MOVE:
//
//                        //找到被点击位置的item的rootView
//                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                        if (view != null) {
////                    通过rootView找到对应的ViewHolder
//                            QuickAdapter.VH holder = (QuickAdapter.VH)recyclerView.getChildViewHolder(view);
//                            //由ViewHolder决定要不要请求不拦截,如果不拦截的话event就回一路传到rootView中.否则被rv消费.
//                            ScrollView scrollview_ll = holder.getView(R.id.scrollview_ll);
//                            if(lastY!=0){
//                                float dY = e.getY()-lastY;
//                                Log.e("asd","lastEvent.getY() = "+lastY);                               Log.e("asd","e.getY() = "+e.getY());
//                                Log.e("asd","dY = "+dY);
//                                if(dY>0){
//                                    //下拉
//                                    boolean isTop = true;
//                                    disallowIntercept = scrollview_ll.arrowScroll(-1);
////                                    recyclerView.requestDisallowInterceptTouchEvent(disallowIntercept);
//                            Log.e("asd","isTop:disallowIntercept = "+disallowIntercept+"");
////                            rv.requestDisallowInterceptTouchEvent(isTop);
//                                }else{
//                                    //上滑
//                                    boolean isBottom = true;
//                                    disallowIntercept = scrollview_ll.arrowScroll(1);
////                                    recyclerView.requestDisallowInterceptTouchEvent(disallowIntercept);
//                            Log.e("asd","isBottom:disallowIntercept = "+disallowIntercept+"");
////                            rv.requestDisallowInterceptTouchEvent(isBottom);
//                                }
//                            }
//                        }
//                        lastY = e.getY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        lastY = 0;
//                        recyclerView.requestDisallowInterceptTouchEvent(false);
//                        return false;
//                }
//
//
//                return false;
//            }
//        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){

            float lastY;
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                Log.e("asd","addOnItemTouchListener:onInterceptTouchEvent");
//
//
//                //找到被点击位置的item的rootView
//                View view = rv.findChildViewUnder(e.getX(), e.getY());
//                if (view != null) {
////                    通过rootView找到对应的ViewHolder
//                    QuickAdapter.VH holder = (QuickAdapter.VH)rv.getChildViewHolder(view);
//                    boolean ss = !interceptTouchEvent(holder);
//                    Log.e("asd","DisallowInterceptTouchEvent = "+ss);
//                    rv.requestDisallowInterceptTouchEvent(ss);
//                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.e("asd","addOnItemTouchListener:onTouchEvent");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.e("asd","onRequestDisallowInterceptTouchEvent = "+disallowIntercept);
            }

            public boolean interceptTouchEvent(QuickAdapter.VH holder){
                //由ViewHolder决定要不要请求不拦截,如果不拦截的话event就回一路传到rootView中.否则被rv消费.
                ScrollView scrollview_ll = holder.getView(R.id.scrollview_ll);
                return !scrollview_ll.canScrollVertically(1);
            }
        });

    }

    class  MyPagerSnapHelper extends PagerSnapHelper {
        private OrientationHelper mVerticalHelper, mHorizontalHelper;

        public MyPagerSnapHelper() {

        }

        @Override
        public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
                throws IllegalStateException {
            super.attachToRecyclerView(recyclerView);
        }

        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                                  @NonNull View targetView) {
            int[] out = new int[2];

            if (layoutManager.canScrollHorizontally()) {
                out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
            } else {
                out[0] = 0;
            }

            if (layoutManager.canScrollVertically()) {
                out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
            } else {
                out[1] = 0;
            }
            return out;
        }

        @Override
        public View findSnapView(RecyclerView.LayoutManager layoutManager) {

            if (layoutManager instanceof LinearLayoutManager) {

                if (layoutManager.canScrollHorizontally()) {
                    return getStartView(layoutManager, getHorizontalHelper(layoutManager));
                } else {
                    return getStartView(layoutManager, getVerticalHelper(layoutManager));
                }
            }

            return super.findSnapView(layoutManager);
        }

        private int distanceToStart(View targetView, OrientationHelper helper) {
            return helper.getDecoratedStart(targetView);
        }

        private View getStartView(RecyclerView.LayoutManager layoutManager,
                                  OrientationHelper helper) {

            if (layoutManager instanceof LinearLayoutManager) {
                int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                boolean isLastItem = ((LinearLayoutManager) layoutManager)
                        .findLastCompletelyVisibleItemPosition()
                        == layoutManager.getItemCount() - 1;

                if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                    return null;
                }

                View child = layoutManager.findViewByPosition(firstChild);

                if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                        && helper.getDecoratedEnd(child) > 0) {
                    return child;
                } else {
                    if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition()
                            == layoutManager.getItemCount() - 1) {
                        return null;
                    } else {
                        return layoutManager.findViewByPosition(firstChild + 1);
                    }
                }
            }

            return super.findSnapView(layoutManager);
        }

        private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
            if (mVerticalHelper == null) {
                mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
            }
            return mVerticalHelper;
        }

        private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
            if (mHorizontalHelper == null) {
                mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
            }
            return mHorizontalHelper;
        }
    }


}