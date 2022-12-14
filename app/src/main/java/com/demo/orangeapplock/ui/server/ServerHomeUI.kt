package com.demo.orangeapplock.ui.server

import android.animation.ValueAnimator
import android.content.Intent
import android.net.VpnService
import android.view.animation.LinearInterpolator
import com.demo.orangeapplock.R
import com.demo.orangeapplock.admob.AdType
import com.demo.orangeapplock.admob.LoadAdManager
import com.demo.orangeapplock.admob.ShowNativeAdManager
import com.demo.orangeapplock.admob.ShowOpenAdManager
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.server.ConnectTimeManager
import com.demo.orangeapplock.server.ServerManager
import com.demo.orangeapplock.util.*
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import com.github.shadowsocks.utils.StartService
import kotlinx.android.synthetic.main.server_home_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ServerHomeUI:BaseUI(),ShadowsocksConnection.Callback,
    ConnectTimeManager.IConnectTimeListener {
    private var hasPermission=false
    private var isConnect=false
    private var click=true
    private val sc= ShadowsocksConnection(true)
    private var progressAnimator:ValueAnimator?=null

    private val launch=registerForActivityResult(StartService()) {
        if (!it && hasPermission) {
            hasPermission = false
            setStateConnecting()
        } else {
            click=true
            showToast("Connected fail")
        }
    }

    private val showConnectAd by lazy { ShowOpenAdManager(AdType.CONNECT_AD,this){
        jumpResultUI()
    } }

    private val showServerHomeAd by lazy { ShowNativeAdManager(AdType.SERVER_HOME_AD,this) }

    override fun layoutId(): Int = R.layout.server_home_layout

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        sc.connect(this,this)
        ConnectTimeManager.addListener(this)
        setOnClickListener()
    }

    private fun setOnClickListener(){
        iv_country.setOnClickListener {
            if (click){
                startActivityForResult(Intent(this,ServerListUI::class.java),1014)
            }
        }
        tv_connect_status.setOnClickListener {
            if (click){
                click=false
                doLogic()
            }
        }
        back.setOnClickListener {
            if (click){
                onBackPressed()
            }
        }

        if (intent.getBooleanExtra("connect",false)){
            if (click){
                click=false
                doLogic(false)
            }
        }
    }

    private fun doLogic(setPoint:Boolean=true){
        LoadAdManager.checkCanLoad(AdType.CONNECT_AD)
        LoadAdManager.checkCanLoad(AdType.RESULT_AD)

        if (ServerManager.state==BaseService.State.Connected){
            setStateStopping()
        }else{
            SetPointManager.point("oa_v_link")
            if (setPoint){
                SetPointManager.point("oa_vpn_link")
            }
            setServerInfoUI()
            if (checkNetworkStatus()==1){
                showNoNetworkDialog()
                click=true
                return
            }
            if (VpnService.prepare(this) != null) {
                hasPermission= true
                launch.launch(null)
                return
            }
            SetPointManager.point("oa_v_jurisdiction")
            setStateConnecting()
        }
    }

    private fun setStateConnecting(){
        setConnectText("Connecting...")
        setProgressPercent(0)
        ServerManager.state= BaseService.State.Connecting
        startConnectServer()
        updateConnectProgress(true)
    }

    private fun setStateStopping(){
        setConnectText("Stopping...")
        ServerManager.state= BaseService.State.Stopping
        startDisconnectServer()
        setProgressPercent(100)
        updateConnectProgress(false)
    }

    private fun updateConnectProgress(connect:Boolean){
        isConnect=connect
        progressAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration=10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val p=it.animatedValue as Int
                connect_progress.progress = if (connect) p else 100-p
                val duation = (10 * (p / 100.0F)).toInt()
                if (duation in 2..9){
                    if (connectSuccess()){
                        showConnectAd.showOpenAd { to->
                            stopUpdateProgress()
                            connectServerFinish(toResult = to)
                        }
                    }
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
                SetPointManager.point("oa_v_succ_link")
                setConnectText("Disconnect")
                if (FireManager.oaProgram=="B"){
                    LoadAdManager.removeAll()
                }
            }else{
                setServerInfoUI()
                setConnectText("Connect",)
            }
            if (toResult){
                jumpResultUI()
            }
        }else{
            SetPointManager.point("oa_v_fail_clcik")
            setConnectText("Connect")
            showToast(if (isConnect) "Connect Fail" else "Disconnect Fail")
        }
        click=true
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
        ConnectTimeManager.t=0L
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
        iv_country.setImageResource(getServerIcon(ServerManager.serverInfoBean.negara))
    }

    private fun setConnectText(text:String){
        tv_connect_status.text=text
        if(text=="Connect"){
            tv_connect_time.text="00:00:00"
        }
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
            if (click){
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

    override fun onBinderDied() {
        sc.disconnect(this)
    }

    override fun onResume() {
        super.onResume()
        showServerHomeAd.checkHasAd()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1014){
            when(data?.getStringExtra("result")){
                "duankai"->{
                    doLogic()
                }
                "lianjie"->{
                    setServerInfoUI()
                    doLogic()
                }
            }
        }
    }

    override fun connectTime(t: String) {
        tv_connect_time.text=t
    }

    override fun onBackPressed() {
        if(click){
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showServerHomeAd.endCheck()
        stopUpdateProgress()
        onBinderDied()
        ConnectTimeManager.removeListener(this)
    }
}