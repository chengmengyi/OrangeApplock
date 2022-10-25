package com.orangeapplock.pro.ui.server

import android.animation.ValueAnimator
import android.content.Intent
import android.net.VpnService
import android.view.animation.LinearInterpolator
import com.orangeapplock.pro.R
import com.orangeapplock.pro.base.BaseUI
import com.orangeapplock.pro.server.ConnectTimeManager
import com.orangeapplock.pro.server.ServerManager
import com.orangeapplock.pro.util.*
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import com.github.shadowsocks.utils.StartService
import kotlinx.android.synthetic.main.server_home_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ServerHomeUI:BaseUI(),ShadowsocksConnection.Callback {
    private var hasPermission=false
    private var isConnect=false
    private val sc= ShadowsocksConnection(true)
    private var progressAnimator:ValueAnimator?=null

    private val launch=registerForActivityResult(StartService()) {
        if (!it && hasPermission) {
            hasPermission = false
            setStateConnecting()
        } else {
            ServerManager.state = BaseService.State.Idle
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
        if(ServerManager.state==BaseService.State.Connecting||ServerManager.state==BaseService.State.Stopping){
            return
        }
        if (ServerManager.state==BaseService.State.Connected){
            setStateStopping()
        }else{
            setServerInfoUI()
            if (checkNetworkStatus()==1){
                showNoNetworkDialog()
                ServerManager.state = BaseService.State.Idle
                return
            }
            if (VpnService.prepare(this) != null) {
                hasPermission= true
                launch.launch(null)
                return
            }
            setStateConnecting()
        }
    }

    private fun setStateConnecting(){
        setConnectText("Connecting...")
        setProgressPercent(0)
        ServerManager.state= BaseService.State.Connecting
        updateConnectProgress(true)
    }

    private fun setStateStopping(){
        setConnectText("Stopping...")
        ServerManager.state= BaseService.State.Stopping
        setProgressPercent(100)
        updateConnectProgress(false)
    }

    private fun updateConnectProgress(connect:Boolean){
        isConnect=connect
        progressAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration=10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                connect_progress.progress = progress
                val duation = (10 * (progress / 100.0F)).toInt()
                if (duation==3){
                    if (connect){
                        startConnectServer()
                    }else{
                        startDisconnectServer()
                    }
                }
                if (duation in 2..9){

                }else if (duation>=10){
                    stopUpdateProgress()
                    connectServerFinish()
                }
            }
            start()
        }
    }

    private fun connectServerFinish(toResult:Boolean=true){
        setProgressPercent(0)
        if (connectSuccess()){
            if (isConnect){
                ServerManager.state=BaseService.State.Connected
                setConnectText("Disconnect")
            }else{
                ServerManager.state=BaseService.State.Stopped
                setServerInfoUI()
                setConnectText("Connect")
            }
            if (toResult){
                jumpResultUI()
            }
        }else{
            ServerManager.state=BaseService.State.Stopped
            setConnectText("Connect")
            showToast(if (isConnect) "Connect Fail" else "Disconnect Fail")
        }
    }

    private fun connectSuccess()=if (isConnect) ServerManager.state==BaseService.State.Connected else ServerManager.state==BaseService.State.Stopped

    private fun startConnectServer(){
        GlobalScope.launch {
            if (ServerManager.isSmartServer(ServerManager.serverInfoBean)){
                DataStore.profileId = ServerManager.getServerId(ServerManager.getFastServerInfo())
            }else{
                DataStore.profileId =ServerManager.getServerId(ServerManager.serverInfoBean)
            }
            Core.startService()
        }
        ConnectTimeManager.startTime()
    }

    private fun jumpResultUI(){
        if (ActivityCallback.appFront){
            startActivity(Intent(this,ResultUI::class.java).apply {
                putExtra("success",isConnect)
            })
        }
    }

    private fun startDisconnectServer(){
        GlobalScope.launch {
            Core.stopService()
        }
    }

    private fun stopUpdateProgress(){
        progressAnimator?.removeAllUpdateListeners()
        progressAnimator?.cancel()
        progressAnimator=null
    }

    private fun setServerInfoUI(){
        iv_country.setImageResource(getServerIcon(ServerManager.serverInfoBean.country))
    }

    private fun setConnectText(text:String){
        tv_connect_status.text=text
    }

    private fun setProgressPercent(progress:Int){
        connect_progress.progress=progress
    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        ServerManager.state=state
        if (state==BaseService.State.Connected){
            ConnectTimeManager.startTime()
        }
        if (state==BaseService.State.Stopped){
            ConnectTimeManager.endTime()
            if (state!=BaseService.State.Connecting&&state!=BaseService.State.Stopping){
                setConnectText("Connect")
            }
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        ServerManager.state=state
        if (state==BaseService.State.Connected){
            ConnectTimeManager.startTime()
            setConnectText("Disconnect")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1014){
            when(data?.getStringExtra("result")){
                "duankai"->{
                    setStateStopping()
                }
                "lianjie"->{
                    setServerInfoUI()
                    setStateConnecting()
                }
            }
        }
    }
}