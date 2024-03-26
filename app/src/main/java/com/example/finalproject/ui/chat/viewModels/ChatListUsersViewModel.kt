package com.example.finalproject.ui.chat.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.chat.db.ChatUsersDatabase
import com.example.finalproject.ui.chat.models.ChatListUsersResponse
import com.example.finalproject.ui.chat.models.ChatUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListUsersViewModel(
    private val chatUsersDatabase: ChatUsersDatabase
): ViewModel() {

    val chatUsersLiveData = MutableLiveData<List<ChatUser>>()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getChatUsers(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatUsers = chatUsersDatabase.chatUsersDao().getChatUsers()

            Log.e("CachedMessages", "Retrieved ${chatUsers.size} chatUsers from cache")

            if (chatUsers.isNotEmpty()) {
                chatUsersLiveData.postValue(chatUsers)
            }

            RetrofitClient.instance.getChatUsers("Bearer $token")
                .enqueue(object : Callback<ChatListUsersResponse> {
                    override fun onResponse(
                        call: Call<ChatListUsersResponse>,
                        response: Response<ChatListUsersResponse>
                    ) {
                        if (response.isSuccessful) {
                            chatUsersLiveData.value = response.body()?.chatUsers
                        } else {
                            errorLiveData.value = response.errorBody()?.string()
                        }
                    }

                    override fun onFailure(call: Call<ChatListUsersResponse>, t: Throwable) {
                        errorLiveData.value = t.message
                    }

                })
        }
    }

    fun observeChatUsersLiveData() : LiveData<List<ChatUser>> {
        return chatUsersLiveData
    }

    fun cacheChatUsers(chatUsers: List<ChatUser>) {
        viewModelScope.launch(Dispatchers.IO) {
            chatUsersDatabase.chatUsersDao().insertChatUsers(chatUsers)
        }
    }

}