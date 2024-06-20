package com.example.finalproject.ui.add_listing.models

data class UpdatePriceResponse(
    val residenceId: String,
    val salePrice: Int,
    val status: String
)