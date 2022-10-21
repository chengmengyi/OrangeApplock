package com.demo.orangeapplock.ui.dialog

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import kotlinx.android.synthetic.main.layout_set_pwd_success.*

class SetPwdSuccessDialog(private val callback:()->Unit): BaseDialogFragment(R.layout.layout_set_pwd_success){
    override fun onView() {
        dialog?.setCancelable(false)

        tv_sure.setOnClickListener {
            callback.invoke()
            dismiss()
        }
    }
}