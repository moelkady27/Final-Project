package com.example.finalproject.ui.chat.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.ui.chat.models.ChatUser
import kotlinx.android.synthetic.main.each_row_chat_list.view.floatingActionButtonOnline
import kotlinx.android.synthetic.main.each_row_chat_list.view.image_chat_list
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_content
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_icon
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_name
import kotlinx.android.synthetic.main.each_row_chat_list.view.message_list_time
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatListAdapter(
    private val onItemClick: (ChatUser) -> Unit
): RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {

    private var list : List<ChatUser> = ArrayList()
    private var filteredList: List<ChatUser> = ArrayList()


    var onlineUsers: Set<String> = HashSet()
    private var activeChatUserIds: Set<String> = HashSet()

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    fun setChatUserList(chatUserList: List<ChatUser>){
        this.list = chatUserList
        filterList("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateOnlineUsers(userIds: Set<String>) {
        onlineUsers = userIds
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeOfflineUsers(userIds: Set<String>) {
        onlineUsers = onlineUsers.minus(userIds)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(query: String) {
        filteredList = if (query.trim().isBlank()) {
            list
        } else {
            list.filter { chatUser ->
                chatUser.fullName.toLowerCase(Locale.ROOT)
                    .contains(query.trim().toLowerCase(Locale.ROOT))
            }
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLastMessage(userId: String, lastMessage: String) {
        Log.e("ChatListAdapter", "Updating last message for user: $userId, message: $lastMessage")
        val chatUser = filteredList.find { it._id == userId }
        chatUser?.let {
            it.lastMessage.messageContent = lastMessage
            notifyDataSetChanged()
        }
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
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chatUser = filteredList[position]

        val isOnline = onlineUsers.contains(chatUser._id)
        val isActiveChat = activeChatUserIds.contains(chatUser._id)

        holder.itemView.message_list_name.text = chatUser.fullName

        if (chatUser.lastMessage.media.isNotEmpty()) {
            holder.itemView.message_list_content.setText(R.string.photo_chat)
            holder.itemView.message_list_icon.visibility = View.VISIBLE
        } else {
            holder.itemView.message_list_content.text = chatUser.lastMessage.messageContent
            holder.itemView.message_list_icon.visibility = View.GONE
        }

        val formattedTime = formatTime(chatUser.lastMessage.createdAt)
        holder.itemView.message_list_time.text = formattedTime

        Glide
            .with(holder.itemView)
            .load(chatUser.image.url)
            .into(holder.itemView.image_chat_list)

        if (isOnline || isActiveChat) {
            holder.itemView.floatingActionButtonOnline.visibility = View.VISIBLE
        } else {
            holder.itemView.floatingActionButtonOnline.visibility = View.GONE
        }

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