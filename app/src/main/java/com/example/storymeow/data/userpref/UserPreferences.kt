package com.example.storymeow.data.userpref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name= "login")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    companion object{
        private val NAME_KEY = stringPreferencesKey("name")
        private val USER_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")

        private var INSTANCE: UserPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserPreferences(dataStore)
            }.also { INSTANCE = it }
    }

    suspend fun saveSession(user: UserModel){
        dataStore.edit {
            it[NAME_KEY] = user.name
            it[USER_KEY] = user.userId
            it[TOKEN_KEY] = user.token
            it[IS_LOGIN] = true
        }
    }

    fun getSession(): Flow<UserModel>{
        return dataStore.data.map {
            UserModel(
                it[TOKEN_KEY].toString(),
                it[NAME_KEY].toString(),
                it[USER_KEY].toString(),
                it[IS_LOGIN]?: false
            )
        }
    }
    suspend fun logout(){
        dataStore.edit {
           it[TOKEN_KEY] = ""
           it[NAME_KEY] = ""
           it[USER_KEY] = ""
           it[IS_LOGIN] = false
        }
    }

}