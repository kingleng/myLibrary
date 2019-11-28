package com.cpsdna.app.mykotlinapplication.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cpsdna.app.mykotlinapplication.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn.setOnClickListener(View.OnClickListener {
            val ss:String = textview.text.toString()
            val intent = Intent(this,WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString("url",ss)
            intent.putExtras(bundle)
            startActivity(intent)
        })
    }
}
