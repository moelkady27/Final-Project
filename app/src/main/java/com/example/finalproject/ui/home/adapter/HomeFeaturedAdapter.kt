package com.example.finalproject.ui.home.adapter

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.each_row_featured_estates.view.apartment_location_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.apartment_name_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.apartment_price_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.image_featured_estates
import kotlinx.android.synthetic.main.each_row_featured_estates.view.iv_featured_estates_fav
import kotlinx.android.synthetic.main.each_row_featured_estates.view.number_star_featured_estates_one
import kotlinx.android.synthetic.main.each_row_featured_estates.view.tv_apartment_view_all
import kotlinx.android.synthetic.main.each_row_featured_estates.view.tv_featured_estates_3
import java.util.Random
import kotlin.math.min

class HomeFeaturedAdapter(
    private val list: MutableList<Residence>,
    private val onFavouriteClick: (String, Boolean) -> Unit
): RecyclerView.Adapter<HomeFeaturedAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val random = Random()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_featured_estates,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return min(list.size, 5)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val homeFeatured = list[position]

        holder.itemView.tv_apartment_view_all.text = homeFeatured.category
        holder.itemView.apartment_name_featured_estates.text = homeFeatured.title
        holder.itemView.apartment_location_featured_estates.text = homeFeatured.location.fullAddress
        holder.itemView.apartment_price_featured_estates.text = homeFeatured.salePrice.toString()
        holder.itemView.tv_featured_estates_3.text = homeFeatured.paymentPeriod
        holder.itemView.number_star_featured_estates_one.text = homeFeatured.avgRating.toString()

        if (homeFeatured.images.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(homeFeatured.images[0].url)
                .into(holder.itemView.image_featured_estates)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.home_image)
                .into(holder.itemView.image_featured_estates)
        }

        val isLiked = homeFeatured.isLiked

        if (isLiked) {
            holder.itemView.iv_featured_estates_fav.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.colorPrimary)
        } else {
            holder.itemView.iv_featured_estates_fav.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.edit_text)
        }

        holder.itemView.iv_featured_estates_fav.setOnClickListener {
            onFavouriteClick(homeFeatured._id, homeFeatured.isLiked)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResidenceDetailsActivity::class.java)
            intent.putExtra("residenceId", homeFeatured._id)
            holder.itemView.context.startActivity(intent)
        }
    }

//    fun addItems(newItems: List<Residence>) {
//        val startPosition = list.size
//        list.addAll(newItems)
//        notifyItemRangeInserted(startPosition, newItems.size)
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Residence>) {
        list.clear()
        val shuffledItems = items.shuffled(random)
        val selectedItems = shuffledItems.take(5)
        list.addAll(selectedItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavouriteStatus(residenceId: String, isLiked: Boolean) {
        list.find { it._id == residenceId }?.isLiked = isLiked
        notifyDataSetChanged()
    }
}
