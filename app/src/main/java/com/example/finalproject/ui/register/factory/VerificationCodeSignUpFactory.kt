package com.example.finalproject.ui.register.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.register.repository.VerificationCodeSignUpRepository
import com.example.finalproject.ui.register.viewModels.VerificationCodeSignUpViewModel

class VerificationCodeSignUpFactory(
    private val verificationCodeSignUpRepository: VerificationCodeSignUpRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerificationCodeSignUpViewModel::class.java)){
            return VerificationCodeSignUpViewModel(verificationCodeSignUpRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}