package com.orangeapplock.pro.admob

import com.orangeapplock.pro.base.BaseUI
import com.orangeapplock.pro.util.printAppLock
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowOpenAdManager(
    private val type:String,
    private val baseUI: BaseUI,
    private val showResult:()->Unit
) {

    fun showOpenAd(finish:(to:Boolean)->Unit){
        val adByType = LoadAdManager.getAdByType(type)
        if (null==adByType&&AdShowClickManager.checkLimit()){
            finish.invoke(true)
        }else{
            if (null!=adByType){
                if (AdShowClickManager.openShowing||!baseUI.onResume){
                    finish.invoke(false)
                }else{
                    finish.invoke(false)
                    printAppLock("start show $type")
                    when(adByType){
                        is InterstitialAd->{
                            showChaPingAd(adByType)
                        }
                        is AppOpenAd->{
                            showKaiPingAd(adByType)
                        }
                    }
                }
            }
        }
    }

    private fun showKaiPingAd(appOpenAd:AppOpenAd){
        appOpenAd.fullScreenContentCallback=callback
        appOpenAd.show(baseUI)
    }

    private fun showChaPingAd(interstitialAd:InterstitialAd){
        interstitialAd.fullScreenContentCallback=callback
        interstitialAd.show(baseUI)
    }


    private val callback=object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            AdShowClickManager.openShowing=false
            showFinish()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            AdShowClickManager.openShowing=true
            AdShowClickManager.writeShowNum()
            LoadAdManager.removeAdByType(type)
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            AdShowClickManager.openShowing=false
            LoadAdManager.removeAdByType(type)
            showFinish()
        }

        override fun onAdClicked() {
            super.onAdClicked()
            AdShowClickManager.writeClickNum()
        }

        private fun showFinish(){
            if (type!=AdType.OPEN_AD){
                LoadAdManager.checkCanLoad(type)
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(200L)
                if (baseUI.onResume){
                    showResult.invoke()
                }
            }
        }
    }
}