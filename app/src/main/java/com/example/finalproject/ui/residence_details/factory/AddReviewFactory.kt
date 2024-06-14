package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.AddReviewRepository
import com.example.finalproject.ui.residence_details.viewModel.AddReviewViewModel

class AddReviewFactory(
    private val addReviewRepository: AddReviewRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddReviewViewModel::class.java)){
            return AddReviewViewModel(addReviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}