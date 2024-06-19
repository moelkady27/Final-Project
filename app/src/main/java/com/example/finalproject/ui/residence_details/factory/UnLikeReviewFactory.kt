package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.UnLikeReviewRepository
import com.example.finalproject.ui.residence_details.viewModel.UnLikeReviewViewModel

class UnLikeReviewFactory(
    private val unLikeReviewRepository: UnLikeReviewRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UnLikeReviewViewModel::class.java)){
            return UnLikeReviewViewModel(unLikeReviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}