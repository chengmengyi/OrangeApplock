package com.demo.orangeapplock.ui.server

import android.Manifest
import android.content.Intent
import android.net.VpnService
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.util.checkNetworkStatus
import com.demo.orangeapplock.util.showNoNetworkDialog
import com.demo.orangeapplock.util.showToast
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.utils.StartService
import kotlinx.android.synthetic.main.server_home_layout.*

class ServerHomeUI:BaseUI(),ShadowsocksConnection.Callback {
    private var hasPermission=false
    private var state = BaseService.State.Idle
    private val sc= ShadowsocksConnection(true)
    private var serverInfoBean=ServerInfoBean()

    private val launch=registerForActivityResult(StartService()) {
        if (!it && hasPermission) {
            hasPermission = false
            connectServer()
        } else {
            state = BaseService.State.Idle
            showToast("Connected fail")
        }
    }

    override fun layoutId(): Int = R.layout.activity_server_home

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        sc.connect(this,this)
        setOnClickListener()
    }

    private fun setOnClickListener(){
        iv_country.setOnClickListener {
            startActivityForResult(Intent(this,ServerListUI::class.java),1014)
        }
        tv_connect_status.setOnClickListener {
            doLogic()
        }
    }

    private fun doLogic(){
        if(state==BaseService.State.Connecting||state!=BaseService.State.Stopping){
            return
        }
        if (state==BaseService.State.Connected){

        }else{
            setServerInfoUI()
            if (checkNetworkStatus()==1){
                showNoNetworkDialog()
                state = BaseService.State.Idle
                return
            }
            if (VpnService.prepare(this) != null) {
                hasPermission= true
                launch.launch(null)
                return
            }
            connectServer()
        }
    }

    private fun connectServer(){

    }

    private fun setServerInfoUI(){

    }

    private fun setConnectText(){

    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {

    }

    override fun onServiceConnected(service: IShadowsocksService) {

    }
}