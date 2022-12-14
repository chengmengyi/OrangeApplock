package com.orangeapplock.pro.ui.applock.app_list_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangeapplock.pro.R
import com.orangeapplock.pro.adapter.AppListAdapter
import com.orangeapplock.pro.admob.AdType
import com.orangeapplock.pro.admob.ShowLockAdManager
import com.orangeapplock.pro.bean.AppInfo
import com.orangeapplock.pro.ui.dialog.OverlayPermissionDialog
import com.orangeapplock.pro.util.AppListManager
import com.orangeapplock.pro.util.checkFloatPermission
import com.orangeapplock.pro.util.show
import kotlinx.android.synthetic.main.layout_app_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class InstallAppListFragment:Fragment() {
    private var lock=true
    private var selectAppInfo:AppInfo?=null
    private val showAd by lazy { ShowLockAdManager(AdType.LOCK_AD,requireActivity()){
        lockApp()
    } }

    private lateinit var installListAdapter:AppListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_app_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_tips.show(false)
        EventBus.getDefault().register(this)
        setInstallAdapter()
    }

    private fun setInstallAdapter(){
        installListAdapter= AppListAdapter(requireContext(),AppListManager.installList){
            if (checkFloatPermission(requireContext())) {
                lock=!it.lock
                selectAppInfo=it
                showAd.showLockAd()
            }else{
                requestPermissionDialog()
            }
        }
        rv_app_list.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=installListAdapter
        }
    }

    private fun lockApp(){
        selectAppInfo?.let {
            if (lock){
                it.lock=true
                AppListManager.addLockApp(it)
            }else{
                it.lock=false
                AppListManager.deleteLockApp(it)
            }
            installListAdapter.notifyDataSetChanged()
            EventBus.getDefault().post("")
        }
    }

    private fun requestPermissionDialog(){
        val overlayPermissionDialog = OverlayPermissionDialog {
            if (it){
                startActivityForResult(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                            "package:${requireContext().packageName}"
                        )
                    ), 1012
                )

            }
        }
        overlayPermissionDialog.show(childFragmentManager,"overlayPermissionDialog")
    }

    @Subscribe
    fun onEvent(str:String) {
        installListAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}