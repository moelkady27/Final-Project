package com.example.finalproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.home.models.Residence
import kotlinx.android.synthetic.main.each_row_featured_estates.view.apartment_location_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.apartment_name_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.apartment_price_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.image_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.tv_apartment_view_all
import kotlinx.android.synthetic.main.each_row_featured_estates.view.tv_featured_estates_3

class HomeFeaturedAdapter(
    private val list: MutableList<Residence>
): RecyclerView.Adapter<HomeFeaturedAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_featured_estates,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val homeFeatured = list[position]

        holder.itemView.tv_apartment_view_all.text = homeFeatured.category
        holder.itemView.apartment_name_featured_estates.text = homeFeatured.title
        holder.itemView.apartment_location_featured_estates.text = homeFeatured.location.fullAddress
        holder.itemView.apartment_price_featured_estates.text = homeFeatured.salePrice.toString()
        holder.itemView.tv_featured_estates_3.text = homeFeatured.paymentPeriod

        if (homeFeatured.images.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(homeFeatured.images[0].url)
                .into(holder.itemView.image_featured_estates)
        } else {
            // TODO: Show default image when the list is empty
        }

    }

    fun addItems(newItems: List<Residence>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
}
