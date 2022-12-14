package com.orangeapplock.pro.tba

import android.content.Context
import android.os.Build
import android.webkit.WebSettings
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.orangeapplock.pro.appLockApp
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class TbaUtil {
    companion object{

        fun uploadTBA(context: Context){
            OkgoManager.requestGet("https://ip.seeip.org/geoip/") {
                GlobalScope.launch {
                    val ip = getIp(it)
                    val jsonObject = BaseUtil.assembleCommonJson(context, ip)
                    getInstallReferrerClient(context, jsonObject)
                    uploadSession(context,ip)
                }
            }
        }

        private fun uploadSession(context: Context,ip: String){
            val jsonObject = BaseUtil.assembleCommonJson(context, ip)
            jsonObject.put("slake","cheeky")
            OkgoManager.uploadInstall(jsonObject, false,context)
        }

        private fun getInstallReferrerClient(context: Context, jsonObject: JSONObject){
            if (!uploadHasReferrerTag() || !uploadNoReferrerTag()){
                val referrerClient = InstallReferrerClient.newBuilder(appLockApp).build()
                referrerClient.startConnection(object : InstallReferrerStateListener {
                    override fun onInstallReferrerSetupFinished(responseCode: Int) {
                        try {
                            referrerClient.endConnection()
                            when (responseCode) {
                                InstallReferrerClient.InstallReferrerResponse.OK -> {
                                    val response = referrerClient.installReferrer
                                    assembleInstallJson(context,response,jsonObject)
                                }
                                else->{
                                    assembleInstallJson(context,null,jsonObject)
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }
                    override fun onInstallReferrerServiceDisconnected() {
                    }
                })
            }
        }

        private fun assembleInstallJson(context: Context,response: ReferrerDetails?,json: JSONObject) {
            if (null==response&& uploadNoReferrerTag()){
                return
            }
            if (null!=response&& uploadHasReferrerTag()){
                return
            }
            val jsonObject=JSONObject()

            jsonObject.put("single", getsingle())
            if (null==response){
                jsonObject.put("fop","")
                jsonObject.put("teutonic","")
                jsonObject.put("hawaiian", 0)
                jsonObject.put("ore", 0)
                jsonObject.put("acquire", 0)
                jsonObject.put("collie", 0)
                jsonObject.put("tendon", false)
            }else{
                jsonObject.put("fop",response.installReferrer)
                jsonObject.put("teutonic",response.installVersion)
                jsonObject.put("hawaiian", response.referrerClickTimestampSeconds)
                jsonObject.put("ore", response.installBeginTimestampSeconds)
                jsonObject.put("acquire", response.referrerClickTimestampServerSeconds)
                jsonObject.put("collie", response.installBeginTimestampServerSeconds)
                jsonObject.put("tendon", response.googlePlayInstantParam)
            }

            jsonObject.put("swing", getDefaultUserAgent(context))
            jsonObject.put("basilar", "snafu")
            jsonObject.put("oslo", getFirstInstallTime(context))
            jsonObject.put("cotton", getLastUpdateTime(context))
            jsonObject.put("ar","schwab")
            jsonObject.put("dud","chelate")
            json.put("doubtful",jsonObject)
            OkgoManager.uploadInstall(json, true,context)
        }

        private fun getsingle():String = "build/${Build.VERSION.RELEASE}"

        private fun getDefaultUserAgent(context: Context) = WebSettings.getDefaultUserAgent(context)

        private fun getFirstInstallTime(context: Context):Long{
            try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                return packageInfo.firstInstallTime
            }catch (e:java.lang.Exception){

            }
            return System.currentTimeMillis()
        }

        private fun getLastUpdateTime(context: Context):Long{
            try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                return packageInfo.lastUpdateTime
            }catch (e:java.lang.Exception){

            }
            return System.currentTimeMillis()
        }

        fun saveNoReferrerTag(){
            MMKV.defaultMMKV().encode("noReferrer",1)
        }

        private fun uploadNoReferrerTag()=MMKV.defaultMMKV().decodeInt("noReferrer")==1

        fun saveHasReferrerTag(){
            MMKV.defaultMMKV().encode("hasReferrer",1)
        }

        private fun uploadHasReferrerTag()=MMKV.defaultMMKV().decodeInt("hasReferrer")==1

        private fun getIp(json:String):String{
            try {
                if (json.isNullOrEmpty()){
                    return ""
                }
                val jsonObject=JSONObject(json)
                return jsonObject.optString("ip")
            }catch (e:Exception){}
            return ""
        }
    }
}