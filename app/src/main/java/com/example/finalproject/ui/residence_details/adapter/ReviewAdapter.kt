package com.example.finalproject.ui.residence_details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.residence_details.models.Review
import kotlinx.android.synthetic.main.each_row_review_details.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    var list: MutableList<Review>,
    private val onLikeClicked: (Review, Int) -> Unit,
    private val onRemoveLikeClicked: (Review, Int) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_review_details,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = list[position]

        val image = review.userId.image.url
        Glide
            .with(holder.itemView)
            .load(image)
            .into(holder.itemView.iv_agent_review_image)

        holder.itemView.tv_review_name_user.text = review.userId.username

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(review.createdAt)
        val formattedDate = outputFormat.format(date!!)
        holder.itemView.tv_review_time.text = formattedDate

        holder.itemView.tv_review_comment.text = review.comment

        val stars = listOf(
            holder.itemView.star_review_details_1,
            holder.itemView.star_review_details_2,
            holder.itemView.star_review_details_3,
            holder.itemView.star_review_details_4,
            holder.itemView.star_review_details_5
        )
        for (i in stars.indices) {
            if (i < review.rating) {
                stars[i].setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.starColor))
            } else {
                stars[i].setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.home_popular))
            }
        }

        holder.itemView.number_star_review_details.text = review.rating.toString()

        holder.itemView.number_of_likes_review_details.text = review.likes.toString()

        holder.itemView.number_of_dislikes_review_details.text = review.unLikes.toString()

        if (holder.itemView.iv_like_review.isSelected) {
            holder.itemView.iv_like_review.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.colorPrimary)
        } else {
            holder.itemView.iv_like_review.backgroundTintList =
                ContextCompat.getColorStateList(holder.itemView.context, R.color.edit_text)
        }

        holder.itemView.iv_like_review.setOnClickListener {
            if (holder.itemView.iv_like_review.isSelected) {
                onRemoveLikeClicked(review, position)
            } else {
                onLikeClicked(review, position)
            }
            holder.itemView.iv_like_review.isSelected = !holder.itemView.iv_like_review.isSelected
        }

    }

    fun updateLikes(reviewId: String, newLikesCount: Int, newUnlikesCount: Int) {
        val position = list.indexOfFirst { it._id == reviewId }
        if (position != -1) {
            list[position].likes = newLikesCount
            list[position].unLikes = newUnlikesCount
            notifyItemChanged(position)
        }
    }

}
