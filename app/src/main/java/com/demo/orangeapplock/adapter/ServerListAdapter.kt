package com.demo.orangeapplock.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.orangeapplock.R
import com.demo.orangeapplock.bean.ServerInfoBean
import com.demo.orangeapplock.server.ConnectHelper
import com.demo.orangeapplock.server.ServerInfoHelper
import com.demo.orangeapplock.util.getServerIcon
import kotlinx.android.synthetic.main.server_list_item_layout.view.*

class ServerListAdapter(
    private val context: Context,
    private val click:(bean:ServerInfoBean)->Unit
):RecyclerView.Adapter<ServerListAdapter.ServerListView>() {
    private val list= arrayListOf<ServerInfoBean>()
    init {
        list.add(ServerInfoBean())
        list.addAll(ServerInfoHelper.get1011ServerList())
    }

    inner class ServerListView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                click.invoke(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerListView {
        return ServerListView(LayoutInflater.from(context).inflate(R.layout.server_list_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ServerListView, position: Int) {
        with(holder.itemView){
            val serverInfoBean = list[position]
            tv_server_country.text=serverInfoBean.country
            val selected = ConnectHelper.server1011Entity.host == serverInfoBean.host
            item_layout.isSelected=selected
            tv_server_country.isSelected=selected
            iv_server_icon.setImageResource(getServerIcon(serverInfoBean.country))
        }
    }

    override fun getItemCount(): Int = list.size
}