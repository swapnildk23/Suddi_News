package com.example.suddinews

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter (val dlist:List<Dclass>,val context:Context):RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder(Iview: View) : RecyclerView.ViewHolder(Iview){
        val imageView: ImageView = Iview.findViewById(R.id.title_image)
        val textView: TextView = Iview.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout. news_list,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dm=dlist[position]
        holder.textView.text=dm.header
        Glide.with(context)
            .load(Uri.parse(dm.img))
            .into(holder.imageView)

    }
}