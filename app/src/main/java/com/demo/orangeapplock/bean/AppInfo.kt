package com.demo.orangeapplock.bean

import android.graphics.drawable.Drawable

class AppInfo (
    var label:String = "",
    var packageName:String = "",
    var icon : Drawable? = null,
    var lock:Boolean=false
){
    override fun toString(): String {
        return "AppInfo(label='$label', packageName='$packageName', icon=$icon)"
    }
}