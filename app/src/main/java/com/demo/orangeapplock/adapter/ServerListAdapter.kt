package com.demo.orangeapplock.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.orangeapplock.R
import kotlinx.android.synthetic.main.server_list_item_layout.view.*

class ServerListAdapter(
    private val context: Context,
):RecyclerView.Adapter<ServerListAdapter.ServerListView>() {

    inner class ServerListView(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerListView {
        return ServerListView(LayoutInflater.from(context).inflate(R.layout.server_list_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ServerListView, position: Int) {
        with(holder.itemView){
            item_layout.isSelected=position==1
            tv_server_country.isSelected=position==1
        }
    }

    override fun getItemCount(): Int = 10
}