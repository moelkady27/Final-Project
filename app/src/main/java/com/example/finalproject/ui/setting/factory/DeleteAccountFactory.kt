package com.example.finalproject.ui.setting.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.setting.repository.DeleteAccountRepository
import com.example.finalproject.ui.setting.viewModels.DeleteAccountViewModel

class DeleteAccountFactory(
    private val deleteAccountRepository: DeleteAccountRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteAccountViewModel::class.java)){
            return DeleteAccountViewModel(deleteAccountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}