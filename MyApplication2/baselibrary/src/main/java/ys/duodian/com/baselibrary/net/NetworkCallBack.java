package ys.duodian.com.baselibrary.net;

import org.json.JSONObject;

/**
 * Created by leng on 2019/4/11.
 */
public interface NetworkCallBack {

    void onSuccess(JSONObject jsonObject);

    void onFailure();

}
