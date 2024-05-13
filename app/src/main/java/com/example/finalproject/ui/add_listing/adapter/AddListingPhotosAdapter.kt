package com.example.finalproject.ui.add_listing.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.add_listing.models.Image
import kotlinx.android.synthetic.main.each_row_add_listing_photos.view.add_image_card_view
import kotlinx.android.synthetic.main.each_row_add_listing_photos.view.card_img_add_listing_photos
import kotlinx.android.synthetic.main.each_row_add_listing_photos.view.floating_action_delete
import kotlinx.android.synthetic.main.each_row_add_listing_photos.view.img_add_listing_photos

class AddListingPhotosAdapter(
    private val list: ArrayList<Image>,
    private val onAddImageClick: () -> Unit
): RecyclerView.Adapter<AddListingPhotosAdapter.MyViewHolder>(){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_add_listing_photos,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val x = list[position]

        if (x.isAddButton) {
            holder.itemView.add_image_card_view.visibility = View.VISIBLE
            holder.itemView.card_img_add_listing_photos.visibility = View.GONE
                        holder.itemView.img_add_listing_photos.visibility = View.GONE
            holder.itemView.floating_action_delete.visibility = View.GONE
            holder.itemView.add_image_card_view.setOnClickListener {
                onAddImageClick.invoke()
            }
        } else {
            holder.itemView.add_image_card_view.visibility = View.GONE
            holder.itemView.card_img_add_listing_photos.visibility = View.VISIBLE
            holder.itemView.img_add_listing_photos.visibility = View.VISIBLE
            holder.itemView.floating_action_delete.visibility = View.VISIBLE

            Glide
                .with(holder.itemView.context)
                .load(x.url)
                .into(holder.itemView.img_add_listing_photos)

            holder.itemView.floating_action_delete.setOnClickListener {
                list.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addImage(imageUrl: String) {
        list.add(list.size - 1, Image("", "", imageUrl))
        notifyDataSetChanged()
    }
}