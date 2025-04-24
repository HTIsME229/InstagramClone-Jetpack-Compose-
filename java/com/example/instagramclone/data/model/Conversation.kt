package com.example.instagramclone.data.model

data class Conversation(
    val userId:String,
    val name: String,
    val avatarUrl: String,
    val lastMessage: String,
    val timestamp: Long,
    val isOnline: Boolean = false,
    val showCameraIcon: Boolean = true
)