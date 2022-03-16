package com.example.leng.myapplication2.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.tools.SlideUtil;

public class XuanFuActivity extends Activity {

    View shareLayout;

    SlideUtil mSlideUtil;

    SubsamplingScaleImageView imageView;

    private static final int MAX_SIZE = 4096;
    private static final int MAX_SCALE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_fu);

        imageView  = (SubsamplingScaleImageView) findViewById(R.id.imageView);

        shareLayout = findViewById(R.id.share_layout);

        mSlideUtil = new SlideUtil.Builder(shareLayout)
//                .withDistance(DensityUtil.dip2px(this, 100))
                .withDistance(300)
                .build();

        mSlideUtil.hide(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSlideUtil.show(true);
            }
        },2000);



        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSlideUtil.isShow()){
                    mSlideUtil.hide(true);
                }else{
                    mSlideUtil.show(true);
                }

            }
        });

        Glide.with(this).load("http://newspaper-management.aegis-info.com/file/download?name=9fb2b92fb0a648ca86f65289c0bea4ce.jpeg").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                float scale = getImageScale(XuanFuActivity.this,resource);
                imageView.setImage(ImageSource.bitmap(resource),new ImageViewState(scale, new PointF(0, 0), 0));
            }
        });
    }


    /**
     * 计算出图片初次显示需要放大倍数
     */
    public float getImageScale(Context context, Bitmap bitmap){

        if(bitmap == null) {
            return 2.0f;
        }

        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

        WindowManager wm = ((Activity)context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
//        bitmap.recycle();
        return scale;
    }

}
