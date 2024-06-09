package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.DeleteResidenceImageRepository
import com.example.finalproject.ui.update_listing.viewModel.DeleteResidenceImageViewModel

class DeleteResidenceImageFactory(
    private val deleteResidenceImageRepository: DeleteResidenceImageRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteResidenceImageViewModel::class.java)){
            return DeleteResidenceImageViewModel(deleteResidenceImageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}