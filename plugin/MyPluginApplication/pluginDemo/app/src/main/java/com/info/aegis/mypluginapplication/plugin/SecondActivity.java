package com.info.aegis.mypluginapplication.plugin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.info.aegis.baselibrary.base.LibraryActivity;
import com.info.aegis.mypluginapplication.plugin.R;

public class SecondActivity extends LibraryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity_second);

        ImageView imageView = findViewById(R.id.aaaaaa);
        Glide.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585562510609&di=73e479c1b8850e7d276cff1ca1e180f7&imgtype=0&src=http%3A%2F%2F2b.zol-img.com.cn%2Fproduct%2F94_940x705%2F961%2FceEXnERYsTFs.jpg")
//                .load(R.drawable.timg)
                .into(imageView);
        Log.e("plugin SecondActivity","onCreate::" +imageView.getId()+"//"+imageView.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("plugin SecondActivity","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("plugin SecondActivity","onResume");
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.e("plugin SecondActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("plugin SecondActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("plugin SecondActivity","onDestroy");
    }
}
