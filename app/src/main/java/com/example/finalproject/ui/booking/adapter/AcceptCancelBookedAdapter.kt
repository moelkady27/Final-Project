package com.example.finalproject.ui.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.booking.models.BookedBy
import kotlinx.android.synthetic.main.each_row_accept_cancel_booked.view.iv_user_accept_cancel
import kotlinx.android.synthetic.main.each_row_accept_cancel_booked.view.iv_yes
import kotlinx.android.synthetic.main.each_row_accept_cancel_booked.view.tv_user_name_accept_cancel

class AcceptCancelBookedAdapter(
    var list: MutableList<BookedBy>,
    private val onAcceptClick: (BookedBy) -> Unit
) : RecyclerView.Adapter<AcceptCancelBookedAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_accept_cancel_booked,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val booked = list[position]

        val image = booked.image.url
        Glide
            .with(holder.itemView)
            .load(image)
            .into(holder.itemView.iv_user_accept_cancel)

        holder.itemView.tv_user_name_accept_cancel.text = booked.username

        holder.itemView.iv_yes.setOnClickListener {
            onAcceptClick(booked)
        }
    }

}