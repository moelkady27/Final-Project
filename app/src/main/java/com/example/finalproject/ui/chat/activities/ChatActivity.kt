package com.example.finalproject.ui.chat.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.socket.SocketHandler
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.chat.adapter.ChattingAdapter
import com.example.finalproject.ui.chat.db.ChatDatabase
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.viewModels.ChattingViewModel
import com.example.finalproject.ui.chat.viewModels.ChattingViewModelFactory
import kotlinx.android.synthetic.main.activity_chat.et_type_a_messages
import kotlinx.android.synthetic.main.activity_chat.floatingActionButtonOnlineOnChat
import kotlinx.android.synthetic.main.activity_chat.iv_gallery_chat
import kotlinx.android.synthetic.main.activity_chat.iv_send
import kotlinx.android.synthetic.main.activity_chat.iv_user_chat
import kotlinx.android.synthetic.main.activity_chat.toolbar_chat
import kotlinx.android.synthetic.main.activity_chat.tv_user_name_chat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class ChatActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ChattingAdapter

    private lateinit var chatViewModel: ChattingViewModel

    private lateinit var socketHandler: SocketHandler

    private lateinit var chatDatabase: ChatDatabase

    private lateinit var selectedImage: String

    private lateinit var networkUtils: NetworkUtils

    companion object {
        private var REQUEST_CODE_GALLERY = 1
    }

    private val pickImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImage = getPathFromUri(uri)
                    Log.e("image", selectedImage)
                    sendImage()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        networkUtils = NetworkUtils(this)

        chatDatabase = ChatDatabase.getInstance(this)

        val factory = ChattingViewModelFactory(chatDatabase)
        chatViewModel = ViewModelProvider(this, factory).get(ChattingViewModel::class.java)

        socketHandler = SocketHandler(this@ChatActivity)

        val token = AppReferences.getToken(this@ChatActivity)

        setUpActionBar()

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
            if (networkUtils.isNetworkAvailable()){
                val messageContent = et_type_a_messages.text.toString().trim()
                if (messageContent.isNotEmpty()) {
                    chatViewModel.sendMessage(token, receiverId, messageContent)
                    et_type_a_messages.text?.clear()

                    socketHandler.sendMessage(receiverId, messageContent)

                } else {
                    Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                showErrorSnackBar("No internet connection", true)
            }
        }

        iv_gallery_chat.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(this@ChatActivity,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this@ChatActivity,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
//                return@setOnClickListener
//            }
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            pickImageFromGallery.launch(galleryIntent)

            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            pickImageFromGallery.launch(galleryIntent)
        }

        socketHandler.connect { isConnected ->
            if (isConnected) {
                socketHandler.on("newMessage") { args ->
                    val data = args["newMessage"] as? JSONObject
                    Log.d("Data object", data.toString())

                    data?.let {

                        val mediaList = mutableListOf<String>()
                        val mediaArray = it.optJSONArray("media")
                        mediaArray?.let { array ->
                            for (i in 0 until array.length()) {
                                val imageUrl = array.optString(i)
                                imageUrl?.let { url ->
                                    mediaList.add(url)
                                }
                            }
                        }

                        val messageChatting = MessageChatting(
                            _id = it.optString("_id"),
                            createdAt = it.optString("createdAt"),
                            media = mediaList,
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
        recyclerView = findViewById(R.id.rv_chat)
        adapter = ChattingAdapter(this@ChatActivity, chatViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

//                    *** adding new message to list ***
    private fun observeChatMessages() {
        recyclerView = findViewById(R.id.rv_chat)
        chatViewModel.observeChattingLiveData().observe(this, Observer { messages ->
            adapter.setMessageChattingList(messages)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

//                    *** get all messages in conversation ***
    private fun observeGetConversation() {
        recyclerView = findViewById(R.id.rv_chat)
        chatViewModel.observeGetConversationLiveData().observe(this, Observer { messages ->
            adapter.setMessagesList(messages)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor.getString(it) } ?: ""
        cursor?.close()
        return path
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageFromGallery.launch(galleryIntent)
            } else {
                Toast.makeText(this@ChatActivity, "Storage permission is required to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendImage() {
        val token = AppReferences.getToken(this@ChatActivity)
        val receiverId = intent.getStringExtra("ReceiverId").toString()
            val file = File(selectedImage)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("images", file.name, requestFile)
            chatViewModel.sendImage(token, receiverId, listOf(body))
    }

}
