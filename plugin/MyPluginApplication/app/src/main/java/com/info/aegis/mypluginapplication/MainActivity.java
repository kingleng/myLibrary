package com.info.aegis.mypluginapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.info.aegis.baselibrary.router.BaseModule;
import com.info.aegis.baselibrary.router.PluginModule;
import com.info.aegis.baselibrary.router.TypeManager;
import com.info.aegis.baselibrary.utils.MyGlide;
import com.info.aegis.mypluginapplication.router.RouterManager;
import com.info.aegis.plugincore.PluginManager;
import com.info.aegis.plugincore.plugIn.PlugInfo;

import java.io.File;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 20171222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.image);
        MyGlide.simapleImageDownLoader(this,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585562510609&di=73e479c1b8850e7d276cff1ca1e180f7&imgtype=0&src=http%3A%2F%2F2b.zol-img.com.cn%2Fproduct%2F94_940x705%2F961%2FceEXnERYsTFs.jpg",
                imageView);

        TextView textView = findViewById(R.id.text);
        TextView textView2 = findViewById(R.id.text2);
        textView.setOnClickListener(v -> {
            BaseModule.startActivityByTypeCode(MainActivity.this, "110001");

        });
        textView2.setOnClickListener(v -> {
            BaseModule.startActivityByTypeCode(MainActivity.this, TypeManager.ZNWD_ITEM_0);

        });


        if (hasPermission()) {
            this.loadPlugin();
        } else {
            requestPermission();
        }


    }

    public void loadPlugin() {

        try {
            //加载插件1
            String apk_path = AndroidUtils.getSDPath() + File.separator + "plugin" + File.separator + "plugin.apk";
            if (new File(apk_path).exists()) {
                PluginManager.getInstance().loadPlugin(new File(apk_path));
            } else {
                Toast.makeText(this, "插件不存在1", Toast.LENGTH_SHORT).show();
            }

            //加载插件2
            String apk_path2 = AndroidUtils.getSDPath() + File.separator + "plugin" + File.separator + "plugin2.apk";
            if (new File(apk_path2).exists()) {
                PluginManager.getInstance().loadPlugin(new File(apk_path2));
            } else {
                Toast.makeText(this, "插件不存在2", Toast.LENGTH_SHORT).show();
            }


            //启动插件路由
            Iterator<String> names = PluginManager.getInstance().getPluginPkgToInfoMap().keySet().iterator();
            while(names.hasNext()){
                final String pkg = names.next();
                if (PluginManager.getInstance().getLoadedPlugin(pkg) == null) {
                    Toast.makeText(MainActivity.this, "plugin [" + pkg + "] not loaded", Toast.LENGTH_SHORT).show();
                    return;
                }

                PlugInfo plugInfo = PluginManager.getInstance().getLoadedPlugin(pkg);
                PluginModule mPluginModule = (PluginModule) plugInfo.getClassLoader().loadClass(pkg+".PluginModuleImpl").newInstance();
                if (mPluginModule != null) {
                    mPluginModule.addPluginModule(RouterManager.getInstance());
                    Toast.makeText(MainActivity.this, pkg + " addPluginModule success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, pkg + " addPluginModule failure", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
            Toast.makeText(this, "插件加载出错》》》》"+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                this.loadPlugin();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean hasPermission() {

        Log.d(TAG, "hasPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestPermission() {

        Log.d(TAG, "requestPermission");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE_STORAGE);
        }
    }
}
