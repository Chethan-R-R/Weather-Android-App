package com.example.weather_android_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchListAdapter(private val data:List<Result>,private val handleClick:(Result)->Unit):RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val textView:TextView=itemView.findViewById(R.id.text_view)
        fun bind(result:Result){
            textView.text=result.formatted
            itemView.setOnClickListener {
                handleClick(result
                )
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.search_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListAdapter.ViewHolder, position: Int) {
        val item=data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }


}