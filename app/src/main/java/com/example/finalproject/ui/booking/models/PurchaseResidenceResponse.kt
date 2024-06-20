package com.example.finalproject.ui.booking.models

data class PurchaseResidenceResponse(
    val count: Int,
    val residences: List<ResidenceX>,
    val status: String
)