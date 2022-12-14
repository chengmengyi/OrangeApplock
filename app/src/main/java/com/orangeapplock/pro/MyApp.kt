package com.orangeapplock.pro

import android.app.Application
import com.orangeapplock.pro.admob.AdShowClickManager
import com.orangeapplock.pro.online.FireManager
import com.orangeapplock.pro.tba.TbaUtil
import com.orangeapplock.pro.ui.server.ServerHomeUI
import com.orangeapplock.pro.util.ActivityCallback
import com.orangeapplock.pro.util.AppLockPwdManager
import com.orangeapplock.pro.util.AppListManager
import com.orangeapplock.pro.util.processName
import com.github.shadowsocks.Core
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
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
        Firebase.initialize(this)
        MobileAds.initialize(this)
        AdShowClickManager.readLocalShowClickNum()
        AppLockPwdManager.initPwd()
        AppListManager.getInstallAppList(this)
        FireManager.readOnlineConfig()
        ActivityCallback.register(this)
        TbaUtil.uploadTBA(this)
    }
}