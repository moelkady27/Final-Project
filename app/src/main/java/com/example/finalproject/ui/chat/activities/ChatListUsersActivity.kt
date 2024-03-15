package com.example.finalproject.ui.chat.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.chat.adapter.ChatListAdapter
import com.example.finalproject.ui.chat.viewModels.ChatListUsersViewModel
import kotlinx.android.synthetic.main.activity_chat_list.toolbar_message

class ChatListUsersActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatListAdapter

    private lateinit var chatListUsersViewModel: ChatListUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

        chatListUsersViewModel = ViewModelProvider(this@ChatListUsersActivity).get(ChatListUsersViewModel::class.java)

        chatListUsersViewModel.getChatUsers(AppReferences.getToken(this@ChatListUsersActivity))
        observeChatUsers()
        getRecycleView()

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_message)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        toolbar_message.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getRecycleView() {
        recyclerView = findViewById(R.id.recycle_messages)
        adapter = ChatListAdapter{user ->
            val intent = Intent(this@ChatListUsersActivity , ChatActivity::class.java)
            intent.putExtra("ChatUserFullName" , user.fullName)
            intent.putExtra("ChatUserImage" , user.image.url)
            intent.putExtra("ReceiverId" , user._id)
            intent.putExtra("SenderId" , user.lastMessage.senderId)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = adapter
    }

    private fun observeChatUsers() {
        chatListUsersViewModel.observeChatUsersLiveData().observe(this@ChatListUsersActivity , Observer { chatList->
            adapter.setChatUserList(chatList)
            adapter.notifyDataSetChanged()
        })
    }

}