package com.example.storymeow.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storymeow.data.repository.DataRepository
import com.example.storymeow.data.userpref.UserModel

class HomeViewModel(private val repository: DataRepository): ViewModel() {
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
    suspend fun logout(){
        repository.logout()
    }
    fun getStories() = repository.getStory()
}