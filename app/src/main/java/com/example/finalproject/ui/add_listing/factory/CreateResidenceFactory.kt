package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.CreateResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.CreateResidenceViewModel

class CreateResidenceFactory(
    private val createResidenceRepository: CreateResidenceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateResidenceViewModel::class.java)){
            return CreateResidenceViewModel(createResidenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}