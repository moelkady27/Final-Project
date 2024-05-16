package com.example.finalproject.ui.add_listing.models

data class UploadPhotoResidenceResponse(
    val residenceId: String,
    val images: List<Image>,
    val status: String
)