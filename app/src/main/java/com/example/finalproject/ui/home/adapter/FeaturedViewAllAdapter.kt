
package com.example.finalproject.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.home.models.Residence
import com.example.finalproject.ui.residence_details.activities.ResidenceDetailsActivity
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.apartment_location_featured_view_all
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.apartment_name_featured_view_all
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.apartment_price_featured_view_all
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.home_featured_view_all_title_3
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.image_featured_view_all
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.iv_featured_view_all_fav
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.number_star_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.tv_apartment_view_all

class FeaturedViewAllAdapter(
    private val context: Context,
    private val list: MutableList<Residence>,
    private val onFavouriteClick: (String, Boolean) -> Unit
): RecyclerView.Adapter<FeaturedViewAllAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_featured_view_all,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val featuredEstates = list[position]

        holder.itemView.tv_apartment_view_all.text = featuredEstates.category
        holder.itemView.apartment_name_featured_view_all.text = featuredEstates.title
        holder.itemView.apartment_location_featured_view_all.text =
            featuredEstates.location.fullAddress
        holder.itemView.apartment_price_featured_view_all.text =
            featuredEstates.salePrice.toString()
        holder.itemView.home_featured_view_all_title_3.text = featuredEstates.paymentPeriod

        if (featuredEstates.images.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(featuredEstates.images[0].url)
                .into(holder.itemView.image_featured_view_all)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.home_image)
                .into(holder.itemView.image_featured_view_all)
        }

        val isLiked = featuredEstates.isLiked

        if (isLiked) {
            holder.itemView.iv_featured_view_all_fav.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.colorPrimary)
        } else {
            holder.itemView.iv_featured_view_all_fav.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.edit_text)
        }

        holder.itemView.iv_featured_view_all_fav.setOnClickListener {
            onFavouriteClick(featuredEstates._id, featuredEstates.isLiked)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResidenceDetailsActivity::class.java)
            intent.putExtra("residenceId", featuredEstates._id)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.number_star_featured_estates.text = featuredEstates.avgRating.toString()
    }

    fun addItems(newItems: List<Residence>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavouriteStatus(residenceId: String, isLiked: Boolean) {
        list.find { it._id == residenceId }?.isLiked = isLiked
        notifyDataSetChanged()
    }
}
