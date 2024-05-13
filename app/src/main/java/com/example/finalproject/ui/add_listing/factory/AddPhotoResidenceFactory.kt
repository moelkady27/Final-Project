package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.AddPhotoResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.AddPhotoResidenceViewModel

class AddPhotoResidenceFactory(
    private val addPhotoResidenceRepository: AddPhotoResidenceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPhotoResidenceViewModel::class.java)){
            return AddPhotoResidenceViewModel(addPhotoResidenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}