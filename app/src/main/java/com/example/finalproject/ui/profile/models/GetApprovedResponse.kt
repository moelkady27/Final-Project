package com.example.finalproject.ui.profile.models

data class GetApprovedResponse(
    val count: Int,
    val residences: List<ResidenceX>,
    val status: String
)