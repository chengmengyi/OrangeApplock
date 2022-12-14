package com.orangeapplock.pro.ui.dialog

import com.orangeapplock.pro.R
import com.orangeapplock.pro.base.BaseDialogFragment
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