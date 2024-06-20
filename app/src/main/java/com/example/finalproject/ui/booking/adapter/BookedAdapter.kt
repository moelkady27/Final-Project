package com.example.finalproject.ui.booking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.booking.models.ResidenceX
import kotlinx.android.synthetic.main.each_row_booked_residences.view.*

class BookedAdapter(
    private var list: List<ResidenceX>
): RecyclerView.Adapter<BookedAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_booked_residences,
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

        Glide.with(holder.itemView.context)
            .load(residence.images.firstOrNull()?.url)
            .into(holder.itemView.image_booked_residences)

        holder.itemView.tv_apartment_booked_residences.text = residence.category
        holder.itemView.number_star_booked_residences_one.text = residence.avgRating.toString()
        holder.itemView.apartment_name_booked_residences.text = residence.title
        holder.itemView.apartment_location_booked_residences.text = residence.location.fullAddress
        holder.itemView.tv_booked_residences_1.visibility = View.GONE
        holder.itemView.apartment_price_booked_residences.visibility = View.GONE
        holder.itemView.tv_booked_residences_2.visibility = View.GONE
        holder.itemView.tv_booked_residences_3.visibility = View.GONE
        holder.itemView.iv_booked_residences_fav.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newResidences: List<ResidenceX>) {
        list = newResidences
        notifyDataSetChanged()
    }
}