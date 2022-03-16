package com.kingleng.app2library.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by leng on 2020/7/1.
 */
public class User extends BaseObservable {

    private String id;
    private String username;
    private String passwd;

    public User(String id, String username, String passwd) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(com.kingleng.app2library.BR.username);

    }

    @Bindable
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
        notifyChange();
    }
}
