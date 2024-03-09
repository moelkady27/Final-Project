package com.example.finalproject.ui.chat.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.chat.adapter.ChatListAdapter
import com.example.finalproject.ui.notification.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.activity_chat_list.toolbar_message
import kotlinx.android.synthetic.main.activity_notification.toolbar_notification

class ChatListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

        recyclerView = findViewById(R.id.recycle_messages)
        adapter = ChatListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = adapter

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
}