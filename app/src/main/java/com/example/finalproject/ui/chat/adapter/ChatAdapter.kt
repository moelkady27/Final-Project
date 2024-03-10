package com.example.finalproject.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import kotlinx.android.synthetic.main.my_message.view.txtMyMessage
import kotlinx.android.synthetic.main.my_message.view.txtMyMessageTime
import kotlinx.android.synthetic.main.other_message.view.txtOtherMessage
import kotlinx.android.synthetic.main.other_message.view.txtOtherMessageTime
import kotlinx.android.synthetic.main.other_message.view.txtOtherUser

private val VIEW_TYPE_MY_MESSAGE = 1
private val VIEW_TYPE_OTHER_MESSAGE = 2


class ChatAdapter(): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
//        return if(viewType == VIEW_TYPE_MY_MESSAGE) {
//            MyMessageViewHolder(
//                LayoutInflater.from(parent.context).inflate(R.layout.my_message, parent, false)
//            )
//        } else {
//            OtherMessageViewHolder(
//                LayoutInflater.from(parent.context).inflate(R.layout.other_message, parent, false)
//            )
//        }

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.my_message ,
            parent ,
            false
        )
        return ChatAdapter.ChatViewHolder(view)

    }

    override fun getItemCount(): Int {
        return 25
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

    }


    inner class MyMessageViewHolder (view: View) : ChatViewHolder(view) {
        private var messageText: TextView = view.txtMyMessage
        private var timeText: TextView = view.txtMyMessageTime
    }

    inner class OtherMessageViewHolder (view: View) : ChatViewHolder(view) {
        private var messageText: TextView = view.txtOtherMessage
        private var userText: TextView = view.txtOtherUser
        private var timeText: TextView = view.txtOtherMessageTime
    }

    open class ChatViewHolder(view : View) : RecyclerView.ViewHolder(view)


}

