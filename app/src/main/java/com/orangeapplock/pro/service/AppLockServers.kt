package com.orangeapplock.pro.service

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.text.TextUtils
import com.orangeapplock.pro.util.AppListManager
import kotlinx.coroutines.*

class AppLockServers: Service() {
    private var channelId="orange_app_lock"
    private var showPackageName=""
//    private val builder by lazy {
//        NotificationCompat.Builder(this, channelId)
//            .setWhen(System.currentTimeMillis())
//            .setContentTitle(getString(R.string.app_name))
//            .setCategory(NotificationCompat.CATEGORY_SERVICE)
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setChannelId(channelId)
//    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, "OrangeAppLock", IMPORTANCE_HIGH)
//            channel.enableLights(true)
//            channel.setShowBadge(true)
//            manager.createNotificationChannel(channel)
//            builder.setContentTitle(getString(R.string.app_name))
//            builder.setContentText("App lock is running")
//            builder.setSmallIcon(R.mipmap.ic_launcher_round)
//            builder.setOngoing(true)
//            builder.setOnlyAlertOnce(true)
//            startForeground(1, builder.build())
//        }
        EnterPwdOverlay.initOverlayView(this)
        check()
    }

    private fun check(){
        GlobalScope.launch{
            while (true){
                delay(200)
                val launcherTopApp = getLauncherTopApp(this@AppLockServers)
                withContext(Dispatchers.Main){
                    if (!launcherTopApp.isNullOrEmpty()){
                        if (launcherTopApp!=showPackageName){
                            if (AppListManager.checkAppInLocked(launcherTopApp)&& Settings.canDrawOverlays(this@AppLockServers)){
                                EnterPwdOverlay.showOverlay()
                            }else{
                                EnterPwdOverlay.hideOverlay()
                            }
                        }
                        showPackageName=launcherTopApp
                    }
                }
            }
        }
    }

    private fun getLauncherTopApp(context: Context): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val activityManager=context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appTasks = activityManager.getRunningTasks(1)
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks[0].topActivity!!.packageName
            }
        } else {
            //5.0以后需要用这方法
            val sUsageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 10000
            var result = ""
            val event = UsageEvents.Event()
            val usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime)
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.packageName
                }
            }
            if (!TextUtils.isEmpty(result)) {
                return result
            }
        }
        return ""
    }
}