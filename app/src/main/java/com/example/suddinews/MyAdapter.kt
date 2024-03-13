package com.example.suddinews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter (val dlist:List<Dclass>,val context:Context):RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder(Iview: View) : RecyclerView.ViewHolder(Iview){
        val imageView: ImageView = Iview.findViewById(R.id.title_image)
        val textView: TextView = Iview.findViewById(R.id.textView)
        val card:CardView=Iview.findViewById(R.id.cn)
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
        holder.card.setOnClickListener {
            val bundle = Bundle().apply {
                putString("NEWS_TITLE", dm.header)
                putString("NEWS_CONTENT", dm.content)
                putString("VIDEO_URI", dm.vid.toString())
                putString("IMAGE_URI", dm.img.toString())
            }
            holder.card.setOnClickListener {
                val intent = Intent(context, News_Extended::class.java)
                intent.putExtras(bundle)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}