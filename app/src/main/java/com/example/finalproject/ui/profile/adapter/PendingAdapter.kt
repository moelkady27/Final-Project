package com.example.finalproject.ui.profile.adapter

import android.annotation.SuppressLint
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
    private val list: MutableList<Residence>
): RecyclerView.Adapter<PendingAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val itemIds = mutableSetOf<String>()

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

            if (residence.images.isNotEmpty()) {
                Glide
                    .with(this)
                    .load(residence.images.first().url)
                    .into(iv_image_pending_profile)
            }
            else {
                Glide
                    .with(this)
                    .load(R.drawable.image_pending)
                    .into(iv_image_pending_profile)
            }
        }
    }

    fun addItems(newItems: List<Residence>) {
        val uniqueItems = newItems.filter { item ->
            item._id !in itemIds
        }
        val startPosition = list.size
        list.addAll(uniqueItems)
        uniqueItems.forEach { item ->
            itemIds.add(item._id)
        }
        notifyItemRangeInserted(startPosition, uniqueItems.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        list.clear()
        itemIds.clear()
        notifyDataSetChanged()
    }

}