package com.example.storymeow.view.login

import androidx.lifecycle.ViewModel
import com.example.storymeow.data.repository.DataRepository
import com.example.storymeow.data.userpref.UserModel

class LoginViewModel(private val repository: DataRepository): ViewModel() {

    fun login(email: String, password: String) = repository.masuk(email,password)

    suspend fun saveSession(userModel: UserModel){
        repository.saveSession(userModel)
    }
}