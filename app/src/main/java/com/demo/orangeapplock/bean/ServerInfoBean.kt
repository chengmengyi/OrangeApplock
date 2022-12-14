package com.demo.orangeapplock.bean

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

class ServerInfoBean(
    val nombor_akaun:String="",
    val katalaluan:String="",
    val ip:String="",
    val negara:String="Smart Servers",
    val port:Int=0,
    val bandar:String=""
){
    
    fun createProfile(){
        val profile = Profile(
            id = 0L,
            name = "${negara} - ${bandar}",
            host = ip,
            remotePort = port,
            password = katalaluan,
            method = nombor_akaun
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