package com.demo.orangeapplock.ui

import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.demo.orangeapplock.R
import com.demo.orangeapplock.admob.AdType
import com.demo.orangeapplock.admob.ShowNativeAdManager
import com.demo.orangeapplock.base.BaseUI
import com.demo.orangeapplock.online.FireManager
import com.demo.orangeapplock.server.ServerManager
import com.demo.orangeapplock.ui.applock.EnterPwdUI
import com.demo.orangeapplock.ui.dialog.IRDialog
import com.demo.orangeapplock.ui.dialog.LoadingDialog
import com.demo.orangeapplock.ui.dialog.PermissionDialog
import com.demo.orangeapplock.ui.dialog.ServerDialog
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.demo.orangeapplock.util.*
import com.github.shadowsocks.bg.BaseService
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class HomeUI:BaseUI() {
    private var isIR=false
    private var showingServerDialog=false
    private val showAd by lazy {  ShowNativeAdManager(AdType.HOME_AD,this) }

    override fun layoutId(): Int = R.layout.activity_home

    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        checkIR()
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
            jumpToServerUI(false)
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
        checkOaProgram()
        checkOaShow()
    }

    private fun checkOaShow(){
        readReferrer {
            if(isBuyUser(it)&&FireManager.oaProgram=="B"){
                return@readReferrer
            }
//        - 0-如果用户没有在连接状态，冷热启动都出现（热启动：退出到后台5s后）
//        - 1-仅在冷启动出现
            if(FireManager.oaShow=="1"&&ActivityCallback.loadType==0){
                ActivityCallback.loadType=2
                checkOaType()
            }
            if(FireManager.oaShow=="0"&& ServerManager.state!=BaseService.State.Connected&&ActivityCallback.loadType!=2){
                ActivityCallback.loadType=2
                checkOaType()
            }
        }

    }

    private fun checkOaType(){
//        1. 0  表示不根据referrer字段对功能作区分，显示VPN弹窗
//                2. 1 表示根据referrer字段对功能区分，非自然用户显示VPN弹窗，自然用户不显示VPN弹窗
//                1. referrer字段包含【fb4a】【gclid】【not%20set】【youtubeads】【%7B%22】识别为买量
//        3. 2  代表根据reffer判断只有FB买量用户能看到引导
//                1. FB用户为referrer字段包含facebook.或者fb4a
//        4. 3 表示所有用户都看不到vpn弹窗

        when(FireManager.oaType){
            "0"->showServerDialog()
            "1"->readReferrer {
                if(isBuyUser(it)){
                    showServerDialog()
                }
            }
            "2"->readReferrer {
                //    1. FB用户为referrer字段包含facebook.或者fb4a
                if (it.contains("facebook")||it.contains("fb4a")){
                    showServerDialog()
                }
            }
        }
    }

    private fun showServerDialog(){
        if(!showingServerDialog){
            showingServerDialog=true
            ServerDialog{
                showingServerDialog=false
                if (it){
                    jumpToServerUI(true)
                }
            }.show(supportFragmentManager,"ServerDialog")
        }
    }

    private fun jumpToServerUI(connect:Boolean){
        if (isIR){
            IRDialog().show(supportFragmentManager,"IRDialog")
        }else{
            startActivity(Intent(this,ServerHomeUI::class.java).apply {
                putExtra("connect",connect)
            })
        }
    }

    private fun readReferrer(callback:(referrer:String)->Unit){
        val decodeString = MMKV.defaultMMKV().decodeString("referrer", "")?:""
        callback.invoke(decodeString)
//        callback.invoke("fb4a")
    }

    private fun checkIR(){
        val country = Locale.getDefault().country
        if(country=="IR"){
            isIR=true
        }else{
            OkGo.get<String>("https://api.myip.com/")
                .execute(object : StringCallback(){
                    override fun onSuccess(response: Response<String>?) {
//                        ipJson="""{"ip":"89.187.185.11","country":"United States","cc":"IR"}"""
                        try {
                            isIR=JSONObject(response?.body()?.toString()).optString("cc")=="IR"
                        }catch (e:Exception){

                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)
                    }
                })
        }
    }

    private fun checkOaProgram(){
        //本地默认80% B   20%A
        if(FireManager.oaProgram.isEmpty()){
            val oaProgram = MMKV.defaultMMKV().decodeString("oaProgram") ?: ""
            if (oaProgram.isEmpty()){
                val nextInt = Random().nextInt(100)
                FireManager.oaProgram=if (nextInt>20) "B" else "A"
//                FireManager.oaProgram="B"
                MMKV.defaultMMKV().encode("oaProgram",oaProgram)
            }else{
                FireManager.oaProgram=oaProgram
            }
        }
        SetPointManager.setUserProperty()
        if(ActivityCallback.loadType==2||!ActivityCallback.topIsHome){
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if(!onResume){
                return@launch
            }
            ActivityCallback.loadType=2
            withContext(Dispatchers.Main){
                if(ServerManager.state!=BaseService.State.Connected){
                    readReferrer {
                        if (isBuyUser(it)&&FireManager.oaProgram=="B"){
                            jumpToServerUI(true)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showAd.endCheck()
    }
}