package com.example.finalproject.ui.profile.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.profile.repository.GetUserInfoRepository
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel

class GetUserInfoFactory(
    private val getUserInfoRepository: GetUserInfoRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GetUserInfoViewModel::class.java)) {
                return GetUserInfoViewModel(getUserInfoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}