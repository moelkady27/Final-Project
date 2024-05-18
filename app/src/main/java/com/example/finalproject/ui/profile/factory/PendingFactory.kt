package com.example.finalproject.ui.profile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.profile.repository.PendingRepository
import com.example.finalproject.ui.profile.viewModels.PendingViewModel

class PendingFactory(
    private val pendingRepository: PendingRepository

): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PendingViewModel::class.java)){
            return PendingViewModel(pendingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}