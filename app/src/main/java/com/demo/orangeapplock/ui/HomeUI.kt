package com.demo.orangeapplock.ui

import android.content.Intent
import android.provider.Settings
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.ui.applock.EnterPwdUI
import com.demo.orangeapplock.ui.dialog.PermissionDialog
import com.demo.orangeapplock.util.LockUtil
import com.demo.orangeapplock.util.checkFloatPermission
import com.demo.orangeapplock.util.showToast
import kotlinx.android.synthetic.main.activity_home.*

class HomeUI:BaseUI() {
    override fun layoutId(): Int = R.layout.activity_home

    override fun initView() {
        view_app_lock.setOnClickListener {
            if (!LockUtil.isStatAccessPermissionSet(this) && LockUtil.isNoOption(this)) {
                val permissionDialog = PermissionDialog{
                    if (it){
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        startActivityForResult(intent, 1011)
                    }else{
                        finish()
                    }
                }
                permissionDialog.show(supportFragmentManager,"PermissionDialog")

            } else {
                jumpToEnterPwdUI()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1011) {
            if (LockUtil.isStatAccessPermissionSet(this)) {
                jumpToEnterPwdUI()
            } else {
                showToast("No permission")
                finish()
            }
        }
    }

    private fun jumpToEnterPwdUI(){
        startActivity(Intent(this,EnterPwdUI::class.java))
    }
}