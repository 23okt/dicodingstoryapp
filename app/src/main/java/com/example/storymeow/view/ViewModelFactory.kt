package com.example.storymeow.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storymeow.data.Injection
import com.example.storymeow.data.repository.DataRepository
import com.example.storymeow.view.camera.CameraViewModel
import com.example.storymeow.view.home.HomeViewModel
import com.example.storymeow.view.login.LoginViewModel
import com.example.storymeow.view.maps.MapsViewModel
import com.example.storymeow.view.register.RegisterViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: DataRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)->{
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java)->{
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java)->{
                CameraViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java)->{
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
        }
    }

    companion object{
        private var INSTANCE: ViewModelFactory? = null

        fun clearInstance(){
            DataRepository.clearInstance()
            INSTANCE = null
        }
        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}