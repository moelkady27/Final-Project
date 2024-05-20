package com.example.finalproject.ui.home.models

data class GetNearestResidencesResponse(
    val count: Int,
    val residences: List<ResidenceX>,
    val status: String
)