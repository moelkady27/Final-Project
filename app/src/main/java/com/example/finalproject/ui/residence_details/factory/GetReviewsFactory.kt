package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.GetReviewsRepository
import com.example.finalproject.ui.residence_details.viewModel.GetReviewsViewModel

class GetReviewsFactory(
    private val getReviewsRepository: GetReviewsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetReviewsViewModel::class.java)){
            return GetReviewsViewModel(getReviewsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}