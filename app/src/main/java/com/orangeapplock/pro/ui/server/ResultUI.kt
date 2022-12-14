package com.orangeapplock.pro.ui.server

import com.orangeapplock.pro.R
import com.orangeapplock.pro.base.BaseUI
import kotlinx.android.synthetic.main.activity_result.*

class ResultUI:BaseUI() {
    private var success=false

    override fun layoutId(): Int = R.layout.activity_result

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        back.setOnClickListener { finish() }

        success=intent.getBooleanExtra("success",false)
        iv_result_bg.isSelected=success
        tv_result_connect_time.isSelected=success
        tv_title.text=if (success)"Connect success" else "Disconnected"
    }
}