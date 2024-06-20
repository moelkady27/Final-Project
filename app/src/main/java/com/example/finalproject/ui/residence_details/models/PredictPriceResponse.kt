package com.example.finalproject.ui.residence_details.models

data class PredictPriceResponse(
    val predicted_price: Int,
    val residenceId: String,
    val residence_price: Int,
    val status: String
)