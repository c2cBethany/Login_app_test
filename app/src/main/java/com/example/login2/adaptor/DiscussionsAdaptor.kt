package com.example.login2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.login2.datasource.DataClassForum

class DiscussionsAdaptor(private val context: Context, private var dataList: List<DataClassForum>) :
    RecyclerView.Adapter<MyViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.each_text, parent, false)
        return MyViewHolder2(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        holder.recDesc.text = dataList[position].dataDesc
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recDesc: TextView
    var recCard: CardView
    init {
        recDesc = itemView.findViewById(R.id.eachText)
        recCard = itemView.findViewById(R.id.recCardForum)
    }
}