package com.example.leng.myapplication2.model.biz;


/**
 * Created by leng on 2017/2/21.
 */

public class UserBiz implements IUserBiz {

    @Override
    public void Login(String userNmae,String passWord,LoginLisenter lisenter) {

        if(userNmae!=null && passWord!=null){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lisenter.loginSuccess();
        }else{
            lisenter.loginFailed();
        }

    }



}
