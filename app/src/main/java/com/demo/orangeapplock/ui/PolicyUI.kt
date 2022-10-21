package com.demo.orangeapplock.ui

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.local.LocalManager
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