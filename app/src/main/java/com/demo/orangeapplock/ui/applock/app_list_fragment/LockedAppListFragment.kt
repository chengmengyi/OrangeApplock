package com.demo.orangeapplock.ui.applock.app_list_fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.orangeapplock.R
import com.demo.orangeapplock.adapter.AppListAdapter
import com.demo.orangeapplock.admob.AdType
import com.demo.orangeapplock.admob.ShowLockAdManager
import com.demo.orangeapplock.bean.AppInfo
import com.demo.orangeapplock.util.AppListManager
import com.demo.orangeapplock.util.show
import kotlinx.android.synthetic.main.layout_app_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class LockedAppListFragment:Fragment() {
    private lateinit var lockedListAdapter:AppListAdapter
    private var selectAppInfo: AppInfo?=null
    private val showAd by lazy { ShowLockAdManager(AdType.LOCK_AD,requireActivity()){
        unLockApp()
    } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_app_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        setTipsSpan()
        setInstallAdapter()
    }

    private fun setInstallAdapter(){
        lockedListAdapter= AppListAdapter(requireContext(),AppListManager.lockedList){
            selectAppInfo=it
            showAd.showLockAd()
        }
        rv_app_list.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=lockedListAdapter
        }
    }

    private fun unLockApp(){
        selectAppInfo?.let {
            it.lock=false
            AppListManager.deleteLockApp(it)
            EventBus.getDefault().post("")
        }
    }

    private fun setTipsSpan(){
        if (AppListManager.lockedList.isEmpty()){
            var str="Enter Installed Apps list, and click % of apps to lock it now!"
            val indexOf = str.indexOf("% ")
            val spannableString= SpannableString(str)
            val d = ContextCompat.getDrawable(requireContext(),R.drawable.suo2)!!
            d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
            spannableString.setSpan(
                ImageSpan(d),
                indexOf,
                indexOf+1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tv_tips.text=spannableString
            tv_tips.movementMethod = LinkMovementMethod.getInstance()
            tv_tips.show(true)
        }else{
            tv_tips.show(false)
        }
    }

    @Subscribe
    fun onEvent(str:String) {
        lockedListAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}