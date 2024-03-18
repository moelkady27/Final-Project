package com.example.finalproject.ui.chat.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.ui.chat.models.ChattingResponse
import com.example.finalproject.ui.chat.models.DeleteMessageResponse
import com.example.finalproject.ui.chat.models.EditMessageResponse
import com.example.finalproject.ui.chat.models.MessageChatting
import com.example.finalproject.ui.chat.models.GetConversationResponse
import com.example.finalproject.ui.chat.models.MessageConversation
import com.example.finalproject.ui.chat.models.UpdatedMessage
import com.example.finalproject.ui.chat.request.EditMessageRequest
import com.example.finalproject.ui.chat.request.SendMessageRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingViewModel: ViewModel() {

    private val chattingResponseLiveData = MutableLiveData<List<MessageChatting>>()

    private val getConversationResponseLiveData = MutableLiveData<List<MessageConversation>>()

    private val deleteMessageResponseLiveData: MutableLiveData<DeleteMessageResponse> = MutableLiveData()

    private val editMessageResponseLiveData = MutableLiveData<List<UpdatedMessage>>()

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

    fun getConversation(token: String, receiverId: String) {
        RetrofitClient.instance.getConversation("Bearer $token", receiverId)
            .enqueue(object : Callback<GetConversationResponse> {
                override fun onResponse(
                    call: Call<GetConversationResponse>,
                    response: Response<GetConversationResponse>
                ) {
                    if (response.isSuccessful){
                        val messages = response.body()?.messages?.messages
                        messages?.let {
                            getConversationResponseLiveData.value = it
                        }
                    }
                }

                override fun onFailure(call: Call<GetConversationResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }

    fun deleteMessage(token: String, messageId: String) {
        RetrofitClient.instance.deleteMessage("Bearer $token", messageId)
            .enqueue(object : Callback<DeleteMessageResponse> {
                override fun onResponse(
                    call: Call<DeleteMessageResponse>,
                    response: Response<DeleteMessageResponse>
                ) {
                    if (response.isSuccessful) {
                        deleteMessageResponseLiveData.value = response.body()
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<DeleteMessageResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }

    fun editMessage(token: String, messageId: String, messageContent: String){
        val data = EditMessageRequest(messageContent)

        RetrofitClient.instance.editMessage("Bearer $token", messageId, data)
            .enqueue(object : Callback<EditMessageResponse>{
                override fun onResponse(
                    call: Call<EditMessageResponse>,
                    response: Response<EditMessageResponse>
                ) {
                    if (response.isSuccessful) {
                        val updatedMessage = response.body()?.updatedMessage
                        updatedMessage?.let {
                            editMessageResponseLiveData.value = listOf(it)
                        }
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<EditMessageResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }

            })

    }

    fun observeChattingLiveData(): LiveData<List<MessageChatting>> {
        return chattingResponseLiveData
    }

    fun observeGetConversationLiveData(): LiveData<List<MessageConversation>> {
        return getConversationResponseLiveData
    }

    fun observeEditMessageLiveData(): LiveData<List<UpdatedMessage>> {
        return editMessageResponseLiveData
    }
}
