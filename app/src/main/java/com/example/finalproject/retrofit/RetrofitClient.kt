package com.example.finalproject.retrofit

import com.example.finalproject.ui.recommendation.models.Location
import com.example.finalproject.ui.recommendation.models.LocationDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

//    private const val BASE_URL = "https://graduation-8fhh.onrender.com/"

//    private const val BASE_URL = "https://grad-test.onrender.com/"

    private const val BASE_URL = "https://home-finder-back-end-i7ca.onrender.com/"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Location::class.java, LocationDeserializer())
        .create()

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder().build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}