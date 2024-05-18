package com.example.finalproject.ui.profile.models

data class PendingResponse(
    val count: Int,
    val residence: List<Residence>,
    val status: String
)