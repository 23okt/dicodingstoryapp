package com.example.storymeow.view.maps

import androidx.lifecycle.ViewModel
import com.example.storymeow.data.repository.DataRepository

class MapsViewModel(private val repository: DataRepository): ViewModel() {
    fun getLocation() = repository.getLocation()
}