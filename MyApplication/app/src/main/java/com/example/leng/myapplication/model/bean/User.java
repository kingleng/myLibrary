package com.example.leng.myapplication.model.bean;

/**
 * Created by leng on 2017/2/21.
 */

public class User {

    String name;
    String age;

    public User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    static class Builder{
        private String name;
        private String age;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(String age) {
            this.age = age;
            return this;
        }

        public void Build() {
            new User(this);
        }

    }
}
