package com.demo.orangeapplock.ui.server

import android.Manifest
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
import com.demo.orangeapplock.server.ConnectHelper
import com.demo.orangeapplock.server.Time1011Helper
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

class ServerHomeUI:BaseUI(), ConnectHelper.IConnect1011Result, Time1011Helper.ITime {
    private var hasPermission=false
    private var isConnect=false
    private var click=true
    private val sc= ShadowsocksConnection(true)
    private var progressAnimator:ValueAnimator?=null

    private val registerResult=registerForActivityResult(StartService()) {
        if (!it && hasPermission) {
            hasPermission = false
            ConnectHelper.connect()
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
        ConnectHelper.create1011(this,this)

        setOnClickListener()
    }

    private fun setOnClickListener(){
        iv_country.setOnClickListener {
            startActivityForResult(Intent(this,ServerListUI::class.java),1014)
        }
        tv_connect_status.setOnClickListener {
            if (click){
                click=false
                doLogic()
            }
        }
        back.setOnClickListener { finish() }
    }

    private fun doLogic(){
        LoadAdManager.checkCanLoad(AdType.CONNECT_AD)
        LoadAdManager.checkCanLoad(AdType.RESULT_AD)

        if (ConnectHelper.isConnected1011()){
            setStateStopping()
        }else{
            setServerInfoUI()
            if (checkNetworkStatus()==1){
                showNoNetworkDialog()
                click=true
                return
            }
            if (VpnService.prepare(this) != null) {
                hasPermission= true
                registerResult.launch(null)
                return
            }
            setStateConnecting()
        }
    }

    private fun setStateConnecting(){
        setConnectText("Connecting...")
        setProgressPercent(0)
//        ConnectHelper.connect()
        updateConnectProgress(true)
    }

    private fun setStateStopping(){
        setConnectText("Stopping...")
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
//                val progress = if (connect) p else 100-p
                connect_progress.progress = p
                val duation = (10 * (p / 100.0F)).toInt()
                if (duation==3){
                    printAppLock("kkk===connect did")
                    if (connect){
                        ConnectHelper.connect()
                    }else{
                        ConnectHelper.disconnect()
                    }
                }
//                if (duation in 2..9){
//                    if (connectSuccess()){
//                        showConnectAd.showOpenAd { to->
//                            stopUpdateProgress()
//                            connectServerFinish(toResult = to)
//                        }
//                    }
//                }
                //                else if (duation>=10){
//                    stopUpdateProgress()
//                    connectServerFinish()
//                }
            }
            start()
        }
    }

    private fun connectServerFinish(toResult:Boolean=true){
        setProgressPercent(0)
        if (connectSuccess()){
            if (isConnect){
                setConnectText("Disconnect")
            }else{
                setServerInfoUI()
                setConnectText("Connect", s = "if")
            }
            if (toResult){
                jumpResultUI()
            }
        }else{
            setConnectText("Connect", s = "else")
            showToast(if (isConnect) "Connect Fail" else "Disconnect Fail")
        }
        click=true
    }

    private fun connectSuccess()=if (isConnect) ConnectHelper.isConnected1011() else ConnectHelper.isStopped1011()

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
//        iv_country.setImageResource(getServerIcon(ServerManager.serverInfoBean.country))
    }

    private fun setConnectText(text:String,s:String=""){
        tv_connect_status.text=text
        if(text=="Connect"){
            tv_connect_time.text="00:00:00"
        }

        if(s.isNotEmpty()){
            printAppLock("kkkk=${s}=")
        }
    }

    private fun setProgressPercent(progress:Int){
        connect_progress.progress=progress
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

    override fun onDestroy() {
        super.onDestroy()
        showServerHomeAd.endCheck()
        stopUpdateProgress()
//        onBinderDied()
//        ConnectTimeManager.removeListener(this)
    }

    override fun connect1011Success() {


    }

    override fun disconnect1011Success() {

    }
}