package com.demo.orangeapplock.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseUI() {
    private var animator:ValueAnimator?=null

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(){
        startAnimator()
    }
    
    private fun startAnimator(){
        animator=ValueAnimator.ofInt(0, 100).apply {
            duration=3000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                launch_progress.progress = progress
            }
            doOnEnd { jumpToHomeUI() }
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