package com.orangeapplock.pro.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.orangeapplock.pro.ui.server.ServerHomeUI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ActivityCallback {
    var appFront=true
    var loadHomeAd=true
    private var reload=false
    private var job: Job?=null


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
                    if (ActivityUtils.isActivityExistsInStack(ServerHomeUI::class.java)){
                        activity.startActivity(Intent(activity, ServerHomeUI::class.java))
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
//                    ActivityUtils.finishActivity(Main1011Page::class.java)
//                    ActivityUtils.finishActivity(AdActivity::class.java)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }
}