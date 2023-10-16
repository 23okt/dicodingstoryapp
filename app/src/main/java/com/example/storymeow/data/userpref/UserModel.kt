package com.example.storymeow.data.userpref

data class UserModel (
    val token: String,
    val name: String,
    val userId: String,
    val isLogin: Boolean = false
)
