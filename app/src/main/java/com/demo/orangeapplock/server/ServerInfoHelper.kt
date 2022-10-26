package com.demo.orangeapplock.server

import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.local.LocalManager
import com.demo.orangeapplock.online.FireManager


object ServerInfoHelper {
    fun get1011ServerList()=FireManager.appLockServerList.ifEmpty { LocalManager.appLockLocalServerList }

    fun createRandomServer()=ServerInfoBean(country = "Smart Servers")

    fun getRandomServer():ServerInfoBean{
        val serverList = get1011ServerList()
        if (!FireManager.appLockCityList.isNullOrEmpty()){
            val filter = serverList.filter { FireManager.appLockCityList.contains(it.city) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }

}