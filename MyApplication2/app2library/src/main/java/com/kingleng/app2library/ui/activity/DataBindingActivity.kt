package com.kingleng.app2library.ui.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kingleng.app2library.R
import com.kingleng.app2library.databinding.ActivityDataBindingBinding
import com.kingleng.app2library.model.User

class DataBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_data_binding)
        val dataBinding:ActivityDataBindingBinding = DataBindingUtil.setContentView(this,R.layout.activity_data_binding)
        val user = User("1","kingjian","123456");
        dataBinding.user = user
        dataBinding.bclick = ButtonClick(user)

    }

    class ButtonClick(var user:User){
        fun changeName(){
            user.username = "tiantian"
        }

        fun changePd(){
            user.passwd = "000000"
        }
    }
}
