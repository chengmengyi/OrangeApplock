package com.orangeapplock.pro.util

import androidx.fragment.app.FragmentActivity
import com.orangeapplock.pro.ui.dialog.PwdFailNumDialog
import com.orangeapplock.pro.ui.dialog.ResetPwdDialog
import com.tencent.mmkv.MMKV

object AppLockPwdManager {
    private var pwd=""
    var failNum=3

    fun initPwd(){
        pwd=MMKV.defaultMMKV().decodeString("pwd","")?:""
    }

    fun setLocalPwd(pwd:String){
        this.pwd=pwd
        MMKV.defaultMMKV().encode("pwd",pwd)
    }

    fun hasLocalPwd():Boolean{
        return pwd.isNotEmpty()
    }

    fun checkPwdRight(fragmentActivity:FragmentActivity,pwd: String,resetPwd:()->Unit):Boolean{
        val right = this.pwd == pwd
        return if (right){
            true
        }else{
            failNum--
            if (failNum<=0){
                val firstDialog = ResetPwdDialog(true){
                    val resetPwdDialog = ResetPwdDialog(false) {
                        setLocalPwd("")
                        AppListManager.removeLockedList()
                        resetPwd.invoke()
                    }
                    resetPwdDialog.show(fragmentActivity.supportFragmentManager,"resetPwdDialog")
                }
                firstDialog.show(fragmentActivity.supportFragmentManager,"firstDialog")
            }else{
                PwdFailNumDialog().show(fragmentActivity.supportFragmentManager,"PwdFailNumDialog")
            }
            false
        }
    }

    fun checkPwdInOverlay(pwd: String)=this.pwd == pwd

}