package com.cpsdna.app.mykotlinapplication.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpsdna.app.mykotlinapplication.Content;
import com.cpsdna.app.mykotlinapplication.R;
import com.cpsdna.app.mykotlinapplication.SharePreHelper;
import com.cpsdna.app.mykotlinapplication.module.HomeModuleBean;
import com.cpsdna.app.mykotlinapplication.net.CallServerNohttp;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by leng on 2019/4/28.
 * 海报内容 --  纯图片样式界面
 */
@SuppressLint("ValidFragment")
public class PicFragment extends Fragment {

    Context mContext;
    String imgurl;
    int position;
    boolean isAddItem;
    HomeNewFragment.OnClickListener onClickListener;
    HomeModuleBean homeModuleBean;

    TextView imageViewTag;
    ImageView imageView;
    public String url = "";

    WebView txWebview;

public static final String TAG = PicFragment.class.getSimpleName();

    public PicFragment(Context mContext, String imgurl, int position, HomeModuleBean homeModuleBean, HomeNewFragment.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.imgurl = imgurl;
        this.position = position;
        this.homeModuleBean = homeModuleBean;
        this.onClickListener = onClickListener;


    }

    public PicFragment(Context mContext, String imgurl, int position, boolean isAddItem, HomeNewFragment.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.imgurl = imgurl;
        this.position = position;
        this.isAddItem = isAddItem;
        this.onClickListener = onClickListener;
    }

    private boolean isLoad = false;

    public void show() {
//        if (!isLoad) {
//            getData();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_item3, container, false);

        imageView = (ImageView) view.findViewById(R.id.image_iv);
        imageViewTag = (TextView) view.findViewById(R.id.image_iv_tag);

        txWebview = (WebView) view.findViewById(R.id.webview);

//        url = "https://zgy.ai-risk-v.aegis-info.com/home";
//        url = "https://www.baidu.com";
        url = "https://dp-newspaper.aegis-info.com/#/";

        Glide.with(mContext)
                .load(R.drawable.defult_pic)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view, homeModuleBean.type, url);
                }
            }
        });


        if(position == 1){
            txWebview.setVisibility(View.VISIBLE);

            txWebview.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            txWebview.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
            txWebview.getSettings().setDisplayZoomControls(true); //隐藏原生的缩放控件
            txWebview.getSettings().setBlockNetworkImage(false);//解决图片不显示
            txWebview.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
            txWebview.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
            // 开启 DOM storage API 功能
            txWebview.getSettings().setDomStorageEnabled(true);
            txWebview.getSettings().setUseWideViewPort(true);
            txWebview.getSettings().setLoadWithOverviewMode(true);

//        txWebview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                refreshHander();
//                return false;
//            }
//        });

            txWebview.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    txWebview.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                    sslErrorHandler.proceed();
                }

//                @Override
//                public WebResourceResponse shouldInterceptRequest(WebView view, String ss) {
////                Log.e("url","url="+url);//
////                if (url.contains("app.12a61c65.css")){
////                    try {
////                        return new WebResourceResponse("text/css","utf-8",getBaseContext().getAssets().open("css/app.12a61c65.css"));
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
//
//                    if (ss.contains("plus_logo_web.png")){
//                        try {
//                            return new WebResourceResponse("image/png","utf-8",getActivity().getBaseContext().getAssets().open("plus_logo_web.png"));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    return super.shouldInterceptRequest(view, ss);
//
//                }

                @Nullable
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    String asd = request.getUrl().toString();
                    if(asd.contains("//www.baidu.com/img/bd_logo1.png?where=super")){
                        try {
                            return new WebResourceResponse("image/png","utf-8",getActivity().getBaseContext().getAssets().open("plus_logo_web.png"));
                        } catch (IOException e) {
                            super.shouldInterceptRequest(view, request);
                        }
                    }
                    return super.shouldInterceptRequest(view, request);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    if(url.contains("//www.baidu.com/img/bd_logo1.png?where=super")){
                        super.onLoadResource(view, url);
                    }
                    super.onLoadResource(view, url);
                }
            });

            txWebview.loadUrl(url);

        }else{
            txWebview.setVisibility(View.GONE);
        }


        return view;
    }

    private void getData() {
        getPicInfo();

    }


    //获取图片信息
    private void getPicInfo() {
        String deviceId = SharePreHelper.getDeviceId();
        String url0 = MessageFormat.format("{0}/poster?deviceId={1}", Content.WEB_PATH, deviceId);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url0, RequestMethod.GET);
        CallServerNohttp.getInstance().add(0, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if(getActivity() == null){
                    return;
                }

                JSONObject jsonObject = response.get();
                if (jsonObject.has("code") && jsonObject.optInt("code") == 0
                        && jsonObject.has("data")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        String text = homeModuleBean.title.replace("\n","");

                        imageViewTag.setText(text);

                        String ImageUrl = homeModuleBean.imageUrl;
                        url = homeModuleBean.jumpUrl;

                        if (TextUtils.equals(homeModuleBean.type,"0")) {
                            ImageUrl = data.optString("learnNewTimesPosterUrl");
                        } else if (TextUtils.equals(homeModuleBean.type,"1")) {
                            ImageUrl = data.optString("customModulePosterUrl");
                        } else if (TextUtils.equals(homeModuleBean.type,"3")) {
                            ImageUrl = data.optString("newspaperCover");
                        } else if (TextUtils.equals(homeModuleBean.type,"5")) {
                            ImageUrl = data.optString("magazineCoverUrl");
                        }

                        if(TextUtils.equals(url,"null")){
                            url = "";
                        }

                        if (getActivity() != null && imageView!=null) {
                            isLoad = true;
                            Glide.with(mContext)
                                    .load(ImageUrl)
                                    .error(R.drawable.pic_error) //失败图片
                                    .into(imageView);
                        }
                    }
                }

            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

}
