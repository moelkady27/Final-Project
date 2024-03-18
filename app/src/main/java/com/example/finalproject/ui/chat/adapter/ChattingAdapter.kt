package com.example.finalproject.ui.chat.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.models.MessageConversation
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.my_message.view.*
import kotlinx.android.synthetic.main.other_message.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingAdapter(
    private val context: Context,
    private val viewModel: ChattingViewModel

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messagesList: List<MessageConversation> = ArrayList()
    private var messageChattingList: List<MessageChatting> = ArrayList()

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_OTHER_MESSAGE = 1
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setMessagesList(messagesList: List<MessageConversation>) {
        this.messagesList = messagesList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMessageChattingList(messageChattingList: List<MessageChatting>) {
        this.messageChattingList += messageChattingList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addReceivedMessage(message: MessageChatting) {
        messageChattingList = messageChattingList.toMutableList().apply {
            add(message)
        }
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
            if (messagesList[position].senderId == AppReferences.getUserId(context as Activity)) {
                VIEW_TYPE_MY_MESSAGE
            } else {
                VIEW_TYPE_OTHER_MESSAGE
            }
        } else {
            if (messageChattingList[position - messagesList.size].senderId == AppReferences.getUserId(context as Activity)) {
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
                    holder.itemView.setOnClickListener {
                        showMessageOptionsDialog(message)
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

    private fun showMessageOptionsDialog(message: MessageConversation) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Message Options")
            .setItems(arrayOf("Delete", "Edit")) { _, which ->
                when (which) {
                    0 -> showDeleteMessageDialog(message)
                    1 -> showEditMessageDialog(message)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showDeleteMessageDialog(message: MessageConversation) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { _, _ ->
                val token = AppReferences.getToken(context)
                val messageId = message._id

                viewModel.deleteMessage(token , messageId)
                removeMessageById(messageId)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun removeMessageById(messageId: String) {
        val position = messagesList.indexOfFirst { it._id == messageId }
        if (position != -1) {
            (messagesList as MutableList).removeAt(position)
            (context as Activity).runOnUiThread {
                notifyItemRemoved(position)
            }
        }
    }

    private fun showEditMessageDialog(message: MessageConversation) {
        val builder = AlertDialog.Builder(context)
        val editMessage = TextInputEditText(context)
        editMessage.setText(message.message.text)
        builder.setTitle("Edit Message")
            .setView(editMessage)
            .setPositiveButton("Save") { _, _ ->
                val editedMessage = editMessage.text.toString().trim()
                if (editedMessage.isNotEmpty()) {
                    val token = AppReferences.getToken(context)
                    val messageId = message._id

                    viewModel.editMessage(token, messageId, editedMessage)

                    editMessageById(messageId, editedMessage)

                } else {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun editMessageById(messageId: String, editedMessage: String) {
        val position = messagesList.indexOfFirst { it._id == messageId }
        if (position != -1) {
            messagesList[position].message.text = editedMessage
            val token = AppReferences.getToken(context)
            viewModel.editMessage(token, messageId, editedMessage)
            notifyItemChanged(position)
        } else {
            val chatMessagePosition = messageChattingList.indexOfFirst { it._id == messageId }
            if (chatMessagePosition != -1) {
                messageChattingList[chatMessagePosition].message.text = editedMessage
                val token = AppReferences.getToken(context)
                viewModel.editMessage(token, messageId, editedMessage)
                notifyItemChanged(messagesList.size + chatMessagePosition)
            }
        }
    }

}