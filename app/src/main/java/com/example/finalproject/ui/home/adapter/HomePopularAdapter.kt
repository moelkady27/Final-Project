package com.example.finalproject.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.home.models.ResidenceX
import kotlinx.android.synthetic.main.each_row_home_popular.view.home_popular_title_1
import kotlinx.android.synthetic.main.each_row_home_popular.view.home_popular_title_3
import kotlinx.android.synthetic.main.each_row_home_popular.view.home_popular_title_5
import kotlinx.android.synthetic.main.each_row_home_popular.view.image_home_popular
import kotlinx.android.synthetic.main.each_row_home_popular.view.iv_home_popular_favourite
import kotlinx.android.synthetic.main.each_row_home_popular.view.tv_home_popular_location
import kotlinx.android.synthetic.main.each_row_popular_view_all.view.iv_popular_view_all_favourite
import java.util.Random
import kotlin.math.min

class HomePopularAdapter(
    private val list: MutableList<ResidenceX>,
    private val onFavouriteClick: (String, Boolean) -> Unit
): RecyclerView.Adapter<HomePopularAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val random = Random()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_home_popular,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return min(list.size, 5)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val homePopular = list[position]

        holder.itemView.home_popular_title_1.text = homePopular.title
        holder.itemView.home_popular_title_3.text = homePopular.salePrice.toString()
        holder.itemView.home_popular_title_5.text = homePopular.paymentPeriod
        holder.itemView.tv_home_popular_location.text = homePopular.location.fullAddress

        if (homePopular.images.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(homePopular.images[0].url)
                .into(holder.itemView.image_home_popular)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.home_image)
                .into(holder.itemView.image_home_popular)
        }

        val isLiked = homePopular.isLiked

        if (isLiked) {
            holder.itemView.iv_home_popular_favourite.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.colorPrimary)
        } else {
            holder.itemView.iv_home_popular_favourite.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.edit_text)
        }

        holder.itemView.iv_home_popular_favourite.setOnClickListener {
            onFavouriteClick(homePopular._id, homePopular.isLiked)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPopularItems(items: List<ResidenceX>) {
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