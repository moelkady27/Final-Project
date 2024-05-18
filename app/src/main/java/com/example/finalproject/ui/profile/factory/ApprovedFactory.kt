package com.example.finalproject.ui.profile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.profile.repository.ApprovedRepository
import com.example.finalproject.ui.profile.viewModels.ApprovedViewModel

class ApprovedFactory(
    private val approvedRepository: ApprovedRepository

): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApprovedViewModel::class.java)){
            return ApprovedViewModel(approvedRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}