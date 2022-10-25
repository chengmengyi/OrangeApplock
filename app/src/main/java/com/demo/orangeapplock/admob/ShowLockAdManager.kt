package com.demo.orangeapplock.admob

import android.app.Activity
import android.content.Context
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.util.printAppLock
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowLockAdManager(
    private val type:String,
    private val context:Activity,
    private val showResult:()->Unit
) {

    fun showLockAd(){
        AdShowClickManager.addLockShowNum()
        val adByType = LoadAdManager.getAdByType(type)
        if ((AdShowClickManager.lockAdShowNum%5)!=0){
            showResult.invoke()
            return
        }
        if (null==adByType||AdShowClickManager.checkLimit()){
            showResult.invoke()
        }else{
            if (null!=adByType){
                if (AdShowClickManager.openShowing){
                    showResult.invoke()
                }else{
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
        appOpenAd.show(context)
    }

    private fun showChaPingAd(interstitialAd:InterstitialAd){
        interstitialAd.fullScreenContentCallback=callback
        interstitialAd.show(context)
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
                showResult.invoke()
            }
        }
    }
}