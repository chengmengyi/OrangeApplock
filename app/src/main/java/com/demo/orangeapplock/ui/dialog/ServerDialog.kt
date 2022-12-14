package com.demo.orangeapplock.ui.dialog

import android.content.Intent
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import com.demo.orangeapplock.ui.server.ServerHomeUI
import com.demo.orangeapplock.util.SetPointManager
import kotlinx.android.synthetic.main.layout_server_dialog.*

class ServerDialog(private val sure:(sure:Boolean)->Unit): BaseDialogFragment(R.layout.layout_server_dialog){

    override fun onView() {
        dialog?.setCancelable(false)
        SetPointManager.point("oa_v_show_pop")
        tv_close.setOnClickListener {
            SetPointManager.point("oa_v_close_pop")
            dismiss()
            sure.invoke(false)
        }
        tv_sure.setOnClickListener {
            SetPointManager.point("oa_v_click_pop")
            dismiss()
            sure.invoke(true)
        }
    }
}