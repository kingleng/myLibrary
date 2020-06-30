package com.example.leng.myapplication2.ui.activity;

import android.content.Context;
import android.content.Intent;
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

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.adapter.QuickAdapter;
import com.example.mylibrary.image.MyGlide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

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
                startActivity(new Intent(CoordinatorLayoutActivity.this,CoordinatorLayout2Activity.class));

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

//                Flowable.just("map1")
//                        .map(new Function<String, Integer>() {
//                            @Override
//                            public Integer apply(String s) throws Exception {
//                                return s.hashCode();
//                            }
//                        })
//                        .map(new Function<Integer, String>() {
//                            @Override
//                            public String apply(Integer integer) throws Exception {
//                                return integer.toString();
//                            }
//                        })
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                System.out.println(s);
//                            }
//                        });

            }
        });
    }

    ImageView imageView;
    private void loadBackdrop() {
        imageView = (ImageView) findViewById(R.id.backdrop);

//        MyGlide.ImageDownLoader(this,"http://dl.bizhi.sogou.com/images/2012/02/16/187433.jpg",R.drawable.default_pic,imageView);
        MyGlide.ImageDownLoader(this,"https://test.cpsdna.com//files//81044301300//takelook//20170419//180202_25_thumb.jpg",R.drawable.default_pic,imageView);


    }

    class MyThread extends Thread{
        private Context context = null;

        public MyThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            super.run();
//            if(context != null){
//                try {
//                    Bitmap myBitmap = Glide.with(context)
//                            .load("http://dl.bizhi.sogou.com/images/2012/02/16/187433.jpg")
//                            .asBitmap() //必须
//                            .centerCrop()
//                            .into(500, 500)
//                            .get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }

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
