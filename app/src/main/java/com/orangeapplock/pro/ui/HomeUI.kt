package com.orangeapplock.pro.ui

import android.content.Intent
import android.provider.Settings
import com.orangeapplock.pro.R
import com.orangeapplock.pro.admob.AdType
import com.orangeapplock.pro.admob.ShowNativeAdManager
import com.orangeapplock.pro.base.BaseUI
import com.orangeapplock.pro.ui.applock.EnterPwdUI
import com.orangeapplock.pro.ui.dialog.PermissionDialog
import com.orangeapplock.pro.util.ActivityCallback
import com.orangeapplock.pro.util.LockUtil
import com.orangeapplock.pro.util.showToast
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