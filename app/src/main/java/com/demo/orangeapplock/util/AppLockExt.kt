package com.demo.orangeapplock.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


fun processName(applicationContext: Application): String {
    val pid = android.os.Process.myPid()
    var processName = ""
    val manager = applicationContext.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
    for (process in manager.runningAppProcesses) {
        if (process.pid === pid) {
            processName = process.processName
        }
    }
    return processName
}

fun Context.checkNetworkStatus(): Int {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
            return 2
        } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return 0
        }
    } else {
        return 1
    }
    return 1
}

fun Context.showNoNetworkDialog(){
    AlertDialog.Builder(this).apply {
        setMessage("You are not currently connected to the network")
        setPositiveButton("sure", null)
        show()
    }
}

fun Context.showToast(s: String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}
