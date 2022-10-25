package com.orangeapplock.pro.online

import com.orangeapplock.pro.admob.AdShowClickManager
import com.orangeapplock.pro.bean.ServerInfoBean
import com.orangeapplock.pro.local.LocalManager
import com.orangeapplock.pro.server.ServerManager
import com.tencent.mmkv.MMKV
import org.json.JSONObject

object FireManager {
    val appLockCityList= arrayListOf<String>()
    val appLockServerList= arrayListOf<ServerInfoBean>()

    fun readOnlineConfig(){
        writeRao(LocalManager.appLockLocalRao)
        ServerManager.createOrUpdateProfile(LocalManager.appLockLocalServerList)

//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                readCityConfig(remoteConfig.getString("appLock_city"))
//                readAdConfig(remoteConfig.getString("appLock_ad"))
//                readServerConfig(remoteConfig.getString("appLock_server"))
//                writeRao(remoteConfig.getString("appLock_rao"))
//            }
//        }
    }
    
    private fun readServerConfig(json:String){
        try {
            val jsonArray = JSONObject(json).getJSONArray("appLock_server")
            for (index in 0 until jsonArray.length()){
                val json0914Object = jsonArray.getJSONObject(index)
                val serverInfoBean = ServerInfoBean(
                    json0914Object.optString("method"),
                    json0914Object.optString("pwd"),
                    json0914Object.optString("host"),
                    json0914Object.optString("country"),
                    json0914Object.optInt("port"),
                    json0914Object.optString("city"),
                )
                appLockServerList.add(serverInfoBean)
            }
            ServerManager.createOrUpdateProfile(appLockServerList)
        }catch (e:Exception){

        }
    }

    private fun readCityConfig(json: String){
        try {
            val jsonArray = JSONObject(json).getJSONArray("appLock_city")
            for (index in 0 until jsonArray.length()){
                appLockCityList.add(jsonArray.optString(index))
            }
        }catch (e:Exception){

        }
    }

    private fun readAdConfig(json: String){
        try {
            MMKV.defaultMMKV().encode("appLock_ad",json)
            val jsonObject = JSONObject(json)
            AdShowClickManager.maxClickNum=jsonObject.optInt("click")
            AdShowClickManager.maxShowNum=jsonObject.optInt("show")
        }catch (e:Exception){

        }
    }

    fun getLocalAdStr():String{
        val appLockAd = MMKV.defaultMMKV().decodeString("appLock_ad")?:""
        return if (appLockAd.isEmpty()) LocalManager.appLockLocalAd else appLockAd
    }

    private fun writeRao(json: String){
        MMKV.mmkvWithID("appLock").encode("appLock_rao",json)
    }
}