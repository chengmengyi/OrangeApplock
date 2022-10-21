package com.demo.orangeapplock.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.demo.orangeapplock.R
import com.demo.orangeapplock.bean.AppInfo
import kotlinx.android.synthetic.main.layout_app_list_item.view.*

class AppListAdapter(
    private val context: Context,
    private val list:ArrayList<AppInfo>,
    private val clickLock:(info:AppInfo)->Unit
):RecyclerView.Adapter<AppListAdapter.MyView>() {

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        private val ivLock=view.findViewById<AppCompatImageView>(R.id.iv_lock)
        init {
            ivLock.setOnClickListener {
                clickLock.invoke(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.layout_app_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val appInfo = list[position]
            tv_app_name.text=appInfo.label
            iv_app_logo.setImageDrawable(appInfo.icon)
            iv_lock.setImageResource(if (appInfo.lock)R.drawable.suo1 else R.drawable.suo2)
        }
    }

    override fun getItemCount(): Int = list.size
}