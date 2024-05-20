package com.example.finalproject.ui.home.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.home.models.Residence
import com.example.finalproject.ui.home.models.ResidenceX
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.image_featured_view_all
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.iv_featured_view_all_fav
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.image_popular_view_all
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.iv_popular_view_all_favourite
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.popular_view_all_title_1
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.popular_view_all_title_3
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.popular_view_all_title_5
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.tv_popular_view_all_location

class PopularViewAllAdapter(
    private val context: Context,
    private val list: MutableList<ResidenceX>,
    private val onItemClicked: (ResidenceX, Boolean) -> Unit
): RecyclerView.Adapter<PopularViewAllAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("liked_residences", Context.MODE_PRIVATE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_popular_view_all,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val popularEstates = list[position]

        holder.itemView.popular_view_all_title_1.text = popularEstates.title
        holder.itemView.popular_view_all_title_3.text = popularEstates.salePrice.toString()
        holder.itemView.popular_view_all_title_5.text = popularEstates.paymentPeriod
        holder.itemView.tv_popular_view_all_location.text = popularEstates.location.fullAddress

        if (popularEstates.images.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(popularEstates.images[0].url)
                .into(holder.itemView.image_popular_view_all)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.home_image)
                .into(holder.itemView.image_popular_view_all)
        }

        val isLiked = sharedPreferences.getBoolean(popularEstates._id, false)
        popularEstates.isLiked = isLiked

        if (isLiked) {
            holder.itemView.iv_popular_view_all_favourite.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.colorPrimary)
        } else {
            holder.itemView.iv_popular_view_all_favourite.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.edit_text)
        }

        holder.itemView.iv_popular_view_all_favourite.setOnClickListener {
            val newLikedState = !isLiked
            popularEstates.isLiked = newLikedState
            saveLikedState(popularEstates._id, newLikedState)
            notifyItemChanged(position)
            onItemClicked(popularEstates, newLikedState)
        }
    }

    fun addItems(newItems: List<ResidenceX>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    private fun saveLikedState(residenceId: String, isLiked: Boolean) {
        sharedPreferences.edit().putBoolean(residenceId, isLiked).apply()
    }
}