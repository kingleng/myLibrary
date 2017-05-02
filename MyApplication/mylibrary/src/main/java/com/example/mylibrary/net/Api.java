package com.example.mylibrary.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leng on 2017/4/25.
 */

public class Api {
    String url = "https://api.github.com/";
    Retrofit retrofit;

    public Api() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getData(){
        retrofitInterface service = retrofit.create(retrofitInterface.class);
        Call<List<String>> repos = service.listRepos("octocat");
    }


}
