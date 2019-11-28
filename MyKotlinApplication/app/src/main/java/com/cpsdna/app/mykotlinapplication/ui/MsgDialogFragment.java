package com.cpsdna.app.mykotlinapplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpsdna.app.mykotlinapplication.R;
import com.cpsdna.app.mykotlinapplication.TopApplication;
import com.cpsdna.app.mykotlinapplication.net.CallServerNohttp;
import com.cpsdna.app.mykotlinapplication.util.GLog.GLog;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.File;

/**
 * Created by leng on 2019/5/27.
 * 版本升级提示框
 */
public class MsgDialogFragment extends DialogFragment {

    public static String TAG = "MsgDialogFragment";

    private String apk_url = "http://t-newspaper-management.aegis-info.com/apkVersion?deviceId=9606fee22d5e9cbdaca559684a5d3746&apkName=fayuanbao_home_20190618_1345_1.0.0_u1";
    private String apk_name = "fayuanbao_home_20190618_1345_1.0.1_u1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
    }

    ImageView close;
    TextView msg_tv;
    TextView updata_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_msg, container, false);

        if(getArguments()!=null){
            apk_url = getArguments().getString("apk_url");
            apk_name = getArguments().getString("apk_name");
        }

        close = view.findViewById(R.id.close);
        msg_tv = view.findViewById(R.id.msg_tv);
        updata_tv = view.findViewById(R.id.updata_tv);

        msg_tv.setText("发现新版本："+apk_name);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        updata_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close.setVisibility(View.GONE);
                msg_tv.setText("正在准备下载新版app，请稍等");
                updata_tv.setVisibility(View.GONE);
                doDownLoadTask();
            }
        });

        return view;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                msg_tv.setText("已下载"+mProgress+"%");
            }else if(msg.what == 1){
                msg_tv.setText("下载失败，请重试！");
                updata_tv.setVisibility(View.VISIBLE);
            }

        }
    };

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    private void doDownLoadTask() {

        DownloadRequest downloadRequest = new DownloadRequest(apk_url,
                RequestMethod.GET,
                getSDPath() + File.separator + "aegis",
                true, true);
        // what 区分下载。
        // downloadRequest 下载请求对象。
        // downloadListener 下载监听。
        CallServerNohttp.getInstance().download(0, downloadRequest, downloadListener);
    }

    int mProgress = 0;
    DownloadListener downloadListener = new DownloadListener() {


        @Override
        public void onDownloadError(int what, Exception exception) {
            GLog.d("onDownloadError", "");
            handler.sendEmptyMessage(1);
        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
            GLog.d("onStart", "");
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            GLog.d("onProgress  :", progress + "");
            mProgress = progress;
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onFinish(int what, String filePath) {
            GLog.d("onFinish", "");
            File file = new File(filePath);
            if (mProgress < 100 || file.length() < 1000) {
                GLog.d(TAG, "自动更新下载失败");
                handler.sendEmptyMessage(1);
//                CustomToast.showLongToast(TopApplication.getTopApplication(), "下载失败");
                return;
            }
            mProgress = 100;
            // 下载成功 但不一定安装 先保存md之后自动更新校验
            GLog.e(TAG, "开始安装应用: ");
            installApk(file);
            // 使用完后需要关闭队列释放CPU：
//            queue.stop();
        }

        @Override
        public void onCancel(int what) {
            Log.e("onCancel", "");
        }
    };

    //安装apk
    public void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 必须新任务栈
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(TopApplication.getTopApplication(), "com.info.aegis.lawpush4android.fileProvider", file);
        } else {
            uri = Uri.fromFile(file); // Android N  -  FC
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
        dismiss();
    }


    @Override
    public void onStart() {
        if(getDialog()==null){
            super.onStart();
            return;
        }
        Window window = getDialog().getWindow();
        WindowManager wm = window != null ? window.getWindowManager() : null;

        Point windowSize = new Point();
        if (wm != null) {
            wm.getDefaultDisplay().getSize(windowSize);
            // 设置宽度为屏幕宽度88%
            window.getAttributes().width = (int) (windowSize.x * 1.0f);
            window.getAttributes().height = (int) (windowSize.y * 1.1f);
            window.setGravity(Gravity.CENTER);
        }
        super.onStart();
    }

}
