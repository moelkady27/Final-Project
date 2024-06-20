package com.example.finalproject.ui.residence_details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.residence_details.repository.PredictPriceRepository
import com.example.finalproject.ui.residence_details.viewModel.PredictPriceViewModel

class PredictPriceFactory(
    private val predictPriceRepository: PredictPriceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredictPriceViewModel::class.java)){
            return PredictPriceViewModel(predictPriceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}