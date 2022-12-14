package com.orangeapplock.pro.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.ActivityUtils
import com.orangeapplock.pro.R
import com.orangeapplock.pro.admob.AdType
import com.orangeapplock.pro.admob.LoadAdManager
import com.orangeapplock.pro.admob.ShowOpenAdManager
import com.orangeapplock.pro.base.BaseUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseUI() {
    private var animator:ValueAnimator?=null
    private val showAd by lazy { ShowOpenAdManager(AdType.OPEN_AD,this){
        jumpToHomeUI()
    } }

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(){
        preLoadAd()
        startAnimator()
    }

    private fun preLoadAd(){
        LoadAdManager.checkCanLoad(AdType.OPEN_AD)
        LoadAdManager.checkCanLoad(AdType.HOME_AD)
        LoadAdManager.checkCanLoad(AdType.APP_LOCK_HOME_AD)
        LoadAdManager.checkCanLoad(AdType.LOCK_AD)
    }
    
    private fun startAnimator(){
        animator=ValueAnimator.ofInt(0, 100).apply {
            duration=10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                launch_progress.progress = progress
                val pro = (10 * (progress / 100.0F)).toInt()
                if (pro in 2..9){
                    showAd.showOpenAd {jump->
                        stopAnimator()
                        launch_progress.progress = 100
                        if (jump){
                            jumpToHomeUI()
                        }
                    }
                }else if (pro>=10){
                    jumpToHomeUI()
                }
            }
            start()
        }
    }

    private fun jumpToHomeUI(){
        val activityExistsInStack = ActivityUtils.isActivityExistsInStack(HomeUI::class.java)
        if (!activityExistsInStack){
            startActivity(Intent(this, HomeUI::class.java))
        }
        finish()
    }

    private fun stopAnimator(){
        animator?.removeAllUpdateListeners()
        animator?.cancel()
        animator=null
    }

    override fun onResume() {
        super.onResume()
        animator?.resume()
    }

    override fun onPause() {
        super.onPause()
        animator?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimator()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }

}