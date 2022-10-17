package com.demo.orangeapplock.ui.server

import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.orangeapplock.R
import com.demo.orangeapplock.adapter.ServerListAdapter
import com.demo.orangeapplock.base.BaseUI
import kotlinx.android.synthetic.main.activity_server_list.*

class ServerListUI:BaseUI(){
    override fun layoutId(): Int = R.layout.activity_server_list

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        rv_server_list.apply {
            layoutManager=LinearLayoutManager(this@ServerListUI)
            adapter=ServerListAdapter(this@ServerListUI)
        }

        back.setOnClickListener { finish() }
    }
}