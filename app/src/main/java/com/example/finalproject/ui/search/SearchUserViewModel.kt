package com.example.finalproject.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserViewModel : ViewModel() {

    private val _searchLiveData = MutableLiveData<List<User>>()
    val searchLiveData: LiveData<List<User>> = _searchLiveData

    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun search(token: String, search: String) {
        RetrofitClient.instance.search("Bearer $token", search)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        _searchLiveData.value = response.body()?.users
                    } else {
                        errorLiveData.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    errorLiveData.value = t.message
                }
            })
    }

//    fun observeChatUsersLiveData() : LiveData<List<User>> {
//        return searchLiveData
//    }
}