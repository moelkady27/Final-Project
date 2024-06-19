package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.RemoveUnLikeRepository
import com.example.finalproject.ui.residence_details.viewModel.RemoveUnLikeViewModel

class RemoveUnLikeFactory(
    private val removeUnLikeRepository: RemoveUnLikeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveUnLikeViewModel::class.java)){
            return RemoveUnLikeViewModel(removeUnLikeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}