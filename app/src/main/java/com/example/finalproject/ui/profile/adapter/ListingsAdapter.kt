package com.example.finalproject.ui.profile.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.profile.models.Residence
import com.example.finalproject.ui.update_listing.activities.UpdateResidenceActivity
import kotlinx.android.synthetic.main.each_row_listings_profile.iv_listings_profile_edit
import kotlinx.android.synthetic.main.each_row_listings_profile.view.iv_image_listings_profile
import kotlinx.android.synthetic.main.each_row_listings_profile.view.iv_listings_profile_edit
import kotlinx.android.synthetic.main.each_row_listings_profile.view.listings_profile_price_2
import kotlinx.android.synthetic.main.each_row_listings_profile.view.tv_listings_profile_title_1
import kotlinx.android.synthetic.main.each_row_listings_profile.view.tv_listings_profile_title_2

class ListingsAdapter(
    private val list: MutableList<Residence>
): RecyclerView.Adapter<ListingsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val itemIds = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_listings_profile,
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
            tv_listings_profile_title_1.text = residence.title
            listings_profile_price_2.text = residence.salePrice.toString()
            tv_listings_profile_title_2.text = residence.location.fullAddress
//            number_star_listings_profile.text = residence.likes.toString()

            if (residence.images.isNotEmpty()) {
                Glide
                    .with(this)
                    .load(residence.images.first().url)
                    .into(iv_image_listings_profile)
            }
            else {
                Glide
                    .with(this)
                    .load(R.drawable.image_pending)
                    .into(iv_image_listings_profile)
            }

            iv_listings_profile_edit.setOnClickListener {
                val intent = Intent(context, UpdateResidenceActivity::class.java)
                context.startActivity(intent)
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