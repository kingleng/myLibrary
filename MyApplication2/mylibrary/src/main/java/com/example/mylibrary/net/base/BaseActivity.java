package com.example.mylibrary.net.base;

import android.app.Activity;
import android.os.Bundle;

import com.example.mylibrary.Constants;
import com.example.mylibrary.net.NetInterf;
import com.example.mylibrary.net.NetMessageInfo;
import com.example.mylibrary.net.retrofitInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leng on 2017/5/2.
 */

public class BaseActivity extends Activity implements NetInterf {

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void netPost(){
        retrofitInterface service = retrofit.create(retrofitInterface.class);
        Call<List<String>> repos = service.listRepos("octocat");

        repos.enqueue(new Callback<List<String>>(){

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }

        });

    }



    @Override
    public void uiSuccess(NetMessageInfo msg) {

    }

    @Override
    public void uiFailure(NetMessageInfo msg) {

    }

    @Override
    public void uiError(NetMessageInfo msg) {

    }

    @Override
    public void uiFinish(NetMessageInfo msg) {

    }
}
