package com.cpsdna.app.mykotlinapplication.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.cpsdna.app.mykotlinapplication.R
import com.cpsdna.app.mykotlinapplication.ui.base.LibraryActivity
import com.cpsdna.app.mykotlinapplication.ui.fragment.HomeNewFragment
import com.cpsdna.app.mykotlinapplication.ui.widget.PagingScrollHelper
import com.cpsdna.app.mykotlinapplication.ui.widget.VerticalScrollTextView

class PHomeActivity : LibraryActivity() {

    private lateinit var title_time: TextView

    private lateinit var mHomeNewFragment: HomeNewFragment
    private lateinit var fragmentlist: ArrayList<Fragment>

    internal lateinit var mWeatherIcon: ImageView
    internal lateinit var mWeatherVerticalScrollTextView: VerticalScrollTextView

    internal lateinit var toutiao_layout_iv: ImageView
    internal lateinit var toutiaoRecycler: RecyclerView
    internal lateinit var scrollHelper: PagingScrollHelper
    internal lateinit var toutiao_layout_view: View

    internal lateinit var back_ib: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phome)

        fragmentlist = arrayListOf()

        initViews()
        initEvents()
    }

    private fun initViews() {
        mWeatherIcon = findViewById(R.id.id_weather_icon)
        mWeatherVerticalScrollTextView = findViewById(R.id.id_weather_value)
        title_time = findViewById(R.id.title_time)
        toutiao_layout_iv = findViewById(R.id.toutiao_layout_iv)
        toutiaoRecycler = findViewById(R.id.toutiao_layout_recycler)
        toutiaoRecycler.layoutManager = LinearLayoutManager(this)
        toutiao_layout_view = findViewById(R.id.toutiao_layout_view)
        mHomeNewFragment = HomeNewFragment()
        fragmentlist.clear()
        changeFragment(mHomeNewFragment)
        back_ib = findViewById(R.id.back_ib) as ImageButton
    }

    private fun initEvents() {
        back_ib.setOnClickListener { goToBackFragment() }
    }

    //跳转（添加fragment）
    fun changeFragment(fragment: Fragment?) {

        val transaction = supportFragmentManager.beginTransaction()
        for (frag in fragmentlist) {
            transaction.hide(frag)
        }
        transaction.add(R.id.id_container, fragment!!)
        transaction.show(fragment)
        transaction.commitAllowingStateLoss()
        fragmentlist.add(fragment)
    }

    //跳转（替换fragment）
    fun replaceFragment(fragment: Fragment) {
        if (fragmentlist.size > 1) {
            val lastFragment = fragmentlist[fragmentlist.size - 1]
            supportFragmentManager.beginTransaction().remove(lastFragment).commitAllowingStateLoss()
            fragmentlist.removeAt(fragmentlist.size - 1)
        }
        val transaction = supportFragmentManager.beginTransaction()
        var hasItem = false
        for (frag in fragmentlist) {
            if (frag === fragment) {
                hasItem = true
            }
            transaction.hide(frag)
        }
        if (!hasItem) {
            transaction.add(R.id.id_container, fragment)
        }
        transaction.show(fragment)
        transaction.commitAllowingStateLoss()
        fragmentlist.add(fragment)
    }

    //返回首页 （删除fragment）
    private fun goToHomeFragment() {

        if (fragmentlist.size > 1) {
            val fragment = fragmentlist[fragmentlist.size - 2]
            val lastFragment = fragmentlist[fragmentlist.size - 1]
//            getFragmentManager().beginTransaction().replace(R.id.id_container, fragment).commit();


            supportFragmentManager.beginTransaction().remove(lastFragment).commitAllowingStateLoss()
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
            fragmentlist.removeAt(fragmentlist.size - 1)
            goToHomeFragment()
        }
    }

    //返回上个界面 （删除fragment）
    fun goToBackFragment() {

        if (fragmentlist.size > 1) {
            val fragment = fragmentlist[fragmentlist.size - 2]
            val lastFragment = fragmentlist[fragmentlist.size - 1]
//            getFragmentManager().beginTransaction().replace(R.id.id_container, fragment).commit();


            supportFragmentManager.beginTransaction().remove(lastFragment).commitAllowingStateLoss()
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
            fragmentlist.removeAt(fragmentlist.size - 1)
        }

    }

    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.id_container)
    }

}
