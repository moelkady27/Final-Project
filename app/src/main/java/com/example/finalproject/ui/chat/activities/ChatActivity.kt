package com.example.finalproject.ui.chat.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.chat.adapter.ChattingAdapter
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
import kotlinx.android.synthetic.main.activity_chat.et_type_a_messages
import kotlinx.android.synthetic.main.activity_chat.iv_send
import kotlinx.android.synthetic.main.activity_chat.iv_user_chat
import kotlinx.android.synthetic.main.activity_chat.rv_chat
import kotlinx.android.synthetic.main.activity_chat.toolbar_chat
import kotlinx.android.synthetic.main.activity_chat.tv_user_name_chat

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChattingAdapter

    private lateinit var chatViewModel: ChattingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatViewModel = ViewModelProvider(this@ChatActivity).get(ChattingViewModel::class.java)

        val token = AppReferences.getToken(this@ChatActivity)

        setUpRecyclerView()
        observeChatMessages()

        val userFullName = intent.getStringExtra("ChatUserFullName")
        val userImage = intent.getStringExtra("ChatUserImage")
        val receiverId = intent.getStringExtra("ReceiverId").toString()
        val senderId = intent.getStringExtra("SenderId").toString()
        Log.e("ReceiverId is " , receiverId)
        Log.e("SenderId is " , senderId)

        tv_user_name_chat.text = userFullName

        Glide
            .with(this@ChatActivity)
            .load(userImage)
            .into(iv_user_chat)

        iv_send.setOnClickListener {
            val messageContent = et_type_a_messages.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                chatViewModel.sendMessage(token, receiverId, messageContent)
                et_type_a_messages.text?.clear()
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        setUpActionBar()

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_chat)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_chat.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpRecyclerView() {
        adapter = ChattingAdapter()
        rv_chat.layoutManager = LinearLayoutManager(this)
        rv_chat.adapter = adapter
    }

    private fun observeChatMessages() {
        chatViewModel.observeChattingLiveData().observe(this, Observer { messages ->
            adapter.setMessageList(messages)
            rv_chat.scrollToPosition(messages.size - 1)
        })
    }
}