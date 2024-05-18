package com.example.finalproject.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.profile.models.Residence
import kotlinx.android.synthetic.main.each_row_sold_profile.view.iv_image_sold_profile
import kotlinx.android.synthetic.main.each_row_sold_profile.view.sold_profile_price_2
import kotlinx.android.synthetic.main.each_row_sold_profile.view.tv_sold_profile_title_1
import kotlinx.android.synthetic.main.each_row_sold_profile.view.tv_sold_profile_title_2

class SoldAdapter(
    private val list: List<Residence>
): RecyclerView.Adapter<SoldAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_sold_profile,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val residence = list[position]

        holder.itemView.apply {
            tv_sold_profile_title_1.text = residence.title
            sold_profile_price_2.text = residence.salePrice.toString()
            tv_sold_profile_title_2.text = residence.location.fullAddress
//            number_star_sold_profile.text = residence.likes.toString()

            Glide.with(this)
                .load(residence.images.firstOrNull()?.url)
                .into(iv_image_sold_profile)

//            if (residence.isLiked) {
//
//            } else {
//
//            }
        }
    }

}