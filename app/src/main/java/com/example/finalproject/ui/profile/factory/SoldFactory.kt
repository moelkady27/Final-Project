package com.example.finalproject.ui.profile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.profile.repository.SoldRepository
import com.example.finalproject.ui.profile.viewModels.SoldViewModel

class SoldFactory(
    private val soldRepository: SoldRepository

): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoldViewModel::class.java)){
            return SoldViewModel(soldRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}