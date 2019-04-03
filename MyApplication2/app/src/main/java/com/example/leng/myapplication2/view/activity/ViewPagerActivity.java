package com.example.leng.myapplication2.view.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.adapter.DepthPageTransformer;
import com.example.leng.myapplication2.view.adapter.My2PagerAdapter;
import com.example.leng.myapplication2.view.adapter.MyPagerAdapter;
import com.example.leng.myapplication2.view.myView.DepthViewPager;
import com.example.leng.myapplication2.view.tools.DensityUtil;
import com.example.leng.myapplication2.view.tools.WHUtil;
import com.example.mylibrary.image.MyGlide;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    DepthViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = (DepthViewPager)findViewById(R.id.viewpager);

        List<String> viewList = new ArrayList<>();
//        for(int i=0;i<10;i++){
//            }
        viewList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554267220378&di=2b18aca5716fa9a86bbf7dfdb10aa30e&imgtype=0&src=http%3A%2F%2Fpic2.duowan.com%2Flol%2F1006%2F139573151906%2F139573337589.jpg");
        viewList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554267220378&di=88b17acdd90b18ce24d92f79c52b756c&imgtype=0&src=http%3A%2F%2Fpic.3h3.com%2Fup%2F2014-6%2F20146614140943212131.jpg");
        viewList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554267220377&di=2502716a4346de8e1fc49e7a14f63f33&imgtype=0&src=http%3A%2F%2Fimg308.ph.126.net%2FQwxWqHl8sXtgMOvJmYrzsQ%3D%3D%2F3904902351906953473.jpg");
        viewList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554267220376&di=0085dea37db93553b40340b4fcf8eaf4&imgtype=0&src=http%3A%2F%2Fimg.ph.126.net%2FZDQ6_7eu5IewG3ZaNFZ01g%3D%3D%2F3714906742627324240.jpg");
        viewList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2006923881,702799044&fm=26&gp=0.jpg");
        viewList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554267220374&di=1dc47a38e7aa7aa7e23e07d01d6da744&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201501%2F27%2F2142443p3fmhhqaqpihqmn.jpg");
        viewList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1554267220374&di=7711c87c1c0f8d79c35df22184d0487b&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201302%2F17%2F095942v3x3a0lgf8zi3c37.jpg");


        My2PagerAdapter adapter = new My2PagerAdapter(this.getBaseContext(),viewList);
        //设置item的间距
        viewPager.setPageMargin(DensityUtil.dip2px(this,10));
        viewPager.setAdapter(adapter);
        //设置缓存数
        viewPager.setOffscreenPageLimit(5);
//        //设置滑动动效（缩放等）
//        viewPager.setPageTransformer(true, new DepthPageTransformer());
//        viewPager.startSrcoll();

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            int position = 0;
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                this.position = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.e("asd","onPageScrollStateChanged"+state);
//                if(state==0){
//                    if(position == viewPager.getChildCount()-1){
//                        viewPager.setCurrentItem(1,false);
//                    }
//
//                    if(position == 0){
//                        viewPager.setCurrentItem(viewPager.getChildCount()-2,false);
//                    }
//                }
//
//            }
//        });


    }
}
