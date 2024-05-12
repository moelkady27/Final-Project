package com.example.finalproject.ui.add_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.add_listing.repository.SetLocationResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.SetLocationResidenceViewModel
import com.example.finalproject.ui.complete_register.repository.SetLocationRepository
import com.example.finalproject.ui.complete_register.viewModels.SetLocationViewModel

class LocationResidenceFactory(
    private val setLocationResidenceRepository: SetLocationResidenceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetLocationResidenceViewModel::class.java)) {
            return SetLocationResidenceViewModel(setLocationResidenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}