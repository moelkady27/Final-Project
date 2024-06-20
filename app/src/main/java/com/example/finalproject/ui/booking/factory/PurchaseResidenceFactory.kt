package com.example.finalproject.ui.booking.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.booking.repository.PurchaseResidenceRepository
import com.example.finalproject.ui.booking.viewModel.PurchaseResidenceViewModel

class PurchaseResidenceFactory(
    private val purchaseResidenceRepository: PurchaseResidenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchaseResidenceViewModel::class.java)){
            return PurchaseResidenceViewModel(purchaseResidenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}