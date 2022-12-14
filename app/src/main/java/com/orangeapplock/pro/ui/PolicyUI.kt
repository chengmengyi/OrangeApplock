package com.orangeapplock.pro.ui

import com.orangeapplock.pro.R
import com.orangeapplock.pro.base.BaseUI
import com.orangeapplock.pro.local.LocalManager
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyUI:BaseUI() {
    override fun layoutId(): Int = R.layout.activity_policy

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        back.setOnClickListener { finish() }

        webview.apply {
            settings.javaScriptEnabled=true
            loadUrl(LocalManager.appLockPolicy)
        }
    }
}