package com.kingleng.app2library.ui.activity

import android.os.Bundle
import com.info.aegis.kl_annotation.Hello
import com.kingleng.app2library.R
import com.kingleng.baselibrary.BaseAvtivity

@Hello(value = "app2_Main2Activity")
class Main2Activity : BaseAvtivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

    }
}
