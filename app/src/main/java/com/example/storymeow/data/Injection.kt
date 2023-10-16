package com.example.storymeow.data

import android.content.Context
import com.example.storymeow.data.repository.DataRepository
import com.example.storymeow.data.retrofit.ApiConfig
import com.example.storymeow.data.userpref.UserPreferences
import com.example.storymeow.data.userpref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return DataRepository.getInstance(apiService,pref)
    }
}