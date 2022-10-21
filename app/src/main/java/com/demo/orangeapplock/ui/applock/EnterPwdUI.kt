package com.demo.orangeapplock.ui.applock

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.orangeapplock.R
import com.demo.orangeapplock.adapter.EnteredPwdAdapter
import com.demo.orangeapplock.adapter.KeyboardAdapter
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.enums.EnterPwdType
import com.demo.orangeapplock.ui.dialog.SetPwdSuccessDialog
import com.demo.orangeapplock.util.AppLockPwdManager
import com.demo.orangeapplock.util.FailPwdDialogManager
import com.demo.orangeapplock.util.showToast
import kotlinx.android.synthetic.main.activity_enter_pwd.*

class EnterPwdUI:BaseUI() {
    private var pwdStr=""
    private var enterPwdType=EnterPwdType.SET_PWD
    private var pwdList= arrayListOf<String>()
    private val enteredPwdAdapter by lazy { EnteredPwdAdapter(this) }
    private val keyboardAdapter by lazy { KeyboardAdapter(this){ clickKeyboard(it) } }

    override fun layoutId(): Int = R.layout.activity_enter_pwd

    override fun initView() {
        getEnterPwdType()
        setAdapter()
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
        when(enterPwdType){
            EnterPwdType.SET_PWD->{
                pwdList.forEach {
                    pwdStr+=it
                }
                enterPwdType=EnterPwdType.ENTER_PWD_AGAIN
                setTopTips()
                pwdList.clear()
                updateEnteredPwd()
            }
            EnterPwdType.ENTER_PWD_AGAIN->{
                var pwdAgain=""
                pwdList.forEach {
                    pwdAgain+=it
                }
                if (pwdAgain != pwdStr){
                    showToast("The two passwords are inconsistent")
                    setTipsColor(true)
                }else{
                    AppLockPwdManager.setLocalPwd(pwdStr)
                    val setPwdSuccessDialog = SetPwdSuccessDialog{
                        jumpToAppLockHome()
                    }
                    setPwdSuccessDialog.show(supportFragmentManager,"SetPwdSuccessDialog")
                }
            }
            EnterPwdType.CHECK_PWD->{
                var pwd=""
                pwdList.forEach {
                    pwd+=it
                }
                if(AppLockPwdManager.checkPwdRight(this,pwd){
                    resetPwd()
                }){
                    jumpToAppLockHome()
                }else{
                    setTipsColor(true)
                }
            }
        }
    }

    private fun jumpToAppLockHome(){
        startActivity(Intent(this,AppLockHomeUI::class.java))
        finish()
    }

    private fun updateEnteredPwd(){
        enteredPwdAdapter.setPwdLength(pwdList.size)
    }

    private fun setAdapter(){
        rv_pwd.apply {
            layoutManager=LinearLayoutManager(this@EnterPwdUI,LinearLayoutManager.HORIZONTAL,false)
            adapter=enteredPwdAdapter
        }
        rv_keyboard.apply {
            layoutManager=GridLayoutManager(this@EnterPwdUI,3)
            adapter=keyboardAdapter
        }
    }

    private fun getEnterPwdType(){
        enterPwdType = if (AppLockPwdManager.hasLocalPwd()){
            EnterPwdType.CHECK_PWD
        }else{
            EnterPwdType.SET_PWD
        }
        setTopTips()
    }

    private fun setTopTips(){
        tv_tips.text=when(enterPwdType){
            EnterPwdType.SET_PWD->"Set Your Password"
            EnterPwdType.ENTER_PWD_AGAIN->"Enter Your Password"
            else->"Enter Your Password"
        }
    }

    private fun setTipsColor(isSelected:Boolean){
        tv_tips.isSelected=isSelected
        enteredPwdAdapter.setPwdFail(isSelected)
    }

    private fun resetPwd(){
        enterPwdType=EnterPwdType.SET_PWD
        setTopTips()
        setTipsColor(false)
        pwdList.clear()
        updateEnteredPwd()
    }
}