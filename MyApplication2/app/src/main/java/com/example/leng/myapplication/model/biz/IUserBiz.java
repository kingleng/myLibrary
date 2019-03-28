package com.example.leng.myapplication.model.biz;


/**
 * Created by leng on 2017/2/21.
 */

public interface IUserBiz {
    void Login(String userNmae,String passWord,LoginLisenter lisenter);
}
