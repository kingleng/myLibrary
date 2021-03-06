package com.kingleng.baselibrary;

import android.app.Activity;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import com.kingleng.baselibrary.db.User;
import com.kingleng.baselibrary.db.UserDatabase;
import com.kingleng.baselibrary.net.NetworkCallBack;
import com.kingleng.baselibrary.net.okhttp.OKHttpHelper;

/**
 * Created by leng on 2019/4/11.
 */
public class BaseAvtivity extends Activity implements NetworkCallBack {

    private void test(){
        User user=new User();
        user.setName("name1");
        user.setAge(18);
        UserDatabase.getInstance(this)
                .getUserDao()
                .insert(user);

        List<User> allUsers = UserDatabase
                .getInstance(this)
                .getUserDao()
                .getAllUsers();
    }

    public void execute(String url, Map<String, String> params){
        OKHttpHelper.getInstance(getApplicationContext())
                .addUrl(url)
                .addTag(this)
                .addParams(params)
                .setMethod((params==null)?OKHttpHelper.ReqMethod.GET:OKHttpHelper.ReqMethod.POST).setNetworkCallBack(this)
                .start();
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onFailure() {

    }

    @Override
    protected void onDestroy() {
        OKHttpHelper.getInstance(getApplicationContext()).cancelTag(this);
        super.onDestroy();
    }
}
