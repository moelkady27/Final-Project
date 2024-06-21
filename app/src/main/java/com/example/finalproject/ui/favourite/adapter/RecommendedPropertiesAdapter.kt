package com.example.finalproject.ui.favourite.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.recommendation.models.Data
import com.example.finalproject.ui.residence_details.activities.ResidenceDetailsActivity
import kotlinx.android.synthetic.main.each_row_featured_view_all.view.iv_featured_view_all_fav
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.image_recommended
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.iv_recommended_favourite
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.recommended_title_1
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.recommended_title_3
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.tv_recommended_location
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.apartment_location_recommends_estates_fav
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.apartment_name_recommends_estates_fav
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.apartment_price_recommends_estates_fav
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.image_recommends_estates_fav
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.iv_recommends_estates_fav_fav
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.number_star_recommends_estates_fav_one
import kotlinx.android.synthetic.main.each_row_recommended_properties_fav.view.tv_apartment_recommends_estates_fav

class RecommendedPropertiesAdapter(
    var list: List<Data>,
    private val onFavouriteClick: (String, Boolean) -> Unit
): RecyclerView.Adapter<RecommendedPropertiesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_recommended_properties_fav,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val x = list[position]

        x?.let {
            holder.itemView.apply {
//                number_star_recommends_estates_fav_one.text = x.avgRating.toString()
                tv_apartment_recommends_estates_fav.text = x.category
                apartment_name_recommends_estates_fav.text = x.title
                apartment_location_recommends_estates_fav.text = x.location.fullAddress
                apartment_price_recommends_estates_fav.text = x.salePrice.toString()

                val isLiked = x.isLiked

                if (isLiked) {
                    holder.itemView.iv_recommends_estates_fav_fav.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.colorPrimary)
                } else {
                    holder.itemView.iv_recommends_estates_fav_fav.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.edit_text)
                }

                if (x.images.isNotEmpty()) {
                    Glide
                        .with(this)
                        .load(x.images[0].url)
                        .into(image_recommends_estates_fav)
                }

                if (it.avgRating != null) {
                    number_star_recommends_estates_fav_one.text = it.avgRating.toString()
                } else {
                    // handle the case where avgRating is null
                }

                holder.itemView.iv_recommends_estates_fav_fav.setOnClickListener {
                    onFavouriteClick(x._id, x.isLiked)
                    x.isLiked = !x.isLiked
                    notifyItemChanged(position)
                }

//            holder.itemView.setOnClickListener {
//                val context = holder.itemView.context
//                val intent = Intent(context, ResidenceDetailsActivity::class.java)
//                intent.putExtra("residenceId", x._id)
//                intent.putExtra("residence_Id", x.Id.toString())
//                context.startActivity(intent)
//            }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavouriteStatus(residenceId: String, isLiked: Boolean) {
        val item = list.find { it?._id == residenceId }
        item?.let {
            it.isLiked = isLiked
            notifyDataSetChanged()
        }
    }
}