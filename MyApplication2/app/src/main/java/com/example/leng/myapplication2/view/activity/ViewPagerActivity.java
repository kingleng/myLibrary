package com.example.leng.myapplication2.view.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.adapter.My2PagerAdapter;
import com.example.leng.myapplication2.view.myView.DepthViewPager;
import com.example.leng.myapplication2.view.tools.DensityUtil;
import com.example.leng.myapplication2.view.tools.ShapeBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ys.duodian.com.baselibrary.BaseAvtivity;
import ys.duodian.com.baselibrary.net.NetworkCallBack;
import ys.duodian.com.baselibrary.net.okhttp.OKHttpHelper;

public class ViewPagerActivity extends BaseAvtivity {

    DepthViewPager viewPager;
    LinearLayout dotLayout;
    List<ImageView> dotLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = (DepthViewPager)findViewById(R.id.viewpager);
        dotLayout = (LinearLayout)findViewById(R.id.dot_layout);

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

        dotLayout.removeAllViews();
        dotLists.clear();
        for(int i=0;i<7;i++){
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20,20);
            lp.leftMargin = 20;
            lp.rightMargin = 20;
            imageView.setLayoutParams(lp);
            if(i==0){
                imageView.setImageDrawable(ShapeBuilder.create().Radius(10).Solid(Color.BLUE).build());
            }else{
                imageView.setImageDrawable(ShapeBuilder.create().Radius(10).Stroke(2,Color.WHITE).build());
            }

            dotLayout.addView(imageView);
            dotLists.add(imageView);
        }


        My2PagerAdapter adapter = new My2PagerAdapter(this.getBaseContext(),viewList);
        //设置item的间距
        viewPager.setPageMargin(DensityUtil.dip2px(this,10));
        viewPager.setAdapter(adapter);
        viewPager.setListener(new DepthViewPager.OnItemChangeListener() {
            @Override
            public void onItemChang(int postion) {
                for(int i=0;i<dotLists.size();i++){
                    if(postion == i){
                        dotLists.get(i).setImageDrawable(ShapeBuilder.create().Radius(10).Solid(Color.BLUE).build());
                    }else{
                        dotLists.get(i).setImageDrawable(ShapeBuilder.create().Radius(10).Stroke(2,Color.WHITE).build());
                    }
                }
            }
        });
//        //设置缓存数
//        viewPager.setOffscreenPageLimit(5);


//        OKHttpHelper okHttpUtil = new OKHttpHelper();
//        okHttpUtil.get(this,"http://imonitor.yu-qing.com/api/wechat/data/top_court?page=1&size=10", new NetworkCallBack() {
//            @Override
//            public void onSuccess(JSONObject jsonObject) {
//                Log.e("asd","jsonObject = "+jsonObject);
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });

        execute("http://imonitor.yu-qing.com/api/wechat/data/top_court?page=1&size=10",null);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.e("asd","onSuccess = "+jsonObject.toString());
    }

    @Override
    public void onFailure() {

    }
}
