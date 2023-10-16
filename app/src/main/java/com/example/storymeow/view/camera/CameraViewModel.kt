package com.example.storymeow.view.camera

import androidx.lifecycle.ViewModel
import com.example.storymeow.data.repository.DataRepository
import java.io.File

class CameraViewModel(private val repository: DataRepository): ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file,description)
}