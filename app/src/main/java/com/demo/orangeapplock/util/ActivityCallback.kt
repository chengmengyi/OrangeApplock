package com.demo.orangeapplock.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.demo.orangeapplock.admob.AdShowClickManager
import com.demo.orangeapplock.ui.HomeUI
import com.demo.orangeapplock.ui.MainActivity
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ActivityCallback {
    var appFront=true
    var loadHomeAd=true
    private var reload=false
    private var job: Job?=null
    //0冷启动1热启动2启动后
    var loadType=0
    var topIsHome=true


    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(callback)
    }

    private val callback=object : Application.ActivityLifecycleCallbacks{
        private var pages=0
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            pages++
            job?.cancel()
            job=null
            if (pages==1){
                appFront=true
                if (reload){
                    loadType=1
                    AdShowClickManager.readLocalShowClickNum()
                    if (ActivityUtils.isActivityExistsInStack(HomeUI::class.java)){
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                    }
                }
                reload=false
            }
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            pages--
            if (pages<=0){
                appFront=false
                loadHomeAd=true
                job= GlobalScope.launch {
                    delay(3000L)
                    reload=true
                    try {
                        topIsHome=ActivityUtils.getTopActivity().javaClass.name==HomeUI::class.java.name
                    }catch (e:Exception){

                    }
                    ActivityUtils.finishActivity(MainActivity::class.java)
                    ActivityUtils.finishActivity(AdActivity::class.java)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }
}