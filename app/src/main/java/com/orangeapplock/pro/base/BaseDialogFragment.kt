package com.orangeapplock.pro.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.orangeapplock.pro.R
import com.gyf.immersionbar.ImmersionBar

abstract class BaseDialogFragment(private val layoutId:Int) : DialogFragment() {
    protected var mWindow:Window?=null
    protected var immersionBar:ImmersionBar?=null

    override fun onStart() {
        super.onStart()
        mWindow=dialog?.window
        mWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        immersionBar=ImmersionBar.with(this).apply {
            statusBarAlpha(0f)
            autoDarkModeEnable(true)
            statusBarDarkFont(false)
            init()
        }
        onView()
    }

    abstract fun onView()
}