package com.example.finalproject.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.profile.models.Residence
import kotlinx.android.synthetic.main.each_row_pending_profile.view.iv_image_pending_profile
import kotlinx.android.synthetic.main.each_row_pending_profile.view.number_star_pending_profile
import kotlinx.android.synthetic.main.each_row_pending_profile.view.tv_pending_profile_rent
import kotlinx.android.synthetic.main.each_row_pending_profile.view.tv_pending_profile_title_1

class PendingAdapter(
    private val list: List<Residence>
): RecyclerView.Adapter<PendingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_pending_profile,
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
            tv_pending_profile_title_1.text = residence.title
            tv_pending_profile_rent.text = residence.type
            number_star_pending_profile.text = residence.status

            Glide.with(this)
                .load(residence.images.firstOrNull()?.url)
                .into(iv_image_pending_profile)

//            if (residence.isLiked) {
//
//            } else {
//
//            }

        }
    }


}