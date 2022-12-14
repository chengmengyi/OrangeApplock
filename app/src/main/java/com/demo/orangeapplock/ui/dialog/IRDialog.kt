package com.demo.orangeapplock.ui.dialog

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import kotlinx.android.synthetic.main.layout_ir_dialog.*

class IRDialog: BaseDialogFragment(R.layout.layout_ir_dialog){
    override fun onView() {
        dialog?.setCancelable(false)
        tv_close.setOnClickListener {
            dismiss()
        }
    }
}