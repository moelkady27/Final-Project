package com.example.finalproject.ui.register.models

data class LocationSignIn(
    val city: String,
    val country: String,
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val state: String
)