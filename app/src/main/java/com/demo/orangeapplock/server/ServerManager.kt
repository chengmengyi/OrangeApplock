package com.demo.orangeapplock.server

import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.local.LocalManager
import com.demo.orangeapplock.online.FireManager
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

object ServerManager {

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
}