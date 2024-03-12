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
        val x = list[position]

        holder.itemView.message_list_name.text = x.fullName
        holder.itemView.message_list_content.text = x.lastMessage.message.text

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("Africa/Cairo")
        val date = inputFormat.parse(x.lastMessage.createdAt)
        val formattedTime = outputFormat.format(date!!)

        holder.itemView.message_list_time.text = formattedTime

        Glide
            .with(holder.itemView)
            .load(x.image.url)
            .into(holder.itemView.image_chat_list)
    }

}