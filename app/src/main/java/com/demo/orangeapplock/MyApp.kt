package com.demo.orangeapplock

import android.app.Application
import com.demo.orangeapplock.admob.AdShowClickManager
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.demo.orangeapplock.util.processName
import com.github.shadowsocks.Core
import com.tencent.mmkv.MMKV

class MyApp:Application(){
    override fun onCreate() {
        super.onCreate()
        Core.init(this,ServerHomeUI::class)
        MMKV.initialize(this)
        if (!packageName.equals(processName(this))){
            return
        }
        AdShowClickManager.readLocalShowClickNum()
        FireManager.readOnlineConfig()

    }
}