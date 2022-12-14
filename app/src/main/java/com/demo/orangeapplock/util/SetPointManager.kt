package com.demo.orangeapplock.util

import android.os.Bundle
import android.util.Log
import com.demo.orangeapplock.online.FireManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object SetPointManager {
//    private val remoteConfig= Firebase.analytics

    fun point(name:String){
        printAppLock("set point:$name")
//        remoteConfig.logEvent(name, Bundle())
    }

    fun setUserProperty(){
        Log.e("qwer","=方案=${FireManager.oaProgram.toLowerCase()}===")
//        remoteConfig.setUserProperty("oa_user_type",FireManager.oaProgram.toLowerCase())
    }
}