package com.orangeapplock.pro.tba

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import java.security.NoSuchAlgorithmException


class BaseUtil {
    companion object{

        fun assembleCommonJson(context: Context,ip:String):JSONObject{
            val jsonObject = JSONObject()
            jsonObject.put("cyclist", assembleCyclistJson(context,ip))
            jsonObject.put("domenico", assembleDomenicoJson(context))
            jsonObject.put("sunlit", assembleSunlitJson(context))
            jsonObject.put("flyer", assembleFlyerJson(context))
            return jsonObject
        }


        private fun assembleCyclistJson(context: Context,ip:String):JSONObject{
            val jsonObject = JSONObject()
            jsonObject.put("euphoric", getEuphoric())
            jsonObject.put("flail", getFlail(context))
            jsonObject.put("umbra", getUmbra(context))
            jsonObject.put("overture", ip)
            jsonObject.put("nocturne", getNocturne())
            jsonObject.put("burt", "fourteen")
            jsonObject.put("anion", getAnion(context))
            jsonObject.put("jury", getJury())
            return jsonObject
        }

        private fun assembleDomenicoJson(context: Context):JSONObject{
            val jsonObject = JSONObject()
            jsonObject.put("amigo", getamigo())
            jsonObject.put("doghouse", getdoghouse(context))
            return jsonObject
        }

        private fun assembleSunlitJson(context: Context):JSONObject{
            val jsonObject = JSONObject()
            jsonObject.put("egret", getegret())
            jsonObject.put("inborn", getinborn())
            jsonObject.put("maniacal", getmaniacal(context))
            jsonObject.put("linden", System.currentTimeMillis())
            jsonObject.put("carrara", getcarrara())
            jsonObject.put("bask", 50)
            jsonObject.put("sea", getEuphoric())
            jsonObject.put("harden", getharden())
            return jsonObject
        }

        private fun assembleFlyerJson(context: Context):JSONObject{
            val jsonObject = JSONObject()
            jsonObject.put("hubby", gethubby(context))
            jsonObject.put("rever", getrever(context))
            jsonObject.put("hodges", gethodges(context))
            jsonObject.put("elegy", getelegy())
            return jsonObject
        }

        fun getEuphoric()=android.os.Build.CPU_ABI

        private fun getFlail(context: Context)=context.packageName

        private fun gethodges(context: Context)=try {
            AdvertisingIdClient.getAdvertisingIdInfo(context).id
        }catch (e:Exception){
            ""
        }

        private fun getharden()= UUID.randomUUID().toString()

        private fun getamigo()= android.os.Build.BRAND

        private fun getinborn():String{
            val default = Locale.getDefault()
            return "${default.language}_${default.country}"
        }

        private fun getNocturne()=Locale.getDefault().country

        private fun getAnion(context: Context): String {
            try {
                val id: String = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                return if ("9774d56d682e549c" == id) "" else id ?: ""
            }catch (e:Exception){

            }
            return ""
        }

        private fun getJury()=TimeZone.getDefault().rawOffset/3600/1000

        fun getmaniacal(context: Context):String{
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager.networkOperator
            }catch (e:Exception){

            }
            return ""
        }

        private fun getUmbra(context: Context):String{
            try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (null==connectivityManager){
                    return "no"
                }
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (null==activeNetworkInfo||activeNetworkInfo.isAvailable){
                    return "no"
                }
                val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (null!=wifiInfo){
                    val state = wifiInfo.state
                    if (null!=state){
                        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                            return "wifi"
                        }
                    }
                }
                return "mobile"
            }catch (ex:Exception){

            }
            return "no"
        }

        private fun getelegy()=Build.VERSION.RELEASE

        private fun getcarrara()=Build.MODEL

        private fun getegret()=Build.MANUFACTURER

        fun getdoghouse(context: Context)=context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA).versionName

        private fun encrypt(raw: String): String {
            var md5Str = raw
            try {
                val md = MessageDigest.getInstance("MD5")
                md.update(raw.toByteArray())
                val encryContext = md.digest()
                var i: Int
                val buf = StringBuffer("")
                for (offset in encryContext.indices) {
                    i = encryContext[offset].toInt()
                    if (i < 0) {
                        i += 256
                    }
                    if (i < 16) {
                        buf.append("0")
                    }
                    buf.append(Integer.toHexString(i))
                }
                md5Str = buf.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return md5Str
        }

        private fun gethubby(context: Context):String{
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.density.toString()
        }

        fun getrever(context: Context)= encrypt(getAnion(context))
    }

}