package com.example.finalproject.ui.chat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.socket.SocketHandler
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.chat.adapter.ChatListAdapter
import com.example.finalproject.ui.chat.viewModels.ChatListUsersViewModel
import kotlinx.android.synthetic.main.activity_chat_list.toolbar_message
import org.json.JSONArray
import org.json.JSONException

class ChatListUsersActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ChatListAdapter

    private lateinit var chatListUsersViewModel: ChatListUsersViewModel

    private lateinit var socketHandler: SocketHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

        chatListUsersViewModel = ViewModelProvider(this@ChatListUsersActivity).get(ChatListUsersViewModel::class.java)

        chatListUsersViewModel.getChatUsers(AppReferences.getToken(this@ChatListUsersActivity))

        observeChatUsers()

        getRecycleView()

        setupActionBar()

        socketHandler = SocketHandler(this@ChatListUsersActivity)

        socketHandler.connect { isConnected ->
            if (isConnected) {
                Log.e("Socket", "Socket connected successfully")

                socketHandler.onOnline("getOnlineUsers") { data ->
                    try {
                        val onlineUserIds = HashSet<String>()
                        if (data is JSONArray) {
                            for (i in 0 until data.length()) {
                                val userId = data.getString(i)
                                onlineUserIds.add(userId)
                            }
                            runOnUiThread {
                                adapter.updateOnlineUsers(onlineUserIds)
                            }
                        } else {
                            Log.e("Socket", "Received data is not JSONArray")
                        }
                    } catch (e: JSONException) {
                        Log.e("Socket", "Error handling event data: $e")
                    }
                }

                socketHandler.setDisconnectCallback {
                    runOnUiThread {
                        adapter.removeOfflineUsers(adapter.onlineUsers)
                    }
                }

            } else {
                Log.e("Socket", "Socket connection failed")
            }
        }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun observeChatUsers() {
        chatListUsersViewModel.observeChatUsersLiveData().observe(this@ChatListUsersActivity , Observer { chatList->
            adapter.setChatUserList(chatList)
            adapter.notifyDataSetChanged()
        })
    }

}