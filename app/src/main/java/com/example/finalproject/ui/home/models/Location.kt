package com.example.finalproject.ui.home.models

data class Location(
    val coordinates: List<Double>,
    val type: String,
    val fullAddress: String,
    val city: String,
    val state: String,
    val country: String
)