package com.demo.orangeapplock.server

import com.demo.orangeapplock.base.BaseUI
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ConnectHelper :ShadowsocksConnection.Callback{
    private var base1011Page:BaseUI?=null
    private var baseServiceState = BaseService.State.Idle
    var server1011Entity= ServerInfoHelper.createRandomServer()
    var lastServer1011Entity= ServerInfoHelper.createRandomServer()
    private var iConnect1011Result: IConnect1011Result?=null
    private val sc= ShadowsocksConnection(true)

    fun create1011(base1011Page: BaseUI,iConnect1011Result: IConnect1011Result){
        ConnectHelper.base1011Page =base1011Page
        ConnectHelper.iConnect1011Result =iConnect1011Result
        sc.connect(base1011Page,this)
    }

    fun connect(){
        baseServiceState = BaseService.State.Connecting
        GlobalScope.launch(Dispatchers.IO) {
            if (server1011Entity.isRandomServer()){
                DataStore.profileId = ServerInfoHelper.getRandomServer().getProfileId()
            }else{
                DataStore.profileId = server1011Entity.getProfileId()
            }
            Core.startService()
        }
        Time1011Helper.setTime()
    }

    fun disconnect(){
        baseServiceState = BaseService.State.Stopping
        GlobalScope.launch {
            Core.stopService()
        }
    }

    fun isConnected1011()= baseServiceState ==BaseService.State.Connected

    fun isStopped1011()= baseServiceState ==BaseService.State.Stopped

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        baseServiceState =state
        if (isConnected1011()){
            lastServer1011Entity = server1011Entity
            Time1011Helper.start1011Time()
        }
        if (isStopped1011()){
            Time1011Helper.stop1011Time()
            iConnect1011Result?.disconnect1011Success()
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        baseServiceState =state
        if (isConnected1011()){
            Time1011Helper.start1011Time()
            lastServer1011Entity = server1011Entity
            iConnect1011Result?.connect1011Success()
        }
    }

    override fun onBinderDied() {
        base1011Page?.let {
            sc.disconnect(it)
        }
    }

    fun onDestroy(){
        onBinderDied()
        base1011Page =null
        iConnect1011Result =null
    }

    interface IConnect1011Result{
        fun connect1011Success()
        fun disconnect1011Success()
    }

}