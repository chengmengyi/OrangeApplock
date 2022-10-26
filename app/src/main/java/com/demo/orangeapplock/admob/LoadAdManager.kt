package com.demo.orangeapplock.admob

import com.demo.orangeapplock.appLockApp
import com.demo.orangeapplock.bean.AdmobBean
import com.demo.orangeapplock.bean.ConfigAdBean
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.util.printAppLock
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import org.json.JSONObject

object LoadAdManager {
    private val appLockAdRes= hashMapOf<String,AdmobBean>()
    private val appLockLoading= arrayListOf<String>()

    fun checkCanLoad(type:String,isOpenAd:Boolean=true){
        if (hasLoading(type)){
            return
        }
        if (hasCache(type)){
            return
        }
        if (AdShowClickManager.checkLimit()){
            printAppLock("load ad limit")
            return
        }
        val configAdList = getConfigAdListByType(type)
        if (configAdList.isNotEmpty()){
            appLockLoading.add(type)
            loadAd(type,configAdList.iterator(),isOpenAd)
        }else{
            printAppLock("$type list empty")
        }
    }

    private fun loadAd(type: String, iterator: Iterator<ConfigAdBean>, isOpenAd: Boolean){
        startLoadAdByType(type,iterator.next()){
            if (null==it){
                if (iterator.hasNext()){
                    loadAd(type,iterator,isOpenAd)
                }else{
                    appLockLoading.remove(type)
                    if (type== AdType.OPEN_AD&&isOpenAd){
                        checkCanLoad(type,isOpenAd = false)
                    }
                }
            }else{
                printAppLock("load $type ad success")
                appLockLoading.remove(type)
                appLockAdRes[type]=it
            }
        }
    }

    private fun startLoadAdByType(type:String,configAdBean: ConfigAdBean,loadResult:(admob:AdmobBean?)->Unit){
        printAppLock("load $type ad ,${configAdBean}")
        val typeApplock = configAdBean.type_applock
        if (typeApplock=="kai"){
            AppOpenAd.load(
                appLockApp,
                configAdBean.id_applock,
                AdRequest.Builder().build(),
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback(){
                    override fun onAdLoaded(p0: AppOpenAd) {
                        loadResult.invoke(AdmobBean(System.currentTimeMillis(),p0))
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        printAppLock("load $type fail,${p0.message}")
                        loadResult.invoke(null)
                    }
                }
            )
        }
        if (typeApplock=="cha"){
            InterstitialAd.load(
                appLockApp,
                configAdBean.id_applock,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        printAppLock("load $type fail,${p0.message}")
                        loadResult.invoke(null)
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        loadResult.invoke(AdmobBean(System.currentTimeMillis(),p0))
                    }
                }
            )
        }
        if (typeApplock=="yuan"){
            AdLoader.Builder(
                appLockApp,
                configAdBean.id_applock,
            ).forNativeAd {
                loadResult.invoke(AdmobBean(System.currentTimeMillis(),it))
            }
                .withAdListener(object : AdListener(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        printAppLock("load $type fail,${p0.message}")
                        loadResult.invoke(null)
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        AdShowClickManager.writeClickNum()
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .setAdChoicesPlacement(
                            NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                        )
                        .build()
                )
                .build()
                .loadAd(AdRequest.Builder().build())
        }

    }

    private fun hasLoading(type: String):Boolean{
        if(appLockLoading.contains(type)){
            printAppLock("$type loading")
            return true
        }
        return false
    }

    private fun hasCache(type: String):Boolean{
        if (appLockAdRes.containsKey(type)){
            val admobBean = appLockAdRes[type]
            if (null!=admobBean?.ad){
                if ((System.currentTimeMillis() - admobBean.time) >=3600000L){
                    removeAdByType(type)
                }else{
                    printAppLock("$type has cache")
                    return true
                }
            }
        }
        return false
    }
    
    private fun getConfigAdListByType(type: String):List<ConfigAdBean>{
        val list= arrayListOf<ConfigAdBean>()
        try {
            val jsonArray = JSONObject(FireManager.getLocalAdStr()).getJSONArray(type)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    ConfigAdBean(
                        jsonObject.optString("id"),
                        jsonObject.optInt("sort"),
                        jsonObject.optString("type"),
                        jsonObject.optString("source"),
                    )
                )
            }
        }catch (e:Exception){
            printAppLock("==$type=${e.message}=");
        }
        return list.filter { it.source_applock == "admob" }.sortedByDescending { it.sort_applock }
    }

    fun removeAdByType(type: String){
        appLockAdRes.remove(type)
    }

    fun getAdByType(type: String)= appLockAdRes[type]?.ad
}