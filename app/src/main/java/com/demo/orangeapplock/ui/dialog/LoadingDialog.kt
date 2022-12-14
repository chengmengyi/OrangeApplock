package com.demo.orangeapplock.ui.dialog

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import com.demo.orangeapplock.R
import com.demo.orangeapplock.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog:BaseDialogFragment(R.layout.dialog_loading) {
    private var objectAnimator: ObjectAnimator?=null

    override fun onView() {
        dialog?.setCancelable(false)
        objectAnimator=ObjectAnimator.ofFloat(iv_loading, "rotation", 0f, 360f).apply {
            duration=1000L
            repeatCount= ValueAnimator.INFINITE
            repeatMode= ObjectAnimator.RESTART
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        objectAnimator?.removeAllUpdateListeners()
        objectAnimator?.cancel()
        objectAnimator=null
    }
}