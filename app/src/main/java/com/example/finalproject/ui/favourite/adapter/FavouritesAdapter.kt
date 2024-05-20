package com.example.finalproject.ui.favourite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.favourite.models.Wishlist
import kotlinx.android.synthetic.main.each_row_favourite.view.apartment_location_favourite
import kotlinx.android.synthetic.main.each_row_favourite.view.apartment_name_favourite
import kotlinx.android.synthetic.main.each_row_favourite.view.apartment_price_favourite
import kotlinx.android.synthetic.main.each_row_favourite.view.cv_image_favourite
import kotlinx.android.synthetic.main.each_row_favourite.view.imageView2
import kotlinx.android.synthetic.main.each_row_favourite.view.image_favourite
import kotlinx.android.synthetic.main.each_row_favourite.view.number_star_favourite
import kotlinx.android.synthetic.main.each_row_favourite.view.tv_apartment_favourites

class FavouritesAdapter(
    private val list: List<Wishlist>
): RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_favourite,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favourite = list[position]

        holder.itemView.apply {
            tv_apartment_favourites.text = favourite.category
            apartment_name_favourite.text = favourite.title
            apartment_location_favourite.text = favourite.location
            apartment_price_favourite.text = favourite.salePrice.toString()
            number_star_favourite.text = favourite.rating.toString()

            Glide.with(this)
                .load(favourite.images.firstOrNull()?.url)
                .into(image_favourite)

        }
    }
}