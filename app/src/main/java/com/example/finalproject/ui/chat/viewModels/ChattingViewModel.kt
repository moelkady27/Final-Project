package com.example.finalproject.ui.chat.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.chat.models.ChattingResponse
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.request.SendMessageRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingViewModel: ViewModel() {

    private val chattingResponseLiveData = MutableLiveData<List<MessageChatting>>()
    private val errorLiveData = MutableLiveData<String>()

    fun sendMessage(token: String, receiverId: String, messageContent: String) {
        val data = SendMessageRequest(messageContent)

        RetrofitClient.instance.sendMessage("Bearer $token", receiverId, data)
            .enqueue(object : Callback<ChattingResponse> {
                override fun onResponse(
                    call: Call<ChattingResponse>,
                    response: Response<ChattingResponse>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        message?.let {
                            chattingResponseLiveData.value = listOf(it)
                        }
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ChattingResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }

    fun observeChattingLiveData(): LiveData<List<MessageChatting>> {
        return chattingResponseLiveData
    }
}
