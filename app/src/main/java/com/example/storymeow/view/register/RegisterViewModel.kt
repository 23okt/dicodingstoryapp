package com.example.storymeow.view.register

import androidx.lifecycle.ViewModel
import com.example.storymeow.data.repository.DataRepository

class RegisterViewModel(private val repository: DataRepository): ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.daftar(name,email, password)
}