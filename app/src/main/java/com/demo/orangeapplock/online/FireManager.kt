package com.demo.orangeapplock.online

import com.demo.orangeapplock.admob.AdShowClickManager
import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.local.LocalManager
import com.demo.orangeapplock.server.ServerManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV
import org.json.JSONObject

object FireManager {
    var oaType="1"
    var oaShow="0"
    var oaProgram=""
    val appLockCityList= arrayListOf<String>()
    val appLockServerList= arrayListOf<ServerInfoBean>()

    fun readOnlineConfig(){
        writeRao(LocalManager.appLockLocalRao)
        ServerManager.createOrUpdateProfile(LocalManager.appLockLocalServerList)

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful){
                readCityConfig(remoteConfig.getString("appLock_city"))
                readAdConfig(remoteConfig.getString("appLock_ad"))
                val appLock_ad_test = remoteConfig.getString("appLock_ad_test")
                if (appLock_ad_test.isNotEmpty()){
                    readAdConfig(appLock_ad_test)
                }
                readServerConfig(remoteConfig.getString("oa_pelayan"))
                val oa_type = remoteConfig.getString("oa_type")
                if(oa_type.isNotEmpty()){
                    oaType=oa_type
                }
                val oa_show = remoteConfig.getString("oa_show")
                if(oa_show.isNotEmpty()){
                    oaShow=oa_show
                }

                val oa_program = remoteConfig.getString("oa_program")
                if(oa_program.isNotEmpty()){
                    oaProgram=oa_program
                }
            }
        }
    }
    
    private fun readServerConfig(json:String){
        try {
            val jsonArray = JSONObject(json).getJSONArray("oa_pelayan")
            for (index in 0 until jsonArray.length()){
                val json0914Object = jsonArray.getJSONObject(index)
                val serverInfoBean = ServerInfoBean(
                    json0914Object.optString("nombor_akaun"),
                    json0914Object.optString("katalaluan"),
                    json0914Object.optString("ip"),
                    json0914Object.optString("negara"),
                    json0914Object.optInt("port"),
                    json0914Object.optString("bandar"),
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