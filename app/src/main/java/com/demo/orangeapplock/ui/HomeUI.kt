package com.demo.orangeapplock.ui

import android.content.Intent
import android.provider.Settings
import com.demo.orangeapplock.R
import com.demo.orangeapplock.admob.AdType
import com.demo.orangeapplock.admob.ShowNativeAdManager
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.ui.applock.EnterPwdUI
import com.demo.orangeapplock.ui.dialog.PermissionDialog
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.demo.orangeapplock.util.ActivityCallback
import com.demo.orangeapplock.util.LockUtil
import com.demo.orangeapplock.util.checkFloatPermission
import com.demo.orangeapplock.util.showToast
import kotlinx.android.synthetic.main.activity_home.*

class HomeUI:BaseUI() {
    private val showAd by lazy {  ShowNativeAdManager(AdType.HOME_AD,this) }

    override fun layoutId(): Int = R.layout.activity_home

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        iv_set.setOnClickListener {
            startActivity(Intent(this,SetUI::class.java))

        }

        view_app_lock.setOnClickListener {
            if (!LockUtil.isStatAccessPermissionSet(this) && LockUtil.isNoOption(this)) {
                val permissionDialog = PermissionDialog{
                    if (it){
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        startActivityForResult(intent, 1011)
                    }
                }
                permissionDialog.show(supportFragmentManager,"PermissionDialog")

            } else {
                jumpToEnterPwdUI()
            }
        }

        view_vpn.setOnClickListener {
            startActivity(Intent(this,ServerHomeUI::class.java))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1011) {
            if (LockUtil.isStatAccessPermissionSet(this)) {
                jumpToEnterPwdUI()
            } else {
                showToast("No permission")
            }
        }
    }

    private fun jumpToEnterPwdUI(){
        startActivity(Intent(this,EnterPwdUI::class.java))
    }

    override fun onResume() {
        super.onResume()
        if(ActivityCallback.loadHomeAd){
            showAd.checkHasAd()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showAd.endCheck()
    }
}