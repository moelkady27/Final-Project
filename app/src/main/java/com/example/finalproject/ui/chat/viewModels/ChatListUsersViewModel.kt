package com.example.finalproject.ui.chat.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.chat.models.ChatListUsersResponse
import com.example.finalproject.ui.chat.models.ChatUser
import com.example.finalproject.ui.chat.models.SearchUsersResponse
import com.example.finalproject.ui.chat.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListUsersViewModel: ViewModel() {

    val chatUsersLiveData = MutableLiveData<List<ChatUser>>()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    val searchUsersResponseLiveData = MutableLiveData<List<User>>()

    fun getChatUsers(token: String){
        RetrofitClient.instance.getChatUsers("Bearer $token")
            .enqueue(object : Callback<ChatListUsersResponse>{
                override fun onResponse(
                    call: Call<ChatListUsersResponse>,
                    response: Response<ChatListUsersResponse>
                ) {
                    if (response.isSuccessful){
                        chatUsersLiveData.value = response.body()?.chatUsers
                    }
                    else{
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ChatListUsersResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

    fun searchUsers(token: String, search: String){
        RetrofitClient.instance.searchUsers("Bearer $token", search)
            .enqueue(object : Callback<SearchUsersResponse>{
                override fun onResponse(
                    call: Call<SearchUsersResponse>,
                    response: Response<SearchUsersResponse>
                ) {
                    if (response.isSuccessful){
                        searchUsersResponseLiveData.value = response.body()?.users
                    }
                    else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SearchUsersResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })
    }

    fun observeChatUsersLiveData() : LiveData<List<ChatUser>> {
        return chatUsersLiveData
    }

    fun observeSearchUsersLiveData() : LiveData<List<User>> {
        return searchUsersResponseLiveData
    }

}