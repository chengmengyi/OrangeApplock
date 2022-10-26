package com.demo.orangeapplock.server

import kotlinx.coroutines.*
import java.lang.Exception

object ConnectTimeManager {
    var t=0L
    private var job:Job?=null
    private val list= arrayListOf<IConnectTimeListener>()

    fun startTime(){
        if (null!= job) return
        job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                list.forEach {
                    it.connectTime(timeChange(t))
                }
                t++
                delay(1000L)
            }
        }
    }

    fun endTime(){
        job?.cancel()
        job=null
    }

    fun getCurrentTimeStr()=timeChange(t)

    fun addListener(listener:IConnectTimeListener){
        list.add(listener)
    }

    fun removeListener(listener:IConnectTimeListener){
        list.remove(listener)
    }

    interface IConnectTimeListener{
        fun connectTime(t:String)
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
}