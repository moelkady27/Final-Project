package com.example.finalproject.ui.password.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.password.repository.ForgotPasswordRepository
import com.example.finalproject.ui.password.repository.ResetPasswordRepository
import com.example.finalproject.ui.password.viewModels.ForgotPasswordViewModel
import com.example.finalproject.ui.password.viewModels.ResetPasswordViewModel

class ResetPasswordFactory(
    private val resetPasswordRepository: ResetPasswordRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)){
            return ResetPasswordViewModel(resetPasswordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}