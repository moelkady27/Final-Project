package com.example.finalproject.ui.setting.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.setting.repository.LogOutRepository
import com.example.finalproject.ui.setting.viewModels.LogOutViewModel

class LogOutFactory(
    private val logOutRepository: LogOutRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogOutViewModel::class.java)){
            return LogOutViewModel(logOutRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}