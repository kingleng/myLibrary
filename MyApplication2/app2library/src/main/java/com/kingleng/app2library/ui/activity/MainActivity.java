package com.kingleng.app2library.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.info.aegis.kl_annotation.Hello;
import com.kingleng.app2library.App2Module;
import com.kingleng.app2library.R;
import com.kingleng.app2library.adapter.CycleViewPagerAdapter;
import com.kingleng.app2library.ui.fragment.PicFragment;
import com.kingleng.app2library.utils.RxTimer;

import java.util.ArrayList;
import java.util.List;

@Hello(value = "app2_MainActivity")
public class MainActivity extends AppCompatActivity implements CycleViewPagerAdapter.OnClickListener {

    ViewPager viewPager;

    String[] images = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565675026644&di=a8987ca6161a0d0e80ff9a1fff96eb95&imgtype=0&src=http%3A%2F%2Fpic41.nipic.com%2F20140508%2F18609517_112216473140_2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565675091214&di=a93b1a98becfaa9c8c1e957bb19656aa&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201205%2F28%2F20120528234603_fnRej.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565675091211&di=8756300f1dcdf52e0dd192ae6a9c2ef8&imgtype=0&src=http%3A%2F%2Fimg3.cache.netease.com%2Fhouse%2F2016%2F1%2F20%2F20160120111620ed4e5.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565675091211&di=7d8a5c94bd1086342fc390a902af2826&imgtype=0&src=http%3A%2F%2Fpic75.nipic.com%2Ffile%2F20150820%2F21632384_183019816159_2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565675091266&di=02cfdb535fa168b74ddfcc5823ca0d81&imgtype=0&src=http%3A%2F%2Fimg.redocn.com%2Fsheji%2F20160424%2Fyouxijingjichangjiemianbeijingtu_6210363.jpg"};

    RxTimer rxTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app2_activity_main);

        viewPager = findViewById(R.id.viewpager);

        rxTimer = new RxTimer();

        List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PicFragment picFragment = new PicFragment(this, images[i], i, this);
            mFragments.add(picFragment);
        }

        CycleViewPagerAdapter adapter = new CycleViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//                if(position == 30){
//                    viewPager.setCurrentItem(0,false);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rxTimer.interval(2 * 1000, new RxTimer.RxAction() {
            @Override
            public void action(long number) {
                int num = viewPager.getCurrentItem();
                num++;
                viewPager.setCurrentItem(num, true);
            }
        });
    }

    public void onClick(View view) {
//        App2Module.startActivityByTypeCode(MainActivity.this,"100020");
        App2Module.startActivityByUrl(MainActivity.this, "https://www.jd.com/");
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxTimer.cancel();
    }
}
