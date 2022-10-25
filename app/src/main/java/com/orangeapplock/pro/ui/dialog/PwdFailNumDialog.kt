package com.orangeapplock.pro.ui.dialog

import com.orangeapplock.pro.R
import com.orangeapplock.pro.base.BaseDialogFragment
import com.orangeapplock.pro.util.AppLockPwdManager
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