package com.example.finalproject.ui.update_listing.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.update_listing.models.Image
import kotlinx.android.synthetic.main.each_row_update_listing_photos.view.add_image_card_view_update
import kotlinx.android.synthetic.main.each_row_update_listing_photos.view.card_img_update_listing_photos
import kotlinx.android.synthetic.main.each_row_update_listing_photos.view.floating_action_delete_update
import kotlinx.android.synthetic.main.each_row_update_listing_photos.view.img_update_listing_photos

class UpdateListingPhotosAdapter(
    private val list: ArrayList<Image>,
    private val onAddImageClick: () -> Unit,
    private val onDeleteImageClick: (String) -> Unit
) : RecyclerView.Adapter<UpdateListingPhotosAdapter.MyViewHolder>(){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_update_listing_photos,
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
            holder.itemView.add_image_card_view_update.visibility = View.VISIBLE
            holder.itemView.card_img_update_listing_photos.visibility = View.GONE
            holder.itemView.img_update_listing_photos.visibility = View.GONE
            holder.itemView.floating_action_delete_update.visibility = View.GONE
            holder.itemView.add_image_card_view_update.setOnClickListener {
                onAddImageClick.invoke()
            }
        } else {
            holder.itemView.add_image_card_view_update.visibility = View.GONE
            holder.itemView.card_img_update_listing_photos.visibility = View.VISIBLE
            holder.itemView.img_update_listing_photos.visibility = View.VISIBLE
            holder.itemView.floating_action_delete_update.visibility = View.VISIBLE

            Glide
                .with(holder.itemView.context)
                .load(x.url)
                .into(holder.itemView.img_update_listing_photos)

            holder.itemView.floating_action_delete_update.setOnClickListener {
                onDeleteImageClick.invoke(x._id)
                list.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addImage(imageUrl: String) {
        list.add(list.size - 1, Image("", "", imageUrl)
        )
        notifyDataSetChanged()
    }
}