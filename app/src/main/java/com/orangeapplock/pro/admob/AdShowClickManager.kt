package com.orangeapplock.pro.admob

import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

object AdShowClickManager {
    var maxClickNum=15
    var maxShowNum=50

    var currentClickNum=0
    var currentShowNum=0

    var openShowing=false
    var lockAdShowNum=-1

    fun addLockShowNum(){
        lockAdShowNum++
    }

    fun readLocalShowClickNum(){
        currentClickNum=MMKV.defaultMMKV().decodeInt(createKey("click"),0)
        currentShowNum=MMKV.defaultMMKV().decodeInt(createKey("show"),0)
    }

    fun writeClickNum(){
        currentClickNum++
        MMKV.defaultMMKV().encode(createKey("click"), currentClickNum)
    }

    fun writeShowNum(){
        currentShowNum++
        MMKV.defaultMMKV().encode(createKey("show"), currentShowNum)
    }

    fun checkLimit()=currentShowNum>= maxShowNum|| currentClickNum>= maxClickNum

    private fun createKey(key:String)="num_${SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))}_$key"
}