package com.demo.orangeapplock.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.collection.arraySetOf
import com.demo.orangeapplock.bean.AppInfo
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object AppListManager {
    val installList= arrayListOf<AppInfo>()
    val lockedList= arrayListOf<AppInfo>()
    private val lockedAppPackageList= arraySetOf<String>()

    fun getInstallAppList(ctx: Context){
//        getLockedAppList()
//        GlobalScope.launch {
//            var bean: AppInfo? = null
//            val packageManager: PackageManager = ctx.packageManager
//            val list = packageManager.getInstalledPackages(0)
//            installList.clear()
//            for (p in list) {
//                bean = AppInfo()
//                bean.icon = p.applicationInfo.loadIcon(packageManager)
//                bean.label=packageManager.getApplicationLabel(p.applicationInfo).toString()
//                bean.packageName=p.applicationInfo.packageName
//                bean.lock=lockedAppPackageList.contains(bean.packageName)
//                val flags = p.applicationInfo.flags
//                if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {
//
//                } else {
//                    installList.add(bean)
//                    if (bean.lock){
//                        lockedList.add(bean)
//                    }
//                }
//            }
//        }

        getLockedAppList()
        var queryIntentActivities = mutableListOf<ResolveInfo>()
        val packageName = ctx.packageName
        GlobalScope.launch {
            val packageManager: PackageManager = ctx.packageManager
            val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            queryIntentActivities = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            } else {
                packageManager.queryIntentActivities(intent, 0)
            }

            lockedList.clear()
            installList.clear()
            queryIntentActivities.forEach {
                val appPackageName = it.activityInfo.applicationInfo.packageName
                val bean = AppInfo(
                    icon = it.loadIcon(packageManager),
                    label = it.loadLabel(packageManager).toString(),
                    packageName = appPackageName,
                    lock = lockedAppPackageList.contains(appPackageName)
                )
                if (bean.packageName!=packageName){
                    installList.add(bean)
                    if (bean.lock){
                        lockedList.add(bean)
                    }
                }

            }
        }
    }

    fun addLockApp(appInfo: AppInfo){
        lockedAppPackageList.add(appInfo.packageName)
        lockedList.add(appInfo)
        updateLocalLockedApp()
    }

    fun deleteLockApp(appInfo: AppInfo){
        lockedAppPackageList.remove(appInfo.packageName)
        lockedList.remove(appInfo)
        updateLocalLockedApp()
    }

    fun removeLockedList(){
        lockedAppPackageList.clear()
        lockedList.clear()
        installList.forEach {
            it.lock=false
        }
        updateLocalLockedApp()
    }

    private fun updateLocalLockedApp(){
        MMKV.defaultMMKV().encode("locked",lockedAppPackageList)
    }

    private fun getLockedAppList(){
        val decodeStringSet = MMKV.defaultMMKV().decodeStringSet("locked")
        if (null!=decodeStringSet){
            lockedAppPackageList.addAll(decodeStringSet)
        }
    }

    fun checkAppInLocked(name:String)=lockedAppPackageList.contains(name)
}