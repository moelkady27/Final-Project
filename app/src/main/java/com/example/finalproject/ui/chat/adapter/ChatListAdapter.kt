package com.example.finalproject.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.chat.models.ChatUser
import kotlinx.android.synthetic.main.each_row_chat_list.view.image_chat_list
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_content
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_name
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_time
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatListAdapter(
    private val onItemClick: (ChatUser) -> Unit
): RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {

    private var list : List<ChatUser> = ArrayList()
    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    fun setChatUserList(chatUserList: List<ChatUser>){
        this.list = chatUserList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_row_chat_list ,
            parent ,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chatUser = list[position]

        holder.itemView.message_list_name.text = chatUser.fullName
        holder.itemView.message_list_content.text = chatUser.lastMessage.message.text

        val formattedTime = formatTime(chatUser.lastMessage.createdAt)
        holder.itemView.message_list_time.text = formattedTime

        Glide
            .with(holder.itemView)
            .load(chatUser.image.url)
            .into(holder.itemView.image_chat_list)

        holder.itemView.setOnClickListener {
            onItemClick(chatUser)
        }
    }

    private fun formatTime(createdAt: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("Africa/Cairo")
        val date = inputFormat.parse(createdAt)
        return outputFormat.format(date!!)
    }

}