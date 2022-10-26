package com.demo.orangeapplock.ui.server

import com.demo.orangeapplock.R
import com.demo.orangeapplock.admob.AdType
import com.demo.orangeapplock.admob.ShowNativeAdManager
import com.demo.orangeapplock.base.BaseUI
import kotlinx.android.synthetic.main.activity_result.*

class ResultUI:BaseUI() {
    private var success=false
    private val showAd by lazy {  ShowNativeAdManager(AdType.RESULT_AD,this) }

    override fun layoutId(): Int = R.layout.activity_result

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        back.setOnClickListener { finish() }

        success=intent.getBooleanExtra("success",false)
        iv_result_bg.isSelected=success
        tv_result_connect_time.isSelected=success
        tv_title.text=if (success)"Connect success" else "Disconnected"
//        if (success){
//            ConnectTimeManager.addListener(this)
//        }else{
//            tv_result_connect_time.text=ConnectTimeManager.getCurrentTimeStr()
//        }
    }

//    override fun connectTime(t: String) {
//        tv_result_connect_time.text=t
//    }

    override fun onResume() {
        super.onResume()
        showAd.checkHasAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        showAd.endCheck()
//        if (success){
//            ConnectTimeManager.removeListener(this)
//        }
    }
}