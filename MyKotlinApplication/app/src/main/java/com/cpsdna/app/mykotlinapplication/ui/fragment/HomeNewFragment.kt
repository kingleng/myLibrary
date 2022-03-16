package com.cpsdna.app.mykotlinapplication.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import com.cpsdna.app.mykotlinapplication.Content
import com.cpsdna.app.mykotlinapplication.R
import com.cpsdna.app.mykotlinapplication.SharePreHelper
import com.cpsdna.app.mykotlinapplication.adapter.CycleViewPager2Adapter
import com.cpsdna.app.mykotlinapplication.module.HomeModuleBean
import com.cpsdna.app.mykotlinapplication.net.CallServerNohttp
import com.cpsdna.app.mykotlinapplication.ui.widget.DepthViewPager
import com.cpsdna.app.mykotlinapplication.ui.widget.StrokeTextView
import com.yanzhenjie.nohttp.NoHttp
import com.yanzhenjie.nohttp.RequestMethod
import com.yanzhenjie.nohttp.rest.OnResponseListener
import com.yanzhenjie.nohttp.rest.Request
import com.yanzhenjie.nohttp.rest.Response
import org.json.JSONArray
import org.json.JSONObject
import java.text.MessageFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeNewFragment : Fragment(), OnClickListener {

    //控制海报的总数
    var pagerNum = 10

    internal var tabBtn0: ImageButton? = null
    internal var btn_layout: LinearLayout? = null

    internal var tabLeft: ImageButton? = null
    internal var tabRight: ImageButton? = null

    internal var dotLayout: LinearLayout? = null
    internal lateinit var mDepthViewPager: DepthViewPager

    internal var mCycleViewPagerAdapter: CycleViewPager2Adapter? = null

    internal lateinit var dots: ArrayList<ImageView?>
//    List<String> titleName;

//    List<String> titleName;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view:View = inflater.inflate(R.layout.fragment_home_new, container, false)
        initViews(view)
        initData()

        return view
    }

    private fun initViews(parentView: View) {
        mDepthViewPager = parentView.findViewById<DepthViewPager>(R.id.id_depthViewPager)
        dotLayout = parentView.findViewById<View>(R.id.dot_layout) as LinearLayout
        tabBtn0 = parentView.findViewById<View>(R.id.tab_btn0) as ImageButton
        btn_layout = parentView.findViewById<View>(R.id.btn_layout) as LinearLayout
        tabLeft = parentView.findViewById<View>(R.id.tab_left) as ImageButton
        tabRight = parentView.findViewById<View>(R.id.tab_right) as ImageButton
        btn_layout!!.setOnClickListener(this)
        tabBtn0!!.setOnClickListener(this)
        tabLeft!!.setOnClickListener(this)
        tabRight!!.setOnClickListener(this)
    }

    private fun addBtnWithoutTextByPosition(position: Int, title: String) {
        val tabBtn = StrokeTextView(btn_layout!!.context)
        val lp = LayoutParams(activity!!.resources.getDimension(R.dimen.px134).toInt(), activity!!.resources.getDimension(R.dimen.px81).toInt())
        lp.topMargin = activity!!.resources.getDimension(R.dimen.px50).toInt()
        tabBtn.setLayoutParams(lp)
        tabBtn.setPadding(activity!!.resources.getDimension(R.dimen.px10).toInt(), 0, activity!!.resources.getDimension(R.dimen.px10).toInt(), 0)
        if (title.length >= 5) {
            tabBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity!!.resources.getDimension(R.dimen.px24))
        } else {
            tabBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity!!.resources.getDimension(R.dimen.px26))
        }
        tabBtn.setLineSpacing(0f, 0.8f)
        tabBtn.setGravity(Gravity.CENTER)
        tabBtn.setBackgroundResource(R.drawable.home_new_btn)
        tabBtn.setText(title)
        tabBtn.setSingleLine(false)
        tabBtn.setOnClickListener(OnClickListener {
            //学习新时代

            tabBtn0!!.visibility = View.VISIBLE
            btn_layout!!.visibility = View.GONE
            mDepthViewPager!!.scrollToItemByPosition(position + 1)
        })
        btn_layout!!.addView(tabBtn)
    }

    private fun addButton() {
        val tabBtn9 = ImageButton(btn_layout!!.context)
        val lp = LayoutParams(activity!!.resources.getDimension(R.dimen.px37).toInt(), activity!!.resources.getDimension(R.dimen.px33).toInt())
        lp.topMargin = activity!!.resources.getDimension(R.dimen.px30).toInt()
        lp.bottomMargin = activity!!.resources.getDimension(R.dimen.px20).toInt()
        tabBtn9.layoutParams = lp
        tabBtn9.setPadding(activity!!.resources.getDimension(R.dimen.px10).toInt(), 0, activity!!.resources.getDimension(R.dimen.px10).toInt(), 0)
        tabBtn9.setImageResource(R.drawable.home_btn_back)
        tabBtn9.background = null
        tabBtn9.scaleType = ScaleType.CENTER_INSIDE
        tabBtn9.setOnClickListener {
            tabBtn0!!.visibility = View.VISIBLE
            btn_layout!!.visibility = View.GONE
        }
        btn_layout!!.addView(tabBtn9)
    }

    private fun setDot(position: Int) {
        for (i in dots!!.indices) {
            if (i == position) {
                dots!![i]!!.setImageResource(R.drawable.home_dot_select)
            } else {
                dots!![i]!!.setImageResource(R.drawable.home_dot_unselect)
            }
        }
    }

    private fun initData() {
        val deviceId: String = SharePreHelper.getDeviceId()
        val url: String? = MessageFormat.format("{0}/moduleList/android?deviceId={1}", Content.WEB_PATH, deviceId)
        val request: Request<JSONObject?>? = NoHttp.createJsonObjectRequest(url, RequestMethod.GET)
        CallServerNohttp.getInstance().add(0, request, object : OnResponseListener<JSONObject?> {

            override fun onStart(what: Int) {}
            override fun onSucceed(what: Int, response: Response<JSONObject?>?) {
                if (activity == null) {
                    return
                }
                val jsonObject: JSONObject ?= response?.get()
                if(jsonObject == null){
                    return
                }


                if (jsonObject.has("code") && jsonObject.optInt("code") == 0 && jsonObject.has("data")) {
                    val data: JSONObject? = jsonObject.optJSONObject("data")
                    if (data != null) {
                        bindData(data)
                    }

                }
            }

            override fun onFailed(what: Int, response: Response<JSONObject?>?) {
                if (activity == null) {
                    return
                }
            }

            override fun onFinish(what: Int) {}
        })
    }

    internal lateinit var homeModuleBeanList: ArrayList<HomeModuleBean>

    //海报信息
    private fun bindData(data: JSONObject) {
        val moduleList: JSONArray? = data.optJSONArray("moduleList")
        if (moduleList == null || moduleList.length() < 1) {
            return
        }
        homeModuleBeanList = arrayListOf()
        for (i in 0 until moduleList.length()) {
            val mHomeModuleBean1 = HomeModuleBean()
            mHomeModuleBean1.type = moduleList.optJSONObject(i).optString("type")
            mHomeModuleBean1.title = moduleList.optJSONObject(i).optString("navigationTitle")
            mHomeModuleBean1.imageUrl = moduleList.optJSONObject(i).optString("posterImageLink")
            mHomeModuleBean1.jumpUrl = moduleList.optJSONObject(i).optString("h5Link")
            mHomeModuleBean1.click = moduleList.optJSONObject(i).optBoolean("click")
            homeModuleBeanList.add(mHomeModuleBean1)
        }
        pagerNum = homeModuleBeanList.size
        btn_layout!!.removeAllViews()
        for (i in 0 until pagerNum) {
            addBtnWithoutTextByPosition(i, homeModuleBeanList.get(i).title)
        }
        addButton()
        dots = ArrayList()
        for (i in 0 until pagerNum) {
            val imageView = ImageView(activity)
            imageView.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.setPadding(10, 10, 10, 10)
            if (i == 0) {
                imageView.setImageResource(R.drawable.home_dot_select)
            } else {
                imageView.setImageResource(R.drawable.home_dot_unselect)
            }
            dotLayout!!.addView(imageView)
            dots.add(imageView)
        }
        mCycleViewPagerAdapter = CycleViewPager2Adapter(fragmentManager, activity, null, homeModuleBeanList)
        mDepthViewPager.adapter = mCycleViewPagerAdapter
        mDepthViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            var position0 = 0
            var position = 0
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position0: Int) {
                this.position0 = position0
                position = position0
                if (position == 0) {
                    position = pagerNum - 1
                } else if (position == pagerNum + 1) {
                    position = 0
                } else {
                    position -= 1
                }
                setDot(position)
                if (mCycleViewPagerAdapter!!.getItem(position0) is PicFragment) {
                    (mCycleViewPagerAdapter!!.getItem(position0) as PicFragment).show()
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == 0) {
                    if (position0 == mDepthViewPager!!.adapter!!.count - 1) {
                        mDepthViewPager!!.setCurrentItem(1, false)
                    }
                    if (position0 == 0) {
                        mDepthViewPager!!.setCurrentItem(mDepthViewPager!!.adapter!!.count - 2, false)
                    }
                }
            }
        })
        setDot(0)
        mDepthViewPager!!.setCurrentItem(0, false)
        mDepthViewPager!!.setCurrentItem(1, false)
    }

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.tab_btn0 -> {
                tabBtn0!!.visibility = View.GONE
                btn_layout!!.visibility = View.VISIBLE
            }
            R.id.tab_right -> {
                tabBtn0!!.visibility = View.VISIBLE
                btn_layout!!.visibility = View.GONE
                mDepthViewPager!!.scrollToNext()
            }
            R.id.tab_left -> {
                tabBtn0!!.visibility = View.VISIBLE
                btn_layout!!.visibility = View.GONE
                mDepthViewPager!!.scrollToPrevious()
            }
        }
    }

    fun onClick(v: View?, type: String?, url: String?) {
        tabBtn0!!.visibility = View.VISIBLE
        btn_layout!!.visibility = View.GONE
        when (type) {
            "0" -> {//学习新时代
                Toast.makeText(activity,"学习新时代",Toast.LENGTH_SHORT).show()
            }
            "1" -> {//add Item
                Toast.makeText(activity,"学习新时代",Toast.LENGTH_SHORT).show()
            }
            "2" -> {
                Toast.makeText(activity,"学习新时代",Toast.LENGTH_SHORT).show()
            }
            "3" -> {//人民法院报
                Toast.makeText(activity,"人民法院报",Toast.LENGTH_SHORT).show()
            }
            "4" -> {
            }
            "5" -> {//人民司法
                Toast.makeText(activity,"人民司法",Toast.LENGTH_SHORT).show()
            }
            "6" -> {
            }
            else -> {
                Toast.makeText(activity,"其它",Toast.LENGTH_SHORT).show()

            }
        }
    }

    interface OnClickListener {
        fun onClick(v: View, type: java.lang.String, url: String)
    }

}
