package com.demo.orangeapplock.bean

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

class ServerInfoBean(
    val method:String="",
    val pwd:String="",
    val host:String="",
    val country:String="Smart Servers",
    val port:Int=0,
    val city:String=""
){
    fun isRandomServer() = host=="" &&country=="Smart Servers"

    fun getProfileId():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==host&&it.remotePort==port){
                return it.id
            }
        }
        return 0L
    }

    fun writeProfileId(){
        val profile = Profile(
            id = 0L,
            name = "$country - $city",
            host = host,
            remotePort = port,
            password = pwd,
            method = method
        )

        var id:Long?=null
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.remotePort==profile.remotePort&&it.host==profile.host){
                id=it.id
                return@forEach
            }
        }
        if (null==id){
            ProfileManager.createProfile(profile)
        }else{
            profile.id=id!!
            ProfileManager.updateProfile(profile)
        }
    }
}