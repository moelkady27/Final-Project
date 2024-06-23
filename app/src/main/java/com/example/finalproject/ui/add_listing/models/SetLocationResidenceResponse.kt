package com.example.finalproject.ui.add_listing.models

data class SetLocationResidenceResponse(
    val residenceId: String,
    val Id: String,
    val location: Location,
    val status: String
)