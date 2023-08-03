package com.example.login2.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.login2.DataClass
import com.example.login2.R

class ImagesAdapter(private val context: Context, private var dataList: List<DataClass>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].dataImage)
            .into(holder.recImage)
        holder.recDesc.text = dataList[position].dataDesc
        holder.postUser.text = dataList[position].dataUser
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView
    var recDesc: TextView
    var postUser: TextView
    var recCard: CardView
    init {
        recImage = itemView.findViewById(R.id.eachItem)
        recDesc = itemView.findViewById(R.id.caption)
        postUser = itemView.findViewById(R.id.postUsername)
        recCard = itemView.findViewById(R.id.recCard)
    }
}