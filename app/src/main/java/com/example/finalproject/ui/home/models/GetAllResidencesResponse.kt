package com.example.finalproject.ui.home.models

data class GetAllResidencesResponse(
    val count: Int,
    val residences: List<Residence>,
    val status: String
)