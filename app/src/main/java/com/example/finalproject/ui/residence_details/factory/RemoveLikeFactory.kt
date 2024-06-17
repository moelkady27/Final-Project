package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.RemoveLikeRepository
import com.example.finalproject.ui.residence_details.viewModel.RemoveLikeViewModel

class RemoveLikeFactory(
    private val removeLikeRepository: RemoveLikeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveLikeViewModel::class.java)){
            return RemoveLikeViewModel(removeLikeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}