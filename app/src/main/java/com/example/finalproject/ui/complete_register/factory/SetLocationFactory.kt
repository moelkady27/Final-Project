package com.example.finalproject.ui.complete_register.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.complete_register.repository.SetLocationRepository
import com.example.finalproject.ui.complete_register.viewModels.SetLocationViewModel

class SetLocationFactory (
    private val setLocationRepository: SetLocationRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetLocationViewModel::class.java)){
            return SetLocationViewModel(setLocationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}