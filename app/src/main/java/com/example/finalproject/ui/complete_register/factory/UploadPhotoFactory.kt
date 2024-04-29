package com.example.finalproject.ui.complete_register.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.ui.complete_register.repository.UploadPhotoRepository
import com.example.finalproject.ui.complete_register.viewModels.UploadPhotoViewModel

class UploadPhotoFactory (
    private val uploadPhotoRepository: UploadPhotoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadPhotoViewModel::class.java)){
            return UploadPhotoViewModel(uploadPhotoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}