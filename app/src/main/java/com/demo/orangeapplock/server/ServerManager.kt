package com.demo.orangeapplock.server

import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.local.LocalManager
import com.demo.orangeapplock.online.FireManager
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.database.ProfileManager

object ServerManager {
    var serverInfoBean=ServerInfoBean()
    var state = BaseService.State.Stopped

    fun getServerList()=FireManager.appLockServerList.ifEmpty { LocalManager.appLockLocalServerList }

    fun getFastServerInfo():ServerInfoBean{
        val serverList = getServerList()
        if (FireManager.appLockCityList.isNullOrEmpty()){
            return serverList.random()
        }else{
            val filter = serverList.filter { FireManager.appLockCityList.contains(it.bandar) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }

    fun isSmartServer(serverInfoBean: ServerInfoBean)=serverInfoBean.negara=="Smart Servers"&&serverInfoBean.ip.isEmpty()

    fun createOrUpdateProfile(list:ArrayList<ServerInfoBean>){
        for (serverInfoBean in list) {
            serverInfoBean.createProfile()
        }
    }

    fun getServerId(serverInfoBean: ServerInfoBean):Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==serverInfoBean.ip&&it.remotePort==serverInfoBean.port){
                return it.id
            }
        }
        return 0L
    }
}