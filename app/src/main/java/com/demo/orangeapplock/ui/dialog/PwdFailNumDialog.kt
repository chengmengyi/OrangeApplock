package com.demo.orangeapplock.ui.dialog

import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import com.demo.orangeapplock.util.AppLockPwdManager
import kotlinx.android.synthetic.main.layout_pwd_fail_num.*

class PwdFailNumDialog: BaseDialogFragment(R.layout.layout_pwd_fail_num){
    override fun onView() {
        dialog?.setCancelable(false)

        val str="Gesture password is wrong, you can try ${AppLockPwdManager.failNum} more times!"
        tv_tips.text=str

        tv_close.setOnClickListener {
            dismiss()
        }
    }
}