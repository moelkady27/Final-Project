package com.example.finalproject.ui.password.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.password.repository.ForgotPasswordRepository
import com.example.finalproject.ui.password.viewModels.ForgotPasswordViewModel

class ForgotPasswordFactory(
    private val forgotPasswordRepository: ForgotPasswordRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)){
            return ForgotPasswordViewModel(forgotPasswordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}