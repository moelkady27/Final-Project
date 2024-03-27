package com.example.finalproject.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.chat.activities.ChatActivity
import com.example.finalproject.ui.chat.viewModels.ChatListUsersViewModel
import kotlinx.android.synthetic.main.activity_chat_list.toolbar_message
import kotlinx.android.synthetic.main.activity_search_users.recycle_search
import kotlinx.android.synthetic.main.activity_search_users.sv_user_chat_search
import kotlinx.android.synthetic.main.activity_search_users.toolbar_search

class SearchUsersActivity : AppCompatActivity() {

    private lateinit var searchUserViewModel: SearchUserViewModel
    private lateinit var searchUserAdapter: SearchUserAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)

        setupActionBar()

        searchUserViewModel = ViewModelProvider(this).get(SearchUserViewModel::class.java)

        setupRecyclerView()

        observeChatUsers()

        sv_user_chat_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
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

    private fun searchForUsers(query: String) {
        if (query.isEmpty()) {
            observeChatUsers()
        } else {
            val token = AppReferences.getToken(this@SearchUsersActivity)
            searchUserViewModel.search(token, query)
        }
    }

    private fun observeChatUsers() {
        searchUserViewModel.searchLiveData.observe(this@SearchUsersActivity, Observer { userList ->
            searchUserAdapter.setChatUserList(userList)
        })
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycle_search)
        searchUserAdapter = SearchUserAdapter { user ->


            val intent = Intent(this@SearchUsersActivity , ChatActivity::class.java)
            intent.putExtra("ChatUserFullName" , user.fullName)
            intent.putExtra("ChatUserImage" , user.image.url)
            intent.putExtra("ReceiverId" , user._id)
            intent.putExtra("SenderId" , AppReferences.getUserId(this))

            Log.e("ReceiverId", user._id)
            Log.e("SenderId", AppReferences.getUserId(this))

            startActivity(intent)

        }
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = searchUserAdapter

        Log.e("SearchUsersActivity", "Number of items in the list: ${searchUserAdapter.itemCount}")

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_search)

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        toolbar_search.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}