package com.example.finalproject.retrofit

import com.example.finalproject.ui.register.models.SignUpResponse
import com.example.finalproject.ui.register.request.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/signup")
    fun signup(
        @Body req: SignUpRequest
    ) : Call<SignUpResponse>


}