package com.orangeapplock.pro

import android.app.Application
import com.github.shadowsocks.Core
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.orangeapplock.pro.admob.AdShowClickManager
import com.orangeapplock.pro.online.FireManager
import com.orangeapplock.pro.ui.server.ServerHomeUI
import com.orangeapplock.pro.util.AppLockPwdManager
import com.orangeapplock.pro.util.AppListManager
import com.orangeapplock.pro.util.processName
import com.tencent.mmkv.MMKV

lateinit var appLockApp:Application
class MyApp:Application(){
    override fun onCreate() {
        super.onCreate()
        appLockApp=this
        Core.init(this, ServerHomeUI::class)
        MMKV.initialize(this)
        Firebase.initialize(this)
        if (!packageName.equals(processName(this))){
            return
        }
        AdShowClickManager.readLocalShowClickNum()
        AppLockPwdManager.initPwd()
        AppListManager.getInstallAppList(this)
        FireManager.readOnlineConfig()

    }
}