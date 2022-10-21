package com.demo.orangeapplock.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.local.LocalManager
import com.demo.orangeapplock.util.showToast
import kotlinx.android.synthetic.main.activity_set.*
import java.lang.Exception

class SetUI:BaseUI() {
    override fun layoutId(): Int = R.layout.activity_set

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        back.setOnClickListener { finish() }

        ll_contact.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data= Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, LocalManager.appLockEmail)
                startActivity(intent)
            }catch (e: Exception){
                showToast("Contact us by email：${LocalManager.appLockEmail}")
            }
        }

        ll_policy.setOnClickListener {
            startActivity(Intent(this,PolicyUI::class.java))
        }

        ll_update.setOnClickListener {
            val packName = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packName")
            }
            startActivity(intent)
        }

        ll_share.setOnClickListener {
            val pm = packageManager
            val packageName=pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=${packageName}"
            )
            startActivity(Intent.createChooser(intent, "share"))
        }
    }
}