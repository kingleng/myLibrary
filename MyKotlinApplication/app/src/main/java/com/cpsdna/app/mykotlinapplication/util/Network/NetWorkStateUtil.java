package com.cpsdna.app.mykotlinapplication.util.Network;

/**
 * Created by gallon on 2018/1/25.
 */

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.cpsdna.app.mykotlinapplication.SharePreHelper;
import com.cpsdna.app.mykotlinapplication.util.GLog.GLog;

import org.greenrobot.eventbus.EventBus;

/**
 * PING网络状况 (优于广播 广播不可识别连上网不可用情况)
 * Created by gallon on 2018/1/25.
 */
public class NetWorkStateUtil {
    private static final String TAG = "NetWorkStateUtil";

    private static long latestToast = 0;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            new Thread() {
                @Override
                public void run() {
                    boolean connected = NetworkUtils.isConnected();
                    String msg = "";
                    long t1 = System.currentTimeMillis();
                    boolean availableByPing = NetworkUtils.isAvailableByPing();
                    long t2 = System.currentTimeMillis();
                    long t = t2 - t1;
                    GLog.d(TAG, "t2-t1:" + t);
                    if (connected && availableByPing) { //网络连接 PING通过
                    }
                    if (connected && !availableByPing) { //网络连接 PING不通过
                        msg = "网络状况异常，请检查网络状况";
                    }
                    if (!connected) { //网络未连接
                        msg = "网络未连接，请检查网络设置";
                    }
                    if (TextUtils.isEmpty(msg)) {
                        EventBus.getDefault().post(new NetWorkStateEvent(t2 - t1));
                    } else {
                        EventBus.getDefault().post(new NetWorkStateEvent(999));
                    }
                    if (t > 3000 && System.currentTimeMillis() > (latestToast + 10000)) {
                        latestToast = System.currentTimeMillis();
                        msg = "网络状况一般，请检查网络环境";
                        Log.e(TAG, "Ping " + t + "ms");
                    }
                    final String message = msg;
                    if (!TextUtils.isEmpty(message)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (SharePreHelper.getAsrType().equals("local"))
                                    return;
//                                CustomToast.showLongToast(message);
                            }
                        });
                    }
                    handler.sendEmptyMessageDelayed(0, 3500);
                }
            }.start();
        }
    };

    public static void init() {
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    public static class NetWorkStateEvent {

        public long ping;

        NetWorkStateEvent(long ping) {
            this.ping = ping;
        }

    }

}
