package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.LikeReviewRepository
import com.example.finalproject.ui.residence_details.viewModel.LikeReviewViewModel

class LikeReviewFactory(
    private val likeReviewRepository: LikeReviewRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikeReviewViewModel::class.java)){
            return LikeReviewViewModel(likeReviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}