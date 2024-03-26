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
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.models.Messages
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatViewModel = ViewModelProvider(this@ChatActivity).get(ChattingViewModel::class.java)

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
                Log.e("Socket", "Socket connected")

                socketHandler.on("newMessage") { args ->
                    val data = args["newMessage"] as JSONObject
                    Log.e("New Message Received", data.toString())

                    val messageChatting = MessageChatting(
                        __v = data.optInt("__v"),
                        _id = data.optString("_id"),
                        createdAt = data.optString("createdAt"),
                        message = Messages(
                            text = data.optJSONObject("message")!!.optString("text"),
                            media = emptyList()

                        ),
                        receiverId = data.optString("receiverId"),
                        senderId = data.optString("senderId"),
                        updatedAt = data.optString("updatedAt")
                    )

                    runOnUiThread {
                        adapter.addReceivedMessage(messageChatting, receiverId)
                        recyclerView.scrollToPosition(adapter.itemCount - 1)
                    }
                }

                socketHandler.on("messageDeleted") { args ->
                    val messageId = args.getString("_id")
                    adapter.removeMessageById(messageId)
                }

                socketHandler.on("messageEdited") { args ->
                    try {
                        val messageId = args.getString("_id")
                        val editedMessage = args.getJSONObject("message").optString("text", "")

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
        adapter = ChattingAdapter(this@ChatActivity , chatViewModel)
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
