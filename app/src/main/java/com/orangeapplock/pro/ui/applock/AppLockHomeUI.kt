package com.orangeapplock.pro.ui.applock

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.orangeapplock.pro.R
import com.orangeapplock.pro.adapter.ViewPagerAdapter
import com.orangeapplock.pro.admob.AdType
import com.orangeapplock.pro.admob.ShowNativeAdManager
import com.orangeapplock.pro.base.BaseUI
import com.orangeapplock.pro.service.AppLockServers
import com.orangeapplock.pro.ui.applock.app_list_fragment.InstallAppListFragment
import com.orangeapplock.pro.ui.applock.app_list_fragment.LockedAppListFragment
import kotlinx.android.synthetic.main.activity_applock_home.*

class AppLockHomeUI:BaseUI() {
    private val showAd by lazy { ShowNativeAdManager(AdType.APP_LOCK_HOME_AD,this) }

    private val fragmentList= arrayListOf<Fragment>()

    override fun layoutId(): Int = R.layout.activity_applock_home

    override fun initView() {
        immersionBar.statusBarView(view_top).init()
        setTabIndex(0)
        setAdapter()
        iv_set.setOnClickListener {
            finish()
        }

        startService(Intent(this,AppLockServers::class.java))
    }

    private fun setAdapter(){
        initFragment()
        viewpager.adapter=ViewPagerAdapter(fragmentList,supportFragmentManager)
        viewpager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    setTabIndex(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            }
        )
        tv_locked.setOnClickListener {
            setTabIndex(0)
            viewpager.currentItem=0
        }
        tv_install.setOnClickListener {
            setTabIndex(1)
            viewpager.currentItem=1
        }
    }

    private fun setTabIndex(index:Int){
        tv_locked.isSelected=index==0
        tv_install.isSelected=index==1
    }

    private fun initFragment(){
        fragmentList.add(LockedAppListFragment())
        fragmentList.add(InstallAppListFragment())
    }

    override fun onResume() {
        super.onResume()
        showAd.checkHasAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        showAd.endCheck()
    }
}