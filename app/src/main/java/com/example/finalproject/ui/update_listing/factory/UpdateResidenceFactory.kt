package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.UpdateResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.UpdateResidenceViewModel

class UpdateResidenceFactory(
    private val updateResidenceRepository: UpdateResidenceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateResidenceViewModel::class.java)){
            return UpdateResidenceViewModel(updateResidenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}