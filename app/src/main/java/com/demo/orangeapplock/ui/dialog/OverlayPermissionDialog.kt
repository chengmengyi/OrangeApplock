package com.demo.orangeapplock.ui.dialog

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import kotlinx.android.synthetic.main.layout_request_overlay_permission.*

class OverlayPermissionDialog(
    private val callback:(sure:Boolean)->Unit
):BaseDialogFragment(R.layout.layout_request_overlay_permission) {
    override fun onView() {
        dialog?.setCancelable(false)
        tv_close.setOnClickListener {
            callback.invoke(false)
            dismiss()
        }
        tv_sure.setOnClickListener {
            callback.invoke(true)
            dismiss()
        }
    }
}