package com.example.mylibrary.net;

import android.os.Environment;

import com.example.mylibrary.net.bean.User;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leng on 2017/4/25.
 */

public class Api {
    String url = "https://api.github.com/";
    Retrofit retrofit;
    retrofitInterface service;

    public Api() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
//                .callFactory(OkHttpUtils.getClient())
                .build();
        service = retrofit.create(retrofitInterface.class);

    }

    public void getData(){
//        retrofitInterface service = retrofit.create(retrofitInterface.class);
        Call<List<String>> repos = service.listRepos("octocat");
    }

    //单文件上传@Multipart
    public void registerUser(){
        File file = new File(Environment.getExternalStorageDirectory(), "icon.png");
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photos", "icon.png", photoRequestBody);

        Call<User> call = service.registerUser(photo, RequestBody.create(null, "abc"), RequestBody.create(null, "123"));
    }


}
