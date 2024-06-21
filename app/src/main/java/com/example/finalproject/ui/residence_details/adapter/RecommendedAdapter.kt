package com.example.finalproject.ui.residence_details.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.recommendation.models.Data
import com.example.finalproject.ui.residence_details.activities.ResidenceDetailsActivity
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.image_recommended
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.iv_recommended_favourite
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.recommended_title_1
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.recommended_title_3
import kotlinx.android.synthetic.main.each_row_recommended_for_you.view.tv_recommended_location

class RecommendedAdapter(
    var list: List<Data>
): RecyclerView.Adapter<RecommendedAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_recommended_for_you,
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
                recommended_title_1.text = it.title
                recommended_title_3.text = it.salePrice.toString()
                tv_recommended_location.text = it.location.fullAddress

                if (it.images.isNotEmpty()) {
                    Glide
                        .with(this)
                        .load(it.images[0].url)
                        .into(image_recommended)
                }

            iv_recommended_favourite.visibility = View.GONE

                holder.itemView.setOnClickListener {
                    val context = holder.itemView.context
                    val intent = Intent(context, ResidenceDetailsActivity::class.java)
                    intent.putExtra("residenceId", x._id)
                    intent.putExtra("residence_Id", x.Id.toString())
                    context.startActivity(intent)
                }
            }
        }
    }
}