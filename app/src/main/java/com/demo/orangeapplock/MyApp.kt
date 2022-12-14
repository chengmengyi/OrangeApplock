package com.demo.orangeapplock

import android.app.Application
import com.demo.orangeapplock.admob.AdShowClickManager
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.tba.TbaUtil
import com.demo.orangeapplock.ui.HomeUI
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.demo.orangeapplock.util.ActivityCallback
import com.demo.orangeapplock.util.AppLockPwdManager
import com.demo.orangeapplock.util.AppListManager
import com.demo.orangeapplock.util.processName
import com.github.shadowsocks.Core
import com.lzy.okgo.OkGo
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


lateinit var appLockApp:Application
class MyApp:Application(){
    override fun onCreate() {
        super.onCreate()
        appLockApp=this
        Core.init(this,HomeUI::class)
        MMKV.initialize(this)
        if (!packageName.equals(processName(this))){
            return
        }
//        initOkGo()
        AdShowClickManager.readLocalShowClickNum()
        AppLockPwdManager.initPwd()
        AppListManager.getInstallAppList(this)
        FireManager.readOnlineConfig()
        ActivityCallback.register(this)
        TbaUtil.uploadTBA(this)
    }

    private fun initOkGo(){
        val builder = OkHttpClient.Builder()
        builder.readTimeout(10000, TimeUnit.MILLISECONDS)
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS)
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS)
        OkGo.getInstance().init(this).okHttpClient = builder.build()
    }
}