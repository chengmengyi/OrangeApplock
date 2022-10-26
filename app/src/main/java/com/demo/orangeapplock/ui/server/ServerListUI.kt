package com.demo.orangeapplock.ui.server

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.orangeapplock.R
import com.demo.orangeapplock.adapter.ServerListAdapter
import com.demo.orangeapplock.admob.AdType
import com.demo.orangeapplock.admob.LoadAdManager
import com.demo.orangeapplock.admob.ShowOpenAdManager
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.server.ConnectHelper
import kotlinx.android.synthetic.main.activity_server_list.*

class ServerListUI:BaseUI(){
    private val showAd by lazy { ShowOpenAdManager(AdType.back_AD,this){finish()} }

    override fun layoutId(): Int = R.layout.activity_server_list

    override fun initView() {
        LoadAdManager.checkCanLoad(AdType.back_AD)
        immersionBar.statusBarView(top_view).init()
        rv_server_list.apply {
            layoutManager=LinearLayoutManager(this@ServerListUI)
            adapter=ServerListAdapter(this@ServerListUI){
                chooseServer(it)
            }
        }

        back.setOnClickListener { onBackPressed() }
    }

    private fun chooseServer(serverInfoBean: ServerInfoBean){
        val current = ConnectHelper.server1011Entity
        val connected = ConnectHelper.isConnected1011()
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
        ConnectHelper.server1011Entity=serverInfoBean
        setResult(1014,Intent().apply {
            putExtra("result",result)
        })
        finish()
    }

    override fun onBackPressed() {
        if(LoadAdManager.getAdByType(AdType.back_AD)!=null){
            showAd.showOpenAd {
                finish()
            }
        }else{
            finish()
        }
    }
}