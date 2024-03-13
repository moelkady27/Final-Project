package com.example.finalproject.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.chat.models.MessageChatting
import kotlinx.android.synthetic.main.my_message.view.txtMyMessage
import kotlinx.android.synthetic.main.my_message.view.txtMyMessageTime
import kotlinx.android.synthetic.main.other_message.view.txtOtherMessage
import kotlinx.android.synthetic.main.other_message.view.txtOtherMessageTime
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChattingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<MessageChatting> = ArrayList()

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_OTHER_MESSAGE = 1
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setMessageList(messageList: List<MessageChatting>) {
        this.list = messageList
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
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        return if (message.senderId == "currentUserId") {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val x = list[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                holder.itemView.txtMyMessage.text = x.message.text

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                outputFormat.timeZone = TimeZone.getTimeZone("Africa/Cairo")
                val date = inputFormat.parse(x.createdAt)
                val formattedTime = outputFormat.format(date!!)

                holder.itemView.txtMyMessageTime.text = formattedTime
            }
            VIEW_TYPE_OTHER_MESSAGE -> {
                holder.itemView.txtOtherMessage.text = x.message.text

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                outputFormat.timeZone = TimeZone.getTimeZone("Africa/Cairo")
                val date = inputFormat.parse(x.createdAt)
                val formattedTime = outputFormat.format(date!!)

                holder.itemView.txtOtherMessageTime.text = formattedTime
            }
        }
    }
}