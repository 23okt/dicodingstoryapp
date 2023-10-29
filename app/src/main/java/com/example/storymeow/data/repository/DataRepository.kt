package com.example.storymeow.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storymeow.data.Result
import com.example.storymeow.data.paging.StoryPagingSource
import com.example.storymeow.data.response.ErrorResponse
import com.example.storymeow.data.response.ListStoryItem
import com.example.storymeow.data.response.LoginResponse
import com.example.storymeow.data.response.RegisterResponse
import com.example.storymeow.data.response.UploadResponse
import com.example.storymeow.data.retrofit.ApiService
import com.example.storymeow.data.userpref.UserModel
import com.example.storymeow.data.userpref.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import kotlin.coroutines.CoroutineContext

class DataRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
): CoroutineScope {

    companion object{
        private var INSTANCE: DataRepository? = null

        fun clearInstance(){
            INSTANCE = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): DataRepository =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: DataRepository(apiService,userPreferences)
            }.also { INSTANCE = it }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

   fun masuk(email: String,password: String): LiveData<Result<LoginResponse>> = liveData{
        emit(Result.Loading)
       try {
           val response = apiService.login(email,password)
           emit(Result.Success(response))
       }catch (e:HttpException){
           val error = e.response()?.errorBody()?.string()
           val body = Gson().fromJson(error,ErrorResponse::class.java)
           emit(Result.Error(body.message))
       }
   }

    fun daftar(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.register(name,email,password)
            emit(Result.Success(response))
        }catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(body.message))
        }
    }
    fun uploadImage(imageFile: File, description: String): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody,UploadResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun getStory(): LiveData<Result<List<ListStoryItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getStory()
            emit(Result.Success(response.listStory))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }


    fun getLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response.listStory))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    fun getPaging(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun saveSession(user: UserModel){
        userPreferences.saveSession(user)
    }
    fun getSession(): Flow<UserModel>{
        return userPreferences.getSession()
    }
    suspend fun logout(){
        userPreferences.logout()
    }
}