package com.demo.orangeapplock.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.orangeapplock.R
import com.demo.orangeapplock.bean.KeyboardBean
import kotlinx.android.synthetic.main.layout_keyboard_item.view.*

class KeyboardAdapter(
    private val context: Context,
    private val clickItem:(content:String)->Unit
):RecyclerView.Adapter<KeyboardAdapter.MyView>() {
    private val list= arrayListOf<KeyboardBean>()
    init {
        list.add(KeyboardBean("1", R.drawable.s1))
        list.add(KeyboardBean("2", R.drawable.s2))
        list.add(KeyboardBean("3", R.drawable.s3))
        list.add(KeyboardBean("4", R.drawable.s4))
        list.add(KeyboardBean("5", R.drawable.s5))
        list.add(KeyboardBean("6", R.drawable.s6))
        list.add(KeyboardBean("7", R.drawable.s7))
        list.add(KeyboardBean("8", R.drawable.s8))
        list.add(KeyboardBean("9", R.drawable.s9))
        list.add(KeyboardBean("x", R.drawable.sx))
        list.add(KeyboardBean("0", R.drawable.s0))
        list.add(KeyboardBean("b", R.drawable.sb))
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                clickItem.invoke(list[layoutPosition].content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView =
        MyView(LayoutInflater.from(context).inflate(R.layout.layout_keyboard_item,parent,false))

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val keyboardBean = list[position]
            iv_keyboard.setImageResource(keyboardBean.icon)
        }
    }

    override fun getItemCount(): Int = list.size
}