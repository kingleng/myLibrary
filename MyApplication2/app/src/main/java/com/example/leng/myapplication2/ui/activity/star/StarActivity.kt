package com.example.leng.myapplication2.ui.activity.star

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.leng.myapplication2.R
import com.example.leng.myapplication2.ui.myView.star.StarView

class StarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star)

        var star = findViewById<StarView>(R.id.star)
    }
}