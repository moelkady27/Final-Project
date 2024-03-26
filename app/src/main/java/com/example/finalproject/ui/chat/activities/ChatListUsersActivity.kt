package com.example.finalproject.ui.chat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.socket.SocketHandler
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.chat.adapter.ChatListAdapter
import com.example.finalproject.ui.chat.db.ChatUsersDatabase
import com.example.finalproject.ui.chat.db.MessageConversationDatabase
import com.example.finalproject.ui.chat.models.ChatUser
import com.example.finalproject.ui.chat.models.Image
import com.example.finalproject.ui.chat.models.LastMessage
import com.example.finalproject.ui.chat.models.Message
import com.example.finalproject.ui.chat.viewModels.ChatListUsersViewModel
import com.example.finalproject.ui.chat.viewModels.ChatListUsersViewModelFactory
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
import com.example.finalproject.ui.chat.viewModels.MessageConversationViewModeFactory
import kotlinx.android.synthetic.main.activity_chat_list.sv_user_chat_list
import kotlinx.android.synthetic.main.activity_chat_list.toolbar_message
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ChatListUsersActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ChatListAdapter

    private lateinit var chatListUsersViewModel: ChatListUsersViewModel

    private lateinit var socketHandler: SocketHandler

    private lateinit var chatUsersDatabase: ChatUsersDatabase


//    private lateinit var chattingViewModel: ChattingViewModel
//
//    private lateinit var messageConversationDatabase: MessageConversationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

//        chatListUsersViewModel = ViewModelProvider(this@ChatListUsersActivity).get(ChatListUsersViewModel::class.java)

        chatUsersDatabase = ChatUsersDatabase.getInstance(this)

        val factory = ChatListUsersViewModelFactory(chatUsersDatabase)
        chatListUsersViewModel = ViewModelProvider(this, factory).get(ChatListUsersViewModel::class.java)

        chatListUsersViewModel.getChatUsers(AppReferences.getToken(this@ChatListUsersActivity))

//        messageConversationDatabase = MessageConversationDatabase.getInstance(this)
//
//        chattingViewModel = ViewModelProvider(this, MessageConversationViewModeFactory(messageConversationDatabase)).get(ChattingViewModel::class.java)

        chatListUsersViewModel.getChatUsers(AppReferences.getToken(this@ChatListUsersActivity))

        observeChatUsers()

        chatListUsersViewModel.observeChatUsersLiveData().observe(this@ChatListUsersActivity, Observer { chatList ->
            if (chatList.isNotEmpty()) {
                chatListUsersViewModel.cacheChatUsers(chatList)
            }
        })

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

                socketHandler.on("newMessage") { args ->
                    val data = args["newMessage"] as JSONObject
                    Log.e("New Message Received", data.toString())

                    val chatUser = ChatUser(
                        _id = data.optString("_id"),
                        fullName = data.optString("fullName"),
                        image = Image(
                            public_id = data.optString("public_id"),
                            url = data.optString("url")
                        ),
                        lastMessage = LastMessage(
                            __v = data.optInt("__v"),
                            _id = data.optString("_id"),
                            createdAt = data.optString("createdAt"),
                            message = Message(
                                text = data.optJSONObject("message")!!.optString("text"),
                                media = emptyList()
                            ),
                            receiverId = data.optString("receiverId"),
                            senderId = data.optString("senderId"),
                            updatedAt = data.optString("updatedAt")
                        ),
                        username = data.optString("username")
                    )

                    runOnUiThread {

                    }
                }

            } else {
                Log.e("Socket", "Socket connection failed")
            }
        }

        sv_user_chat_list.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchForUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchForUsers(newText.orEmpty())
                return true
            }
        })


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

//            chattingViewModel.getCachedConversation(user.lastMessage.senderId, user._id)

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
//    private fun observeChatUsers() {
//        chatListUsersViewModel.observeChatUsersLiveData().observe(this@ChatListUsersActivity, Observer { chatList ->
//            if (chatList.isNotEmpty()) {
//                adapter.setChatUserList(chatList)
//                adapter.notifyDataSetChanged()
////                chatListUsersViewModel.cacheChatUsers(chatList)
//            }
//        })
//    }

    private fun searchForUsers(query: String) {
        if (query.isEmpty()) {
            observeChatUsers()
        } else {
            adapter.filterList(query)
        }
    }


}