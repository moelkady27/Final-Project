package com.example.finalproject.ui.booking.models

data class LocationX(
    val city: String,
    val coordinates: List<Double>,
    val country: String,
    val fullAddress: String,
    val state: String,
    val type: String
)