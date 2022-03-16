package com.example.leng.myapplication.presenter;

import com.example.leng.myapplication.model.biz.LoginLisenter;
import com.example.leng.myapplication.model.biz.UserBiz;

/**
 * Created by leng on 2017/2/21.
 */

public class UserLoginPresenter {

    private UserBiz userBiz;

    public UserLoginPresenter() {
        userBiz = new UserBiz();
    }

    public void login(String userNmae,String passWord){
        userBiz.Login(userNmae,passWord, new LoginLisenter() {
            @Override
            public void loginSuccess() {

            }

            @Override
            public void loginFailed() {

            }
        });
    }
}
