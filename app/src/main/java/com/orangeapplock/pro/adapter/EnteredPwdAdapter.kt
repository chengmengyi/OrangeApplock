package com.orangeapplock.pro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orangeapplock.pro.R
import kotlinx.android.synthetic.main.layout_entered_pwd_item.view.*

class EnteredPwdAdapter(private val context: Context):RecyclerView.Adapter<EnteredPwdAdapter.MyView>() {
    private var pwdLength=0
    private var pwdFail=false

    fun setPwdLength(length:Int){
        pwdLength=length
        notifyDataSetChanged()
    }

    fun setPwdFail(fail:Boolean){
        pwdFail=fail
        notifyDataSetChanged()
    }

    fun getPwdFail()=pwdFail

    inner class MyView (view:View):RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView =
        MyView(LayoutInflater.from(context).inflate(R.layout.layout_entered_pwd_item,parent,false))

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            if (pwdFail){
                iv_pwd.setImageResource(R.drawable.p3)
            }else{
                iv_pwd.setImageResource(if (pwdLength>=(position+1)) R.drawable.p2 else R.drawable.p1)
            }
        }
    }

    override fun getItemCount(): Int = 4
}