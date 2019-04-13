package ys.duodian.com.baselibrary;

import android.app.Activity;

import org.json.JSONObject;

import java.util.Map;

import ys.duodian.com.baselibrary.net.NetworkCallBack;
import ys.duodian.com.baselibrary.net.okhttp.OKHttpHelper;

/**
 * Created by leng on 2019/4/11.
 */
public class BaseAvtivity extends Activity implements NetworkCallBack {

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
