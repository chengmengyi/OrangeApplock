package com.demo.orangeapplock.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.demo.orangeapplock.R

object FailPwdDialogManager {
    fun showFailNumDialog(context: Context){
        val view = LayoutInflater.from(context).inflate(R.layout.layout_pwd_fail_num, null, false)
        val tv_tips = view.findViewById<AppCompatTextView>(R.id.tv_tips)
        val str="Gesture password is wrong, you can try ${AppLockPwdManager.failNum} more times!"
        tv_tips.text=str
        val tv_close=view.findViewById<AppCompatTextView>(R.id.tv_close)
        val dialog = AlertDialog.Builder(context).setView(view).create()
        tv_close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

    }

    fun showRestPwdDialog(context: Context,first:Boolean,sureCallback:()->Unit){
        val view = LayoutInflater.from(context).inflate(R.layout.layout_reset_pwd, null, false)
        val dialog = AlertDialog.Builder(context).setView(view).create()

        val tv_tips = view.findViewById<AppCompatTextView>(R.id.tv_tips)
        tv_tips.text=if (first) "Do you want to reset your password?\n" +
                "if you reset the password, all currently encrypted data will also be deleted!"
        else "All currently encrypted data will also be deleted!\n" +
                "Are you sure?"

        val tv_close=view.findViewById<AppCompatTextView>(R.id.tv_close)
        tv_close.setOnClickListener {
            dialog.dismiss()
        }
        val tv_sure=view.findViewById<AppCompatTextView>(R.id.tv_sure)
        tv_sure.text=if (first)"Confirm" else "Delete"
        tv_sure.setOnClickListener {
            sureCallback.invoke()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

    }
}