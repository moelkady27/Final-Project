package com.example.finalproject.ui.password.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.password.repository.VerificationCodeForgetRepository
import com.example.finalproject.ui.password.viewModels.VerificationCodeForgetPasswordViewModel

class VerificationCodeForgetFactory(
    private val verificationCodeForgetPasswordRepository: VerificationCodeForgetRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerificationCodeForgetPasswordViewModel::class.java)){
            return VerificationCodeForgetPasswordViewModel(verificationCodeForgetPasswordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}