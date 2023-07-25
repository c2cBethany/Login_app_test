package com.example.login2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login2.databinding.EachTextBinding
import kotlinx.coroutines.withContext

class DiscussionsAdaptor(private var mList: List<String>) :
    RecyclerView.Adapter<DiscussionsAdaptor.DiscussionsViewHolder>() {

    class DiscussionsViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.eachText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionsViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_text, parent, false)

        return DiscussionsViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: DiscussionsViewHolder, position: Int) {
        val item = mList[position]
        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}