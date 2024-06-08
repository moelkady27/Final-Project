package com.example.finalproject.ui.update_listing.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel

class GetResidenceFactory(
    private val getResidenceRepository: GetResidenceRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetResidenceViewModel::class.java)){
            return GetResidenceViewModel(getResidenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}