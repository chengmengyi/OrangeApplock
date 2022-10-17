package com.demo.orangeapplock.ui.server

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import kotlinx.android.synthetic.main.activity_result.*

class ResultUI:BaseUI() {
    private var success=false

    override fun layoutId(): Int = R.layout.activity_result

    override fun initView() {
        back.setOnClickListener { finish() }

        success=intent.getBooleanExtra("success",false)
        iv_result_bg.isSelected=success
        tv_result_connect_time.isSelected=success
        tv_title.text=if (success)"Connect success" else "Disconnected"
    }
}