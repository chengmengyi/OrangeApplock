package com.demo.orangeapplock.ui.dialog

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import kotlinx.android.synthetic.main.layout_reset_pwd.*

class ResetPwdDialog(
    private val first:Boolean,
    private val sureCallback:()->Unit
    ): BaseDialogFragment(R.layout.layout_reset_pwd){

    override fun onView() {
        dialog?.setCancelable(false)

        tv_tips.text=if (first) "Do you want to reset your password?\n" +
                "if you reset the password, all currently encrypted data will also be deleted!"
        else "All currently encrypted data will also be deleted!\n" +
                "Are you sure?"
        tv_sure.text=if (first)"Confirm" else "Delete"

        tv_close.setOnClickListener { dismiss() }
        tv_sure.setOnClickListener {
            sureCallback.invoke()
            dismiss()
        }
    }
}