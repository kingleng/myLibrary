package com.example.leng.myapplication2.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.Toast;

import com.example.leng.myapplication2.router.RouterManager;
import com.info.aegis.plugincorelibrary.PluginManager;
import com.info.aegis.plugincorelibrary.plugin.PlugInfo;
import com.kingleng.baselibrary.router.PluginModule;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by leng on 2020/4/14.
 */
public class PluginUtil {



    public static List<Resources> loadPlugin(Context mContext) {
        List<Resources> resources = new ArrayList<>();
        try {
            //加载插件1
            String pluginPath = getSDPath() + File.separator + "klapp" + File.separator + "plugin";
            File pPath = new File(pluginPath);
            if(!pPath.exists()){
                pPath.mkdir();
                return resources;
            }
            if (pPath.exists() && pPath.isDirectory()) {
                File[] files = pPath.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        PluginManager.getInstance().loadPlugin(file);
                    }
                }
            }


            //启动插件路由
            Iterator<String> names = PluginManager.getInstance().getPluginPkgToInfoMap().keySet().iterator();
            while(names.hasNext()){
                final String pkg = names.next();
                if (PluginManager.getInstance().getLoadedPlugin(pkg) == null) {
                    Toast.makeText(mContext, "plugin [" + pkg + "] not loaded", Toast.LENGTH_SHORT).show();
                    return resources;
                }

                PlugInfo plugInfo = PluginManager.getInstance().getLoadedPlugin(pkg);

                resources.add(plugInfo.getResources());

                PluginModule mPluginModule = (PluginModule) plugInfo.getClassLoader().loadClass(pkg+".PluginModuleImpl").newInstance();
                if (mPluginModule != null) {
                    mPluginModule.addPluginModule(RouterManager.getInstance());
                    Toast.makeText(mContext, pkg + " addPluginModule success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, pkg + " addPluginModule failure", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
            Toast.makeText(mContext, "插件加载出错》》》》"+e.toString(), Toast.LENGTH_SHORT).show();
        }

        return resources;

    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
