package com.demo.orangeapplock.admob

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.util.ActivityCallback
import com.demo.orangeapplock.util.printAppLock
import com.demo.orangeapplock.util.show
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.*

class ShowNativeAdManager(
    private val type:String,
    private val baseUI: BaseUI
) {
    private var ad:NativeAd?=null
    private var checkJob:Job?=null

    fun checkHasAd(){
        LoadAdManager.checkCanLoad(type)
        endCheck()
        checkJob=GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (!baseUI.onResume){
                return@launch
            }
            while (true) {
                if (!isActive) {
                    break
                }
                val adByType = LoadAdManager.getAdByType(type)
                if (baseUI.onResume&&null != adByType&&adByType is NativeAd) {
                    cancel()
                    ad?.destroy()
                    ad=adByType
                    showNativeAd(adByType)
                }
                delay(1000L)
            }
        }
    }

    private fun showNativeAd(adRes:NativeAd){
        printAppLock("start show ad $type")
        val nativeView = baseUI.findViewById<NativeAdView>(R.id.native_view)
        nativeView.iconView=baseUI.findViewById(R.id.native_logo)
        (nativeView.iconView as ImageFilterView).setImageDrawable(adRes.icon?.drawable)

        nativeView.callToActionView=baseUI.findViewById(R.id.native_install)
        (nativeView.callToActionView as AppCompatTextView).text=adRes.callToAction

        if(type!=AdType.APP_LOCK_HOME_AD){
            nativeView.mediaView=baseUI.findViewById(R.id.native_cover)
            adRes.mediaContent?.let {
                nativeView.mediaView?.apply {
                    setMediaContent(it)
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    outlineProvider = appLockProvider
                }
            }
        }

        nativeView.bodyView=baseUI.findViewById(R.id.native_desc)
        (nativeView.bodyView as AppCompatTextView).text=adRes.body

        nativeView.headlineView=baseUI.findViewById(R.id.native_title)
        (nativeView.headlineView as AppCompatTextView).text=adRes.headline

        nativeView.setNativeAd(adRes)
        baseUI.findViewById<AppCompatImageView>(R.id.native_zhanwei).show(false)
        AdShowClickManager.writeShowNum()
        LoadAdManager.removeAdByType(type)
        LoadAdManager.checkCanLoad(type)
        if (AdType.HOME_AD==type||type==AdType.APP_LOCK_HOME_AD){
            ActivityCallback.loadHomeAd=false
        }
    }

    private val appLockProvider=object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            if (view == null || outline == null) return
            outline.setRoundRect(
                0,
                0,
                view.width,
                view.height,
                SizeUtils.dp2px(8F).toFloat()
            )
            view.clipToOutline = true
        }
    }

    fun endCheck(){
        checkJob?.cancel()
        checkJob=null
    }
}