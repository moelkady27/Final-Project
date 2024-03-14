package com.example.finalproject.ui.chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.models.lol.Message
import kotlinx.android.synthetic.main.my_message.view.*
import kotlinx.android.synthetic.main.other_message.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messagesList: List<Message> = ArrayList()
    private var messageChattingList: List<MessageChatting> = ArrayList()

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_OTHER_MESSAGE = 1
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setMessagesList(messagesList: List<Message>) {
        this.messagesList = messagesList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMessageChattingList(messageChattingList: List<MessageChatting>) {
        this.messageChattingList = messageChattingList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.my_message,
                    parent,
                    false
                )
                MyViewHolder(view)
            }
            VIEW_TYPE_OTHER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.other_message,
                    parent,
                    false
                )
                OtherViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size + messageChattingList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < messagesList.size) {
            if (messagesList[position].senderId == "currentUserId") {
                VIEW_TYPE_MY_MESSAGE
            } else {
                VIEW_TYPE_OTHER_MESSAGE
            }
        } else {
            if (messageChattingList[position - messagesList.size].senderId == "currentUserId") {
                VIEW_TYPE_MY_MESSAGE
            } else {
                VIEW_TYPE_OTHER_MESSAGE
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < messagesList.size) {
            val message = messagesList[position]
            when (holder.itemViewType) {
                VIEW_TYPE_MY_MESSAGE -> {
                    holder.itemView.apply {
                        txtMyMessage.text = message.message.text
                        txtMyMessageTime.text = formatTime(message.createdAt)
                    }
                }
                VIEW_TYPE_OTHER_MESSAGE -> {
                    holder.itemView.apply {
                        txtOtherMessage.text = message.message.text
                        txtOtherMessageTime.text = formatTime(message.createdAt)
                    }
                }
            }
        } else {
            val message = messageChattingList[position - messagesList.size]
            when (holder.itemViewType) {
                VIEW_TYPE_MY_MESSAGE -> {
                    holder.itemView.apply {
                        txtMyMessage.text = message.message.text
                        txtMyMessageTime.text = formatTime(message.createdAt)
                    }
                }
                VIEW_TYPE_OTHER_MESSAGE -> {
                    holder.itemView.apply {
                        txtOtherMessage.text = message.message.text
                        txtOtherMessageTime.text = formatTime(message.createdAt)
                    }
                }
            }
        }
    }

    private fun formatTime(time: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("Africa/Cairo")
        val date = inputFormat.parse(time)
        return outputFormat.format(date!!)
    }
}
