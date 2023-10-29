package com.example.storymeow.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storymeow.data.repository.DataRepository
import com.example.storymeow.data.response.ListStoryItem
import com.example.storymeow.data.userpref.UserModel

class HomeViewModel(private val repository: DataRepository): ViewModel() {

    val paging: LiveData<PagingData<ListStoryItem>> =
        repository.getPaging().cachedIn(viewModelScope)
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
    suspend fun logout(){
        repository.logout()
    }
    fun getStories() = repository.getStory()
}