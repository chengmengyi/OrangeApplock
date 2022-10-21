package com.demo.orangeapplock.ui.dialog

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import kotlinx.android.synthetic.main.layout_request_permission.*

class PermissionDialog(
    private val callback:(sure:Boolean)->Unit
):BaseDialogFragment(R.layout.layout_request_permission) {
    override fun onView() {
        dialog?.setCancelable(false)
        tv_close.setOnClickListener {
            dismiss()
            callback.invoke(false)
        }
        tv_sure.setOnClickListener {
            dismiss()
            callback.invoke(true)
        }
    }
}