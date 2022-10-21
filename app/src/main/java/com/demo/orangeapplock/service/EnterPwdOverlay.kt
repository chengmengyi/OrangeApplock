package com.demo.orangeapplock.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.orangeapplock.R
import com.demo.orangeapplock.adapter.EnteredPwdAdapter
import com.demo.orangeapplock.adapter.KeyboardAdapter
import com.demo.orangeapplock.util.AppLockPwdManager
import com.demo.orangeapplock.util.showToast

@SuppressLint("StaticFieldLeak")
object EnterPwdOverlay {
    var isShowing=false
    private val pwdList= arrayListOf<String>()
    private lateinit var view:View
    private lateinit var tips:AppCompatTextView
    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var enteredPwdAdapter:EnteredPwdAdapter
    private lateinit var keyboardAdapter: KeyboardAdapter

    fun initOverlayView(context: Context){
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        fitHeight(context)
        view = LayoutInflater.from(context).inflate(R.layout.activity_enter_pwd, null)
        tips=view.findViewById(R.id.tv_tips)
        tips.text="Enter Your Password"
        setAdapter(context)
    }

    private fun setAdapter(context: Context){
        val rv_pwd = view.findViewById<RecyclerView>(R.id.rv_pwd)
        val rv_keyboard = view.findViewById<RecyclerView>(R.id.rv_keyboard)
        enteredPwdAdapter= EnteredPwdAdapter(context)
        keyboardAdapter=KeyboardAdapter(context){
            clickKeyboard(it)
        }
        rv_pwd.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter=enteredPwdAdapter
        }
        rv_keyboard.apply {
            layoutManager= GridLayoutManager(context,3)
            adapter=keyboardAdapter
        }
    }

    private fun clickKeyboard(content:String){
        if(content=="b"){
            if (enteredPwdAdapter.getPwdFail()){
                setTipsColor(false)
            }
            if (pwdList.isNotEmpty()){
                pwdList.removeLast()
                updateEnteredPwd()
            }
            return
        }
        if (content=="x"){
            pwdList.clear()
            updateEnteredPwd()
            if (enteredPwdAdapter.getPwdFail()){
                setTipsColor(false)
            }
            return
        }
        if (pwdList.size>4){
            return
        }
        pwdList.add(content)
        updateEnteredPwd()
        if(pwdList.size==4){
            pwdEnterFinish()
        }
    }

    private fun pwdEnterFinish(){
        var pwd=""
        pwdList.forEach {
            pwd+=it
        }
        if (AppLockPwdManager.checkPwdInOverlay(pwd)){
            tips.text="Enter Your Password"
            pwdList.clear()
            updateEnteredPwd()
            hideOverlay()
        }else{
            tips.text="Password error"
            setTipsColor(true)
        }
    }

    private fun setTipsColor(isSelected:Boolean){
        tips.isSelected=isSelected
        enteredPwdAdapter.setPwdFail(isSelected)
    }

    private fun updateEnteredPwd(){
        enteredPwdAdapter.setPwdLength(pwdList.size)
    }

    fun showOverlay(){
        if (!isShowing){
            isShowing=true
            windowManager.addView(view, layoutParams)
        }
    }

    fun hideOverlay(){
        if (isShowing){
            isShowing=false
            windowManager.removeView(view)
        }
    }

    private fun fitHeight(context: Context){
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val td = metrics.heightPixels / 760f
        val dpi = (160 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }
}