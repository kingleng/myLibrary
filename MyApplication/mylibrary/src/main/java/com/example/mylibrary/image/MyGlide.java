package com.example.mylibrary.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by leng on 2017/4/24.
 * 图片加载工具类
 */

public class MyGlide {

    //ImageView的资源回收问题
    public static void simapleImageDownLoader(Context context ,String url,ImageView view){
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(view);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context).load(url).into(target);
        }
//        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    public static void ImageDownLoader(Context context ,String url,int defultImage,ImageView view){

        Glide.with(context)
                .load(url)
                .placeholder(defultImage)//加载中显示的图片
//                .error(defultImage)//加载失败时显示的图片
//                .crossFade()//淡入显示,注意:如果设置了这个,则必须要去掉asBitmap
//                .override(80,80)//设置最终显示的图片像素为80*80,注意:这个是像素,而不是控件的宽高
//                .centerCrop()//中心裁剪,缩放填充至整个ImageView
//                .skipMemoryCache(true)//跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)// DiskCacheStrategy.NONE:什么都不缓存 DiskCacheStrategy.SOURCE:仅缓存原图(全分辨率的图片) DiskCacheStrategy.RESULT:仅缓存最终的图片,即修改了尺寸或者转换后的图片 DiskCacheStrategy.ALL:缓存所有版本的图片,默认模式
//                .thumbnail(0.1f)//10%的原图大小
                .into(view);
    }

    //先显示缩略图，再显示原图
    public static void smallToBigDownLoader(Context context ,String url,int defultImage,ImageView view){

        //用原图的1/10作为缩略图
        Glide.with(context)
                .load(url)
                .thumbnail(0.1f)
                .into(view);

//        //用其它图片作为缩略图
//        DrawableRequestBuilder<Integer> thumbnailRequest = Glide
//                .with(this)
//                .load(R.drawable.news);
//        Glide.with(this)
//                .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
//                .thumbnail(thumbnailRequest)
//                .into(iv_0);

    }

    //对图片进行裁剪、模糊、滤镜等处理
    public static void ImageHandleDownLoader(Context context ,String url,int defultImage,ImageView view){

        //圆形裁剪
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(view);
//        //圆角处理
//        Glide.with(context)
//                .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
//                .bitmapTransform(new RoundedCornersTransformation(context,30,0, RoundedCornersTransformation.CornerType.ALL))
//                .into(view);
//        //灰度处理
//        Glide.with(context)
//                .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
//                .bitmapTransform(new GrayscaleTransformation(context))
//                .into(view);
//
//        //原图的毛玻璃、高斯模糊效果
//        Glide.with(context).
//                load(url).
//                bitmapTransform(new BlurTransformation(context, 25))
//                .crossFade(1000)
//                .into(view);

    }
}
