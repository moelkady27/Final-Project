package com.example.finalproject.ui.chat.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.models.MessageConversation
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.delete_message_dialog.view.btn_cancel_message
import kotlinx.android.synthetic.main.delete_message_dialog.view.btn_delete_message
import kotlinx.android.synthetic.main.edit_message_dialog.view.btn_cancel_message_edit
import kotlinx.android.synthetic.main.edit_message_dialog.view.et_type_a_messages_edit
import kotlinx.android.synthetic.main.edit_message_dialog.view.iv_send_edit
import kotlinx.android.synthetic.main.message_options_dialog.view.ll_delete_message_option
import kotlinx.android.synthetic.main.message_options_dialog.view.ll_edit_message_option
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
    fun addReceivedMessage(message: MessageChatting, currentReceiverId: String) {
        if (message.senderId == currentReceiverId) {
            messageChattingList = messageChattingList.toMutableList().apply {
                add(message)
            }
            notifyDataSetChanged()
        }
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
                        txtMyMessage.text = message.messageContent
                        txtMyMessageTime.text = formatTime(message.createdAt)
                    }
                    holder.itemView.setOnLongClickListener {
                        showMessageOptionsDialog(message)
                        true
                    }
                }
                VIEW_TYPE_OTHER_MESSAGE -> {
                    holder.itemView.apply {
                        txtOtherMessage.text = message.messageContent
                        txtOtherMessageTime.text = formatTime(message.createdAt)
                    }
                }
            }
        } else {
            val message = messageChattingList[position - messagesList.size]
            when (holder.itemViewType) {
                VIEW_TYPE_MY_MESSAGE -> {
                    holder.itemView.apply {
                        txtMyMessage.text = message.messageContent
                        txtMyMessageTime.text = formatTime(message.createdAt)
                    }
                    holder.itemView.setOnLongClickListener {
                        showMessageOptionsDialog(message)
                        true
                    }
                }
                VIEW_TYPE_OTHER_MESSAGE -> {
                    holder.itemView.apply {
                        txtOtherMessage.text = message.messageContent
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

    private fun showMessageOptionsDialog(message: Any) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)

        val messageDialogView =
            LayoutInflater.from(context).inflate(R.layout.message_options_dialog, null)

        messageDialogView.background =
            ContextCompat.getDrawable(context, R.drawable.message_options_dialog_background)

        val editMessageOption = messageDialogView.ll_edit_message_option
        val deleteMessageOption = messageDialogView.ll_delete_message_option

        editMessageOption.setOnClickListener {
            bottomSheetDialog.dismiss()
            showEditMessageDialog(message)
        }

        deleteMessageOption.setOnClickListener {
            bottomSheetDialog.dismiss()
            showDeleteMessageDialog(message)
        }

        bottomSheetDialog.setContentView(messageDialogView)
        bottomSheetDialog.show()
    }

    private fun showDeleteMessageDialog(message: Any) {
        val builder = AlertDialog.Builder(context, R.style.DeleteMessageDialog)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.delete_message_dialog, null)
        dialogView.background =
            ContextCompat.getDrawable(context, R.drawable.delete_message_dialog_background)

        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)

        val btnDelete = dialogView.btn_delete_message
        val btnCancel = dialogView.btn_cancel_message

        btnDelete.setOnClickListener {
            val token = AppReferences.getToken(context)

            val messageId = when (message) {
                is MessageConversation -> message._id
                is MessageChatting -> message._id
                else -> null
            }

            messageId?.let {
                viewModel.deleteMessage(token, it)
                removeMessageById(it)
            }

            alertDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    fun removeMessageById(messageId: String) {
        val conversationPosition = messagesList.indexOfFirst { it._id == messageId }
        if (conversationPosition != -1) {
            (messagesList as MutableList).removeAt(conversationPosition)
            (context as Activity).runOnUiThread {
                notifyItemRemoved(conversationPosition)
            }
        } else {
            val chattingPosition = messageChattingList.indexOfFirst { it._id == messageId }
            if (chattingPosition != -1) {
                (messageChattingList as MutableList).removeAt(chattingPosition)
                (context as Activity).runOnUiThread {
                    notifyItemRemoved(messagesList.size + chattingPosition)
                }
            }
        }
    }


    private fun showEditMessageDialog(message: Any) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val editDialogView =
            LayoutInflater.from(context).inflate(R.layout.edit_message_dialog, null)
        val editMessage = editDialogView.et_type_a_messages_edit

        editDialogView.background =
            ContextCompat.getDrawable(context, R.drawable.message_options_dialog_background)

        when (message) {
            is MessageConversation -> editMessage.setText(message.messageContent)
            is MessageChatting -> editMessage.setText(message.messageContent)
            else -> return
        }

        bottomSheetDialog.setContentView(editDialogView)

        val btnSendEdit = editDialogView.iv_send_edit
        val btnCancelMessageEdit = editDialogView.btn_cancel_message_edit

        btnSendEdit.setOnClickListener {
            val editedMessage = editMessage.text.toString().trim()
            if (editedMessage.isNotEmpty()) {
                val token = AppReferences.getToken(context)

                val messageId = when (message) {
                    is MessageConversation -> message._id
                    is MessageChatting -> message._id
                    else -> null
                }

                messageId?.let {
                    viewModel.editMessage(token, it, editedMessage)
                    editMessageById(it, editedMessage)
                }
            } else {
                editMessage.error = "Edit Message cannot be empty"
                return@setOnClickListener
            }

            bottomSheetDialog.dismiss()
        }

        btnCancelMessageEdit.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    fun editMessageById(messageId: String, editedMessage: String) {
        val position = messagesList.indexOfFirst { it._id == messageId }
        if (position != -1) {
//            messagesList[position].message.text = editedMessage
            messagesList[position].messageContent = editedMessage
            val token = AppReferences.getToken(context)
            viewModel.editMessage(token, messageId, editedMessage)
            notifyItemChanged(position)
        } else {
            val chatMessagePosition = messageChattingList.indexOfFirst { it._id == messageId }
            if (chatMessagePosition != -1) {
//                messageChattingList[chatMessagePosition].message.text = editedMessage
                messageChattingList[chatMessagePosition].messageContent = editedMessage
                val token = AppReferences.getToken(context)
                viewModel.editMessage(token, messageId, editedMessage)
                notifyItemChanged(messagesList.size + chatMessagePosition)
            }
        }
    }

}