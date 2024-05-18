package com.example.finalproject.ui.home.models

data class Location(
    val city: String,
    val coordinates: List<Double>,
    val country: String,
    val fullAddress: String,
    val state: String,
    val type: String
)