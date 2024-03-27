package com.example.finalproject.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.chat.models.ChatUser
import kotlinx.android.synthetic.main.each_row_chat_list.view.image_chat_list
import kotlinx.android.synthetic.main.each_row_search.view.image_chat_list_search
import kotlinx.android.synthetic.main.each_row_search.view.message_list_name_search
import java.util.Locale

class SearchUserAdapter(
    private val onItemClick: (User) -> Unit
): RecyclerView.Adapter<SearchUserAdapter.MyViewHolder>() {

    private var list : List<User> = ArrayList()
    private var filteredList: List<User> = ArrayList()
    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    fun setChatUserList(chatUserList: List<User>) {
        list = chatUserList
        filterList("") // Clear filter initially
    }

    private fun filterList(query: String) {
        filteredList = if (query.isBlank()) {
            list
        } else {
            list.filter { user ->
                user.fullName.toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_search ,
            parent ,
            false
        )
        return SearchUserAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val user = filteredList[position]
//
////        val isOnline = onlineUsers.contains(chatUser._id)
////        val isActiveChat = activeChatUserIds.contains(chatUser._id)
//
//        holder.itemView.message_list_name_search.text = user.fullName
//
//        Glide
//            .with(holder.itemView)
//            .load(user.image.url)
//            .into(holder.itemView.image_chat_list)
//
//        holder.itemView.setOnClickListener {
//            onItemClick(user)
//        }
//    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = filteredList[position]

        holder.itemView.message_list_name_search.text = user.fullName

        // Check if the image URL is not null or empty before loading with Glide
        if (user.image.url.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(user.image.url)
                .into(holder.itemView.image_chat_list_search)
        }

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

}