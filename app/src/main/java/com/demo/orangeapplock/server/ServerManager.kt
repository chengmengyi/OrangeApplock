package com.demo.orangeapplock.server

import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.local.LocalManager
import com.demo.orangeapplock.online.FireManager
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.database.Profile
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
            val filter = serverList.filter { FireManager.appLockCityList.contains(it.city) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }

    fun isSmartServer(serverInfoBean: ServerInfoBean)=serverInfoBean.country=="Smart Servers"&&serverInfoBean.host.isEmpty()

    fun createOrUpdateProfile(list:ArrayList<ServerInfoBean>){
        for (serverInfoBean in list) {
            val profile = Profile(
                id = 0L,
                name = "${serverInfoBean.country} - ${serverInfoBean.city}",
                host = serverInfoBean.host,
                remotePort = serverInfoBean.port,
                password = serverInfoBean.pwd,
                method = serverInfoBean.method
            )

            var id: Long? = null
            ProfileManager.getActiveProfiles()?.forEach {
                if (it.remotePort == profile.remotePort && it.host == profile.host) {
                    id = it.id
                    return@forEach
                }
            }
            if (null == id) {
                ProfileManager.createProfile(profile)
            } else {
                profile.id = id!!
                ProfileManager.updateProfile(profile)
            }
        }
    }

    fun getServerId(serverInfoBean: ServerInfoBean):Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==serverInfoBean.host&&it.remotePort==serverInfoBean.port){
                return it.id
            }
        }
        return 0L
    }
}