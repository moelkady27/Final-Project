package com.example.finalproject.ui.chat.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.socket.SocketHandler
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.chat.adapter.ChattingAdapter
import com.example.finalproject.ui.chat.db.ChatDatabase
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
import com.example.finalproject.ui.chat.viewModels.ChattingViewModelFactory
import kotlinx.android.synthetic.main.activity_chat.et_type_a_messages
import kotlinx.android.synthetic.main.activity_chat.floatingActionButtonOnlineOnChat
import kotlinx.android.synthetic.main.activity_chat.iv_send
import kotlinx.android.synthetic.main.activity_chat.iv_user_chat
import kotlinx.android.synthetic.main.activity_chat.toolbar_chat
import kotlinx.android.synthetic.main.activity_chat.tv_user_name_chat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ChattingAdapter

    private lateinit var chatViewModel: ChattingViewModel

    private lateinit var socketHandler: SocketHandler

    private lateinit var chatDatabase: ChatDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatDatabase = ChatDatabase.getInstance(this)

        val factory = ChattingViewModelFactory(chatDatabase)
        chatViewModel = ViewModelProvider(this, factory).get(ChattingViewModel::class.java)

        socketHandler = SocketHandler(this@ChatActivity)

        val token = AppReferences.getToken(this@ChatActivity)

        setUpRecyclerView()

        observeChatMessages()

        val userFullName = intent.getStringExtra("ChatUserFullName")
        val userImage = intent.getStringExtra("ChatUserImage")
        val receiverId = intent.getStringExtra("ReceiverId").toString()
        val senderId = intent.getStringExtra("SenderId").toString()
        Log.e("ReceiverId is ", receiverId)
        Log.e("SenderId is ", senderId)

        tv_user_name_chat.text = userFullName

        Glide
            .with(this@ChatActivity)
            .load(userImage)
            .into(iv_user_chat)

        chatViewModel.getConversation(token, receiverId)

        observeGetConversation()

        iv_send.setOnClickListener {
            val messageContent = et_type_a_messages.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                chatViewModel.sendMessage(token, receiverId, messageContent)
                et_type_a_messages.text?.clear()

                // Send message with socket
                socketHandler.sendMessage(receiverId, messageContent)

            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        socketHandler.connect { isConnected ->
            if (isConnected) {
                socketHandler.on("newMessage") { args ->
                    val data = args["newMessage"] as? JSONObject
                    Log.d("Data object", data.toString())

                    data?.let {
                        val messageChatting = MessageChatting(
                            _id = it.optString("_id"),
                            createdAt = it.optString("createdAt"),
                            media = emptyList(),
                            messageContent = it.optString("messageContent", ""),
                            receiverId = it.optString("receiverId"),
                            senderId = it.optString("senderId"),
                            updatedAt = it.optString("updatedAt")
                        )
                        runOnUiThread {
                            adapter.addReceivedMessage(messageChatting, receiverId)
                            recyclerView.scrollToPosition(adapter.itemCount - 1)
                        }
                    }
                }

                socketHandler.on("messageDeleted") { args ->
                    val messageId = args.getString("_id")
                    adapter.removeMessageById(messageId)
                }

                socketHandler.on("messageEdited") { args ->
                    Log.e("args" , args.toString())
                    try {
                        val messageId = args.getString("_id")
                        val editedMessage = args.optString("messageContent" , "")

                        runOnUiThread {
                            adapter.editMessageById(messageId, editedMessage)
                        }
                    } catch (e: JSONException) {
                        Log.e("Socket", "JSONException: ${e.message}")
                    }
                }

                socketHandler.onOnline("getOnlineUsers") { data ->
                    try {
                        val onlineUserIds = HashSet<String>()
                        if (data is JSONArray) {
                            for (i in 0 until data.length()) {
                                val userId = data.getString(i)
                                onlineUserIds.add(userId)
                            }
                            runOnUiThread {
                                if (onlineUserIds.contains(receiverId)) {
                                    floatingActionButtonOnlineOnChat.visibility = View.VISIBLE
                                } else {
                                    floatingActionButtonOnlineOnChat.visibility = View.GONE
                                }
                            }
                        } else {
                            Log.e("Socket", "Received data is not JSONArray")
                        }
                    } catch (e: JSONException) {
                        Log.e("Socket", "Error handling event data: $e")
                    }
                }


            } else {
                Log.e("Socket", "Socket connection failed")
            }
        }

        setUpActionBar()

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        socketHandler.disconnect()
//    }

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
        recyclerView = findViewById(R.id.rv_chat)
        adapter = ChattingAdapter(this@ChatActivity, chatViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeChatMessages() {
        recyclerView = findViewById(R.id.rv_chat)
        chatViewModel.observeChattingLiveData().observe(this, Observer { messages ->
            adapter.setMessageChattingList(messages)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun observeGetConversation() {
        recyclerView = findViewById(R.id.rv_chat)
        chatViewModel.observeGetConversationLiveData().observe(this, Observer { messages ->
            adapter.setMessagesList(messages)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

}
