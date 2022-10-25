package com.demo.orangeapplock.tba

import android.content.Context
import android.os.Build
import android.util.Log
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import org.json.JSONObject


object OkgoManager {
    const val url="https://pravda.orangeapplock.com/suckle/shaman/gamma"
//    const val url="http://chromiumquadrant-437480215.us-east-1.elb.amazonaws.com/chromium/quadrant"
    //https://pravda.orangeapplock.com/suckle/shaman/gamma

    fun requestGet(url:String,result:(json:String)-> Unit){
        OkGo.get<String>(url).execute(object : StringCallback(){
            override fun onSuccess(response: Response<String>?) {
                result(response?.body().toString())
            }
        })
    }

    fun uploadInstall(jsonObject: JSONObject, install:Boolean,context: Context){
        val rever = BaseUtil.getrever(context)
        val path = "$url?maniacal=${BaseUtil.getmaniacal(context)}&elegy=${Build.VERSION.RELEASE}&linden=${System.currentTimeMillis()}&rever=$rever&doghouse=${BaseUtil.getdoghouse(context)}"
//        Log.e("qweraaa",jsonObject.toString())
        OkGo.post<String>(path)
            .retryCount(2)
            .headers("content-type","application/json")
            .headers("rever", rever)
            .headers("euphoric", BaseUtil.getEuphoric())
            .upJson(jsonObject)
            .execute(object :StringCallback(){
                override fun onSuccess(response: Response<String>?) {
                    if (install){
                        if (jsonObject.optString("ovulate").isEmpty()){
                            TbaUtil.saveNoReferrerTag()
                        }else{
                            TbaUtil.saveHasReferrerTag()
                        }
                    }
//                    Log.e("qweraaa","=onSuccess==${response?.code()}===${response?.message()}===${response?.body()}==")
                }
            })
    }
}