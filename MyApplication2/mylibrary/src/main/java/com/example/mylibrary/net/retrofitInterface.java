package com.example.mylibrary.net;

import com.example.mylibrary.net.bean.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by leng on 2017/4/25.
 */

public interface retrofitInterface {

    @GET("users/{user}/repos")
    Call<List<String>> listRepos(@Path("user") String user);

    @GET("group/{id}/users")
    Call<List<String>> groupList(@Path("id") int groupId);

    @POST("users/new")
    Call<User> createUser(@Body User user);

    @FormUrlEncoded
    @POST("user/edit")
    Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);


    //单文件上传@Multipart
    @Multipart
    @POST("register")
    Call<User> registerUser(@Part MultipartBody.Part photo, @Part("username") RequestBody username, @Part("password") RequestBody password);
}
