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
import kotlinx.android.synthetic.main.each_row_search_results.view.apartment_location_search_results
import kotlinx.android.synthetic.main.each_row_search_results.view.apartment_name_search_results
import kotlinx.android.synthetic.main.each_row_search_results.view.apartment_price_search_results
import kotlinx.android.synthetic.main.each_row_search_results.view.image_search_results
import kotlinx.android.synthetic.main.each_row_search_results.view.iv_search_results_fav
import kotlinx.android.synthetic.main.each_row_search_results.view.number_star_search_results
import kotlinx.android.synthetic.main.each_row_search_results.view.tv_apartment_search_results
import kotlinx.android.synthetic.main.each_row_search_results.view.tv_search_results_3
import java.util.Locale

class SearchResultsAdapter(
    private val context: Context,
    private val list: MutableList<Residence>,
    private val onFavouriteClick: (String, Boolean) -> Unit
): RecyclerView.Adapter<SearchResultsAdapter.MyViewHolder>() {

    private var filteredList: MutableList<Residence> = list

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_search_results,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val searchResults = filteredList[position]

        if (searchResults.images.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(searchResults.images[0].url)
                .into(holder.itemView.image_search_results)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.home_image)
                .into(holder.itemView.image_search_results)
        }

        holder.itemView.number_star_search_results.text = searchResults.avgRating.toString()
        holder.itemView.tv_apartment_search_results.text = searchResults.category
        holder.itemView.apartment_name_search_results.text = searchResults.title
        holder.itemView.apartment_location_search_results.text = searchResults.location.fullAddress
        holder.itemView.apartment_price_search_results.text = searchResults.salePrice.toString()
        holder.itemView.tv_search_results_3.text = searchResults.paymentPeriod

        val isLiked = searchResults.isLiked

        if (isLiked) {
            holder.itemView.iv_search_results_fav.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.colorPrimary)
        } else {
            holder.itemView.iv_search_results_fav.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.edit_text)
        }

        holder.itemView.iv_search_results_fav.setOnClickListener {
            onFavouriteClick(searchResults._id, searchResults.isLiked)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ResidenceDetailsActivity::class.java)
            intent.putExtra("residenceId", searchResults._id)
            intent.putExtra("residence_Id", searchResults.Id)
            holder.itemView.context.startActivity(intent)
        }
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

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(query: String) {
        filteredList = if (query.trim().isBlank()) {
            list
        } else {
            list.filter { featured ->
                featured.title.lowercase(Locale.ROOT)
                    .contains(query.trim().lowercase(Locale.ROOT))
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}