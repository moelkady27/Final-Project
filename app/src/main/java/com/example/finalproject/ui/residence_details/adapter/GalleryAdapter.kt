package com.example.finalproject.ui.residence_details.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import kotlinx.android.synthetic.main.each_row_gallery_details.view.image_gallery_details

class GalleryAdapter(
    val list: MutableList<String>
): RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_gallery_details,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageUrl = list[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.itemView.image_gallery_details)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<String>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}