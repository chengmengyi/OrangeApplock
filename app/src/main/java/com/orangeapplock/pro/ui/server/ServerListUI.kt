package com.orangeapplock.pro.ui.server

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeapplock.pro.R
import com.orangeapplock.pro.adapter.ServerListAdapter
import com.orangeapplock.pro.base.BaseUI
import com.orangeapplock.pro.bean.ServerInfoBean
import com.orangeapplock.pro.server.ServerManager
import com.github.shadowsocks.bg.BaseService
import kotlinx.android.synthetic.main.activity_server_list.*

class ServerListUI:BaseUI(){
    override fun layoutId(): Int = R.layout.activity_server_list

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        rv_server_list.apply {
            layoutManager=LinearLayoutManager(this@ServerListUI)
            adapter=ServerListAdapter(this@ServerListUI){
                chooseServer(it)
            }
        }

        back.setOnClickListener { finish() }
    }

    private fun chooseServer(serverInfoBean: ServerInfoBean){
        val current = ServerManager.serverInfoBean
        val connected = ServerManager.state == BaseService.State.Connected
        if(connected&&current.host==serverInfoBean.host){
            AlertDialog.Builder(this).apply {
                setMessage("You are currently connected and need to disconnect before manually connecting to the server.")
                setPositiveButton("sure") { _, _ ->
                    finishUI(serverInfoBean,"duankai")
                }
                setNegativeButton("cancel",null)
                show()
            }
        }else{
            if (connected){
                finishUI(serverInfoBean,"")
            }else{
                finishUI(serverInfoBean,"lianjie")
            }
        }
    }
    private fun finishUI(serverInfoBean: ServerInfoBean,result:String){
        ServerManager.serverInfoBean=serverInfoBean
        setResult(1014,Intent().apply {
            putExtra("result",result)
        })
    }
}