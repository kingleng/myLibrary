package com.example.leng.myapplication.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.adapter.QuickAdapter;
import com.example.mylibrary.image.MyGlide;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CoordinatorLayoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<String> myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        final String cheeseName = "Title";

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData();
        recyclerView.setAdapter(new QuickAdapter(myData) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.list_item;
            }

            @Override
            public void convert(VH holder, Object data, int position) {
                ((TextView)holder.getView(R.id.tv_item)).setText((String)data);
            }
        });

//        final Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> e) throws Exception {
//                e.onNext("hello RxJava 2");
//                e.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER);

//        final Flowable<String> flowable = Flowable.just("hello RxJava 2");

        // create
//        final Subscriber subscriber = new Subscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                System.out.println("onSubscribe");
//                s.request(Long.MAX_VALUE);
//            }
//
//            @Override
//            public void onNext(String s) {
//                System.out.println(s);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("onComplete");
//            }
//        };

        final Consumer consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        };


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CoordinatorLayoutActivity.this,CoordinatorLayout2Activity.class));

//                flowable.subscribe(consumer);
//                Flowable.just("hello RxJava 2")
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                System.out.println(s);
//                            }
//                        });
//                Flowable.just("map")
//                        .map(new Function<String, String>() {
//                            @Override
//                            public String apply(String s) throws Exception {
//                                return s + " -ittianyu";
//                            }
//                        })
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                System.out.println(s);
//                            }
//                        });

                Flowable.just("map1")
                        .map(new Function<String, Integer>() {
                            @Override
                            public Integer apply(String s) throws Exception {
                                return s.hashCode();
                            }
                        })
                        .map(new Function<Integer, String>() {
                            @Override
                            public String apply(Integer integer) throws Exception {
                                return integer.toString();
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                System.out.println(s);
                            }
                        });

            }
        });
    }

    ImageView imageView;
    private void loadBackdrop() {
        imageView = (ImageView) findViewById(R.id.backdrop);

        MyGlide.ImageDownLoader(this,"http://dl.bizhi.sogou.com/images/2012/02/16/187433.jpg",R.drawable.default_pic,imageView);
//        Glide.with(this)
//                .load("http://dl.bizhi.sogou.com/images/2012/02/16/187433.jpg")
//                .placeholder(R.drawable.default_pic)//加载中显示的图片
////                .error(R.drawable.default_pic)//加载失败时显示的图片
////                .crossFade()//淡入显示,注意:如果设置了这个,则必须要去掉asBitmap
////                .override(80,80)//设置最终显示的图片像素为80*80,注意:这个是像素,而不是控件的宽高
////                .centerCrop()//中心裁剪,缩放填充至整个ImageView
////                .skipMemoryCache(true)//跳过内存缓存
////                .diskCacheStrategy(DiskCacheStrategy.RESULT)// DiskCacheStrategy.NONE:什么都不缓存 DiskCacheStrategy.SOURCE:仅缓存原图(全分辨率的图片) DiskCacheStrategy.RESULT:仅缓存最终的图片,即修改了尺寸或者转换后的图片 DiskCacheStrategy.ALL:缓存所有版本的图片,默认模式
////                .thumbnail(0.1f)//10%的原图大小
//                .into(imageView);


    }

    class MyThread extends Thread{
        private Context context = null;

        public MyThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            super.run();
            if(context != null){
                try {
                    Bitmap myBitmap = Glide.with(context)
                            .load("http://dl.bizhi.sogou.com/images/2012/02/16/187433.jpg")
                            .asBitmap() //必须
                            .centerCrop()
                            .into(500, 500)
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    void setData(){
        myData = new ArrayList<>();
        myData.add("第一次");
        myData.add("第2次");
        myData.add("第3次");
        myData.add("第4次");
        myData.add("第5次");
        myData.add("第6次");
        myData.add("第7次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
        myData.add("第8次");
    }
}
