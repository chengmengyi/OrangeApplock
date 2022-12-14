package com.orangeapplock.pro.bean

class ConfigAdBean(
    val id_applock:String,
    val sort_applock:Int,
    val type_applock:String,
    val source_applock:String,
) {
    override fun toString(): String {
        return "ConfigAdBean(id_applock='$id_applock', sort_applock=$sort_applock, type_applock='$type_applock')"
    }
}