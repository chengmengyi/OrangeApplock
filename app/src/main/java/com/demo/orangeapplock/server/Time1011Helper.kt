package com.demo.orangeapplock.server

import kotlinx.coroutines.*
import java.lang.Exception

object Time1011Helper {
    private var time=0L
    private var time1011Job:Job?=null
    private var iTime: ITime?=null

    fun setTime(){
        time =0L
    }

    fun start1011Time(){
        if (null!= time1011Job) return
        time1011Job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                iTime?.connectTime(timeChange(time))
                time++
                delay(1000L)
            }
        }
    }


    private fun timeChange(t:Long):String{
        try {
            val shi=t/3600
            val fen= (t % 3600) / 60
            val miao= (t % 3600) % 60
            val s=if (shi<10) "0${shi}" else shi
            val f=if (fen<10) "0${fen}" else fen
            val m=if (miao<10) "0${miao}" else miao
            return "${s}:${f}:${m}"
        }catch (e: Exception){}
        return "00:00:00"
    }

    fun stop1011Time(){
        time1011Job?.cancel()
        time1011Job =null
    }

    fun setTimeListener(iTime: ITime?){
        Time1011Helper.iTime =iTime
    }

    interface ITime{
        fun connectTime(time:String)
    }
}