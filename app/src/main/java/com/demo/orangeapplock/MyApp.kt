package com.demo.orangeapplock

import android.app.Application
import com.demo.orangeapplock.admob.AdShowClickManager
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.demo.orangeapplock.util.AppLockPwdManager
import com.demo.orangeapplock.util.AppListManager
import com.demo.orangeapplock.util.processName
import com.github.shadowsocks.Core
import com.tencent.mmkv.MMKV

lateinit var appLockApp:Application
class MyApp:Application(){
    override fun onCreate() {
        super.onCreate()
        appLockApp=this
        Core.init(this,ServerHomeUI::class)
        MMKV.initialize(this)
        if (!packageName.equals(processName(this))){
            return
        }
        AdShowClickManager.readLocalShowClickNum()
        AppLockPwdManager.initPwd()
        AppListManager.getInstallAppList(this)
        FireManager.readOnlineConfig()

    }
}